package com.guoxiaoxing.kitty.ui.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.adapter.FriendAdapter;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.bean.Entity;
import com.guoxiaoxing.kitty.bean.Friend;
import com.guoxiaoxing.kitty.bean.FriendsList;
import com.guoxiaoxing.kitty.bean.Notice;
import com.guoxiaoxing.kitty.service.NoticeUtils;
import com.guoxiaoxing.kitty.ui.MainActivity;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.guoxiaoxing.kitty.viewpagerfragment.NoticeViewPagerFragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * 关注、粉丝
 *
 * @author guoxiaoxing
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FriendsFragment extends BaseListFragment<Friend> {

    public final static String BUNDLE_KEY_UID = "UID";

    protected static final String TAG = FriendsFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "friend_list";

    private int mUid;

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mUid = args.getInt(BUNDLE_KEY_UID, 0);
        }
    }

    @Override
    public void onResume() {
        if (mCatalog == FriendsList.TYPE_FANS
                && mUid == AppContext.getInstance().getLoginUid()) {
            refreshNotice();
        }
        super.onResume();
    }

    private void refreshNotice() {
        Notice notice = MainActivity.mNotice;
        if (notice != null && notice.getNewFansCount() > 0) {
            onRefresh();
        }
    }

    @Override
    protected FriendAdapter getListAdapter() {
        return new FriendAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + "_" + mCatalog + "_" + mUid;
    }

    @Override
    protected FriendsList parseList(InputStream is) throws Exception {
        FriendsList list = XmlUtils.toBean(FriendsList.class, is);
        return list;
    }

    @Override
    protected FriendsList readList(Serializable seri) {
        return ((FriendsList) seri);
    }

    @Override
    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (((Friend) enity).getUserid() == ((Friend) data.get(i))
                        .getUserid()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getFriendList(mUid, mCatalog, mCurrentPage, mHandler);
    }

    @Override
    protected void onRefreshNetworkSuccess() {
        if ((NoticeViewPagerFragment.sCurrentPage == 3 || NoticeViewPagerFragment.sShowCount[3] > 0)
                && mCatalog == FriendsList.TYPE_FANS
                && mUid == AppContext.getInstance().getLoginUid()) {
            NoticeUtils.clearNotice(Notice.TYPE_NEWFAN);
            UIHelper.sendBroadcastForNotice(getActivity());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Friend item = (Friend) mAdapter.getItem(position);
        if (item != null) {
            if (mUid == AppContext.getInstance().getLoginUid()) {
                UIHelper.showMessageDetail(getActivity(), item.getUserid(),
                        item.getName());
                return;
            }
            UIHelper.showUserCenter(getActivity(), item.getUserid(),
                    item.getName());
        }
    }
}
