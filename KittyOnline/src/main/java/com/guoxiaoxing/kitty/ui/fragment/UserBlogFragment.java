package com.guoxiaoxing.kitty.ui.fragment;

import java.io.InputStream;
import java.io.Serializable;

import com.guoxiaoxing.kitty.adapter.BlogAdapter;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.base.BaseListFragment;
import com.guoxiaoxing.kitty.bean.Blog;
import com.guoxiaoxing.kitty.bean.BlogList;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import android.view.View;
import android.widget.AdapterView;

/**
 * 用户的博客列表(用用户的id来获取)
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月29日 下午5:09:13
 *
 */
public class UserBlogFragment extends BaseListFragment<Blog> {
	
	protected static final String TAG = UserBlogFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "user_bloglist_";
	
	@Override
	protected BlogAdapter getListAdapter() {
		return new BlogAdapter();
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX + mCatalog;
	}

	@Override
	protected BlogList parseList(InputStream is) throws Exception {
		BlogList list = XmlUtils.toBean(BlogList.class, is);
		return list;
	}

	@Override
	protected BlogList readList(Serializable seri) {
		return ((BlogList) seri);
	}

	@Override
	protected void sendRequestData() {
		OSChinaApi.getUserBlogList(mCatalog, "", mCatalog, mCurrentPage, mHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Blog blog = (Blog) mAdapter.getItem(position);
		if (blog != null)
			UIHelper.showUrlRedirect(view.getContext(), blog.getUrl());
	}
}
