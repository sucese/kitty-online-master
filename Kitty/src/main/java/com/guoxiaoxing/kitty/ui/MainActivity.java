package com.guoxiaoxing.kitty.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.guoxiaoxing.kitty.ui.fragment.HomeFragment;
import com.networkbench.agent.impl.NBSAppAgent;

import com.guoxiaoxing.kitty.AppConfig;
import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.AppManager;
import net.oschina.kitty.R;
import com.guoxiaoxing.kitty.bean.Constants;
import com.guoxiaoxing.kitty.bean.Notice;
import com.guoxiaoxing.kitty.bean.SimpleBackPage;
import com.guoxiaoxing.kitty.cache.DataCleanManager;
import com.guoxiaoxing.kitty.ui.fragment.MyInformationFragment;
import com.guoxiaoxing.kitty.interf.BaseViewInterface;
import com.guoxiaoxing.kitty.interf.OnTabReselectListener;
import com.guoxiaoxing.kitty.service.NoticeUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.UpdateManager;
import com.guoxiaoxing.kitty.widget.BadgeView;
import com.guoxiaoxing.kitty.widget.MyFragmentTabHost;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 应用启动入口
 *
 * @author guoxiaoxing
 */

@SuppressLint("InflateParams")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends AppCompatActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        OnTabChangeListener, BaseViewInterface, View.OnClickListener,
        OnTouchListener, HomeFragment.OnFragmentInteractionListener{

    private DoubleClickExitHelper mDoubleClickExit;

    /*左侧抽屉Fragment*/
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @InjectView(android.R.id.tabhost)
    public MyFragmentTabHost mTabHost;

    private BadgeView mBvNotice;

    public static Notice mNotice;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.INTENT_ACTION_NOTICE)) {
                mNotice = (Notice) intent.getSerializableExtra("notice_bean");
                int atmeCount = mNotice.getAtmeCount();// @我
                int msgCount = mNotice.getMsgCount();// 留言
                int reviewCount = mNotice.getReviewCount();// 评论
                int newFansCount = mNotice.getNewFansCount();// 新粉丝
                int newLikeCount = mNotice.getNewLikeCount();// 收到赞
                int activeCount = atmeCount + reviewCount + msgCount
                        + newFansCount + newLikeCount;

                Fragment fragment = getCurrentFragment();
                if (fragment instanceof MyInformationFragment) {
                    ((MyInformationFragment) fragment).setNotice();
                } else {
                    if (activeCount > 0) {
                        mBvNotice.setText(activeCount + "");
                        mBvNotice.show();
                    } else {
                        mBvNotice.hide();
                        mNotice = null;
                    }
                }
            } else if (intent.getAction()
                    .equals(Constants.INTENT_ACTION_LOGOUT)) {
                mBvNotice.hide();
                mNotice = null;
            }
        }
    };

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @InjectView(R.id.quick_option_iv)
    View mAddBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppContext.getNightModeSwitch()) {
            setTheme(R.style.AppBaseTheme_Night);
        } else {
            setTheme(R.style.AppBaseTheme_Light);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
        AppManager.getAppManager().addActivity(this);

        handleIntent(getIntent());
        // 注册听云的检测分析
        NBSAppAgent.setLicenseKey("0ed0cc66c5cb45c0a91c6fa932ca99ac")
                .withCrashReportEnabled(true).withLocationServiceEnabled(true)
                .start(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /**
     * 处理传进来的intent
     *
     * @param intent
     * @return void
     */
    private void handleIntent(Intent intent) {
        if (intent == null)
            return;
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_VIEW)) {
            UIHelper.showUrlRedirect(this, intent.getDataString());
        } else if (intent.getBooleanExtra("NOTICE", false)) {
            notifitcationBarClick(intent);
        }
    }

    /**
     * 从通知栏点击的时候相应
     */
    private void notifitcationBarClick(Intent fromWhich) {
        if (fromWhich != null) {
            boolean fromNoticeBar = fromWhich.getBooleanExtra("NOTICE", false);
            if (fromNoticeBar) {
                Intent toMyInfor = new Intent(this, SimpleBackActivity.class);
                toMyInfor.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE,
                        SimpleBackPage.MY_MES.getValue());
                startActivity(toMyInfor);
            }
        }
    }

    @Override
    public void initView() {
        mDoubleClickExit = new DoubleClickExitHelper(this);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }

        initTabs();

        // 中间按键图片触发
        mAddBt.setOnClickListener(this);

        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);

        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        registerReceiver(mReceiver, filter);
        NoticeUtils.bindToService(this);

        if (AppContext.isFristStart()) {
            mNavigationDrawerFragment.openDrawerMenu();
            DataCleanManager.cleanInternalCache(AppContext.getInstance());
            AppContext.setFristStart(false);
        }

        checkUpdate();
    }

    private void checkUpdate() {
        if (!AppContext.get(AppConfig.KEY_CHECK_UPDATE, true)) {
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                new UpdateManager(MainActivity.this, false).checkUpdate();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NoticeUtils.unbindFromService(this);
        unregisterReceiver(mReceiver);
        mReceiver = null;
        NoticeUtils.tryToShutDown(this);
    }

    @Override
    public void initData() {

    }

    private void initTabs() {
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;
        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];
            TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(
                    mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                    null);
            if (i == 2) {
                indicator.setVisibility(View.INVISIBLE);
                mTabHost.setNoTabChangedTag(getString(mainTab.getResName()));
            }
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(tab, mainTab.getClz(), null);

            if (mainTab.equals(MainTab.ME)) {
                View cn = indicator.findViewById(R.id.tab_mes);
                mBvNotice = new BadgeView(MainActivity.this, cn);
                mBvNotice.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                mBvNotice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                mBvNotice.setBackgroundResource(R.drawable.notification_bg);
                mBvNotice.setGravity(Gravity.CENTER);
            }
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                UIHelper.showSimpleBack(this, SimpleBackPage.SEARCH);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabChanged(String tabId) {
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
        if (tabId.equals(getString(MainTab.ME.getResName()))) {
            mBvNotice.setText("");
            mBvNotice.hide();
        }
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            // 点击了快速操作按钮
            case R.id.quick_option_iv:
                showQuickOption();
                break;
            default:
                break;
        }
    }

    // 显示快速操作界面
    private void showQuickOption() {
        final QuickOptionDialog dialog = new QuickOptionDialog(
                MainActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
        boolean consumed = false;
        // use getTabHost().getCurrentTabView to decide if the current tab is
        // touched again
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && v.equals(mTabHost.getCurrentTabView())) {
            // use getTabHost().getCurrentView() to get a handle to the view
            // which is displayed in the tab - and to get this views context
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
                consumed = true;
            }
        }
        return consumed;
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                mTabHost.getCurrentTabTag());
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            if (AppContext.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true)) {
                return mDoubleClickExit.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        // 当 API Level > 11 调用这个方法可能导致奔溃（android.os.Build.VERSION.SDK_INT > 11）
    }


    @Override
    public void onHomeFragmentInteraction(Uri uri) {

    }
}
