package net.oschina.app.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.SimpleBackPage;
import net.oschina.app.util.TDevice;
import net.oschina.app.util.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 侧滑菜单界面
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 下午6:00:05
 */
public class NavigationDrawerFragment extends BaseFragment implements
        OnClickListener {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private View mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;

    @InjectView(R.id.menu_item_quests)
    View mMenu_item_quests;

    @InjectView(R.id.menu_item_opensoft)
    View mMenu_item_opensoft;

    @InjectView(R.id.menu_item_blog)
    View mMenu_item_blog;

    @InjectView(R.id.menu_item_gitapp)
    View mMenu_item_gitapp;

    @InjectView(R.id.menu_item_setting)
    View mMenu_item_setting;

    @InjectView(R.id.menu_item_theme)
    View mMenu_item_theme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState
                    .getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = inflater.inflate(R.layout.fragment_navigation_drawer,
                container, false);
        mDrawerListView.setOnClickListener(this);
        ButterKnife.inject(this, mDrawerListView);
        initView(mDrawerListView);
        initData();
        return mDrawerListView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.menu_item_quests:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.QUEST);
                break;
            case R.id.menu_item_opensoft:
                UIHelper.showSimpleBack(getActivity(),
                        SimpleBackPage.OPENSOURCE_SOFTWARE);
                break;
            case R.id.menu_item_blog:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.BLOG);
                break;
            case R.id.menu_item_gitapp:

                boolean res = TDevice.openAppActivity(getActivity(),
                        "net.oschina.gitapp", "net.oschina.gitapp.WelcomePage");

                if (!res) {
                    if (!TDevice.isHaveMarket(getActivity())) {
                        UIHelper.openSysBrowser(getActivity(),
                                "http://git.oschina.net/appclient");
                    } else {
                        TDevice.gotoMarket(getActivity(), "net.oschina.gitapp");
                    }
                }
                break;
            case R.id.menu_item_setting:
                UIHelper.showSetting(getActivity());
                break;
            case R.id.menu_item_theme:
                switchTheme();
                break;
            default:
                break;

        }
        mDrawerLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        }, 800);
    }

    private void switchTheme() {
        if (AppContext.getNightModeSwitch()) {
            AppContext.setNightModeSwitch(false);
        } else {
            AppContext.setNightModeSwitch(true);
        }

        if (AppContext.getNightModeSwitch()) {
            getActivity().setTheme(R.style.AppBaseTheme_Night);
        } else {
            getActivity().setTheme(R.style.AppBaseTheme_Light);
        }

        getActivity().recreate();
    }

    @Override
    public void initView(View view) {

        TextView night = (TextView) view.findViewById(R.id.tv_night);
        if (AppContext.getNightModeSwitch()) {
            night.setText("日间");
        } else {
            night.setText("夜间");
        }

        mMenu_item_opensoft.setOnClickListener(this);
        mMenu_item_blog.setOnClickListener(this);
        mMenu_item_quests.setOnClickListener(this);

        mMenu_item_setting.setOnClickListener(this);
        mMenu_item_theme.setOnClickListener(this);

        mMenu_item_gitapp.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null
                && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation
     * drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                null, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActivity().invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void openDrawerMenu() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }
}
