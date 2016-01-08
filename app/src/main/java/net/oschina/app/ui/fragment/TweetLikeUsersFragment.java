package net.oschina.app.ui.fragment;

import java.io.InputStream;
import java.io.Serializable;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import net.oschina.app.adapter.TweetLikeUsersAdapter;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseActivity;
import net.oschina.app.base.BaseListFragment;
import net.oschina.app.bean.TweetLikeUserList;
import net.oschina.app.bean.User;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;

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

