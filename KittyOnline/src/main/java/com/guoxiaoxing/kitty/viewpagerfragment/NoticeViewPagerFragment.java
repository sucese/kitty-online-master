package com.guoxiaoxing.kitty.viewpagerfragment;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.ViewPageFragmentAdapter;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.ui.base.BaseViewPagerFragment;
import com.guoxiaoxing.kitty.model.ActiveList;
import com.guoxiaoxing.kitty.model.Constants;
import com.guoxiaoxing.kitty.model.FriendsList;
import com.guoxiaoxing.kitty.model.Notice;
import com.guoxiaoxing.kitty.ui.fragment.ActiveFragment;
import com.guoxiaoxing.kitty.ui.fragment.FriendsFragment;
import com.guoxiaoxing.kitty.ui.fragment.MessageFragment;
import com.guoxiaoxing.kitty.ui.fragment.TweetsLikesFragment;
import com.guoxiaoxing.kitty.ui.MainActivity;
import com.guoxiaoxing.kitty.widget.BadgeView;
import com.guoxiaoxing.kitty.widget.PagerSlidingTabStrip.OnPagerChangeLis;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

/**
 * 消息中心页面
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @author kymjs (https://github.com/kymjs)
 * @created 2014年9月25日 下午2:21:52
 * 
 */
public class NoticeViewPagerFragment extends BaseViewPagerFragment {

