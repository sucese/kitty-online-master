package com.guoxiaoxing.kitty.ui.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.TweetLikeAdapter;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.model.Constants;
import com.guoxiaoxing.kitty.model.BaseObject;
import com.guoxiaoxing.kitty.model.Notice;
import com.guoxiaoxing.kitty.model.UserTalk;
import com.guoxiaoxing.kitty.model.TweetLike;
import com.guoxiaoxing.kitty.model.TweetLikeList;
import com.guoxiaoxing.kitty.model.TweetsList;
import com.guoxiaoxing.kitty.service.NoticeUtils;
import com.guoxiaoxing.kitty.ui.empty.EmptyLayout;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.guoxiaoxing.kitty.viewpagerfragment.NoticeViewPagerFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * 赞过我动弹的列表
 * 
 * @date 2014年10月10日
 */
public class TweetsLikesFragment extends BaseListFragment<TweetLike> {

    protected static final String TAG = TweetsLikesFragment.class
	    .getSimpleName();
    private static final String CACHE_KEY_PREFIX = "mytweets_like_list_";

    private boolean mIsWatingLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	if (mCatalog > 0) {
	    IntentFilter filter = new IntentFilter(
		    Constants.INTENT_ACTION_USER_CHANGE);
	    filter.addAction(Constants.INTENT_ACTION_LOGOUT);
	    getActivity().registerReceiver(mReceiver, filter);
	}
    }

    @Override
    public void onDestroy() {
	if (mCatalog > 0) {
	    getActivity().unregisterReceiver(mReceiver);
	}
	super.onDestroy();
    }

    @Override
    protected TweetLikeAdapter getListAdapter() {
	return new TweetLikeAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
	return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected TweetLikeList parseList(InputStream is) throws Exception {
	TweetLikeList list = XmlUtils.toBean(TweetLikeList.class, is);
	return list;
    }

    @Override
    protected TweetLikeList readList(Serializable seri) {
	return ((TweetLikeList) seri);
    }

    @Override
    protected void sendRequestData() {
	OSChinaApi.getTweetLikeList(mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	UserTalk userTalk = mAdapter.getItem(position).getUserTalk();
	if (userTalk != null) {
	    UIHelper.showTweetDetail(view.getContext(), null, userTalk.getId());
	}
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

	@Override
	public void onReceive(Context context, Intent intent) {
	    setupContent();
	}
    };

    private void setupContent() {
	if (AppContext.getInstance().isLogin()) {
	    mIsWatingLogin = false;
	    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
	    requestData(true);
	} else {
	    mCatalog = TweetsList.CATALOG_ME;
	    mIsWatingLogin = true;
	    mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
	    mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
	}
    }

    @Override
    protected void requestData(boolean refresh) {
	if (AppContext.getInstance().isLogin()) {
	    mCatalog = AppContext.getInstance().getLoginUid();
	    mIsWatingLogin = false;
	    super.requestData(refresh);
	} else {
	    mIsWatingLogin = true;
	    mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
	    mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
	}
    }

    @Override
    public void initView(View view) {
	super.initView(view);
	mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (AppContext.getInstance().isLogin()) {
		    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
		    requestData(true);
		} else {
		    UIHelper.showLoginActivity(getActivity());
		}
	    }
	});
    }

    @Override
    protected long getAutoRefreshTime() {
	if (mCatalog == TweetsList.CATALOG_LATEST) {
	    return 3 * 60;
	}
	return super.getAutoRefreshTime();
    }

    @Override
    protected void onRefreshNetworkSuccess() {
	// TODO Auto-generated method stub
	super.onRefreshNetworkSuccess();
	if (AppContext.getInstance().isLogin()
		&& mCatalog == AppContext.getInstance().getLoginUid()
		&& 4 == NoticeViewPagerFragment.sCurrentPage) {
	    NoticeUtils.clearNotice(Notice.TYPE_NEWLIKE);
	    UIHelper.sendBroadcastForNotice(getActivity());
	}
    }
    
    protected boolean compareTo(List<? extends BaseObject> data, BaseObject enity) {
        int s = data.size();
        
        if (enity != null && enity instanceof TweetLike) {
            TweetLike tweetLike = (TweetLike) enity;
            for (int i = 0; i < s; i++) {
                if (tweetLike.getUser().getId() == ((TweetLike)data.get(i)).getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}