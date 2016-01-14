package com.guoxiaoxing.kitty.ui.fragment;

import java.io.InputStream;
import java.io.Serializable;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.guoxiaoxing.kitty.adapter.TweetLikeUsersAdapter;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.ui.base.BaseActivity;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.bean.TweetLikeUserList;
import com.guoxiaoxing.kitty.bean.User;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;

/**
 * TweetLikeUsersFragment.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-3-26 下午4:04:12
 */
public class TweetLikeUsersFragment extends BaseListFragment<User> {
    
    @Override
    protected TweetLikeUsersAdapter getListAdapter() {
	// TODO Auto-generated method stub
	return new TweetLikeUsersAdapter();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setActionBarTitle("动弹点赞列表");
    }
    
    @Override
    protected String getCacheKeyPrefix() {
        // TODO Auto-generated method stub
        return "tweet_like_list_" + mCatalog;
    }
    
    @Override
    protected TweetLikeUserList parseList(InputStream is) throws Exception {
        // TODO Auto-generated method stub
	TweetLikeUserList list = XmlUtils.toBean(TweetLikeUserList.class, is);
        return list;
    }
    
    @Override
    protected TweetLikeUserList readList(Serializable seri) {
        // TODO Auto-generated method stub
        return (TweetLikeUserList) seri;
    }
    
    @Override
    protected void sendRequestData() {
        // TODO Auto-generated method stub
	OSChinaApi.getTweetLikeList(mCatalog, mCurrentPage, mHandler);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
	User item = mAdapter.getItem(position);
	if (item != null && item.getId() > 0) {
	    UIHelper.showUserCenter(getActivity(), item.getId(), item.getName());
	}
    }
}