    public BadgeView mBvAtMe, mBvComment, mBvMsg, mBvFans, mBvLike;
    public static int sCurrentPage = 0;
    public static int[] sShowCount = new int[] { 0, 0, 0, 0, 0}; // 当前界面显示了多少次
    private BroadcastReceiver mNoticeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setNoticeTip();
            changePagers();
        }
    };

    /**
     * 界面每次显示，去重置tip的显示
     */
    @Override
    public void onResume() {
        super.onResume();
        setNoticeTip();
        changePagers();
        mViewPager.setOffscreenPageLimit(2);
    }

    /**
     * 设置tip
     */
    private void setNoticeTip() {
        Notice notice = MainActivity.mNotice;
        if (notice != null) {
            changeTip(mBvAtMe, notice.getAtmeCount());// @我
            changeTip(mBvComment, notice.getReviewCount());// 评论
            changeTip(mBvMsg, notice.getMsgCount());// 私信
            changeTip(mBvFans, notice.getNewFansCount());// 新粉丝
            changeTip(mBvLike, notice.getNewLikeCount());// 点赞数
        } else {
            switch (mViewPager.getCurrentItem()) {
            case 0:
                changeTip(mBvAtMe, -1);
                break;
            case 1:
                changeTip(mBvComment, -1);
                break;
            case 2:
                changeTip(mBvMsg, -1);
                break;
            case 3:
                changeTip(mBvFans, -1);
                break;
            case 4:
        	changeTip(mBvLike, -1);
        	break;
            }
        }
    }

    /**
     * 判断指定控件是否应该显示tip红点
     * 
     * @author kymjs
     */
    private void changeTip(BadgeView view, int count) {
        if (count > 0) {
            view.setText(count + "");
            view.show();
        } else {
            view.hide();
        }
    }

    /**
     * 当前tip是否在显示
     * 
     * @param which
     *            哪个界面的tip
     * @return
     */
    private boolean tipIsShow(int which) {
        switch (which) {
        case 0:
            return mBvAtMe.isShown();
        case 1:
            return mBvComment.isShown();
        case 2:
            return mBvMsg.isShown();
        case 3:
            return mBvFans.isShown();
        case 4:
            return mBvLike.isShown();
        default:
            return false;
        }
    }

    /**
     * 首次进入，切换到有tip的page
     */
    private void changePagers() {
        Notice notice = MainActivity.mNotice;
        if (notice == null) {
            return;
        }
        if (notice.getAtmeCount() != 0) {
            mViewPager.setCurrentItem(0);
            sCurrentPage = 0;
            refreshPage(0);
            sShowCount[0] = 1;
        } else if (notice.getReviewCount() != 0) {
            mViewPager.setCurrentItem(1);
            sCurrentPage = 1;
            refreshPage(1);
            sShowCount[1] = 1;
        } else if (notice.getMsgCount() != 0) {
            mViewPager.setCurrentItem(2);
            sCurrentPage = 2;
            refreshPage(2);
            sShowCount[2] = 1;
        } else if (notice.getNewFansCount() != 0) {
            mViewPager.setCurrentItem(3);
            sCurrentPage = 3;
            refreshPage(3);
            sShowCount[3] = 1;
        } else if (notice.getNewLikeCount() != 0) {
            mViewPager.setCurrentItem(4);
            sCurrentPage = 4;
            refreshPage(4);
            sShowCount[4] = 1;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 注册接收者接受tip广播
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
        getActivity().registerReceiver(mNoticeReceiver, filter);

        mBvAtMe = new BadgeView(getActivity(), mTabStrip.getBadgeView(0));
        mBvAtMe.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mBvAtMe.setBadgePosition(BadgeView.POSITION_CENTER);
        mBvAtMe.setGravity(Gravity.CENTER);
        mBvAtMe.setBackgroundResource(R.drawable.notification_bg);

        mBvComment = new BadgeView(getActivity(), mTabStrip.getBadgeView(1));
        mBvComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mBvComment.setBadgePosition(BadgeView.POSITION_CENTER);
        mBvComment.setGravity(Gravity.CENTER);
        mBvComment.setBackgroundResource(R.drawable.notification_bg);

        mBvMsg = new BadgeView(getActivity(), mTabStrip.getBadgeView(2));
        mBvMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mBvMsg.setBadgePosition(BadgeView.POSITION_CENTER);
        mBvMsg.setGravity(Gravity.CENTER);
        mBvMsg.setBackgroundResource(R.drawable.notification_bg);

        mBvFans = new BadgeView(getActivity(), mTabStrip.getBadgeView(3));
        mBvFans.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mBvFans.setBadgePosition(BadgeView.POSITION_CENTER);
        mBvFans.setGravity(Gravity.CENTER);
        mBvFans.setBackgroundResource(R.drawable.notification_bg);
        
        mBvLike = new BadgeView(getActivity(), mTabStrip.getBadgeView(4));
        mBvLike.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mBvLike.setBadgePosition(BadgeView.POSITION_CENTER);
        mBvLike.setGravity(Gravity.CENTER);
        mBvLike.setBackgroundResource(R.drawable.notification_bg);

        mTabStrip.getBadgeView(0).setVisibility(View.GONE);
        mTabStrip.getBadgeView(1).setVisibility(View.VISIBLE);
        mTabStrip.getBadgeView(2).setVisibility(View.VISIBLE);
        mTabStrip.getBadgeView(3).setVisibility(View.VISIBLE);
        mTabStrip.getBadgeView(4).setVisibility(View.VISIBLE);
        initData();
        initView(view);
    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mNoticeReceiver);
        mNoticeReceiver = null;
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.mymes_viewpage_arrays);
        adapter.addTab(title[0], "active_me", ActiveFragment.class,
                getBundle(ActiveList.CATALOG_ATME));
        adapter.addTab(title[1], "active_comment", ActiveFragment.class,
                getBundle(ActiveList.CATALOG_COMMENT));
        adapter.addTab(title[2], "active_mes", MessageFragment.class, null);
        Bundle bundle = getBundle(FriendsList.TYPE_FANS);
        bundle.putInt(FriendsFragment.BUNDLE_KEY_UID, AppContext.getInstance()
                .getLoginUid());
        adapter.addTab(title[3], "active_fans", FriendsFragment.class, bundle);
        adapter.addTab(title[4], "my_tweet", TweetsLikesFragment.class, null);
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView(View view) {
        changePagers();
        mViewPager.setOffscreenPageLimit(3);
        mTabStrip.setOnPagerChange(new OnPagerChangeLis() {
            @Override
            public void onChanged(int page) {
                refreshPage(page);
                sShowCount[page]++;
                sCurrentPage = page;
            }
        });
    }

    private void refreshPage(int index) {
        if (tipIsShow(index)) {
            try {
                ((BaseListFragment) getChildFragmentManager().getFragments()
                        .get(index)).onRefresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initData() {}
}
