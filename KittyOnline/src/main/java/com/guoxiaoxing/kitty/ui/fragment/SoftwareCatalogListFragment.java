package com.guoxiaoxing.kitty.ui.fragment;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.SoftwareAdapter;
import com.guoxiaoxing.kitty.adapter.SoftwareCatalogListAdapter;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.bean.Entity;
import com.guoxiaoxing.kitty.bean.SoftwareCatalogList;
import com.guoxiaoxing.kitty.bean.SoftwareCatalogList.SoftwareType;
import com.guoxiaoxing.kitty.bean.SoftwareDec;
import com.guoxiaoxing.kitty.bean.SoftwareList;
import com.guoxiaoxing.kitty.ui.empty.EmptyLayout;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.guoxiaoxing.kitty.widget.ScrollLayout;

import cz.msebera.android.httpclient.Header;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class SoftwareCatalogListFragment extends BaseFragment implements
		OnItemClickListener, OnScrollListener {
	protected static final int STATE_NONE = 0;
	protected static final int STATE_REFRESH = 1;
	protected static final int STATE_LOADMORE = 2;

	private final static int SCREEN_CATALOG = 0;
	private final static int SCREEN_TAG = 1;
	private final static int SCREEN_SOFTWARE = 2;

	private static ScrollLayout mScrollLayout;
	private static ListView mLvCatalog, mLvTag, mLvSoftware;
	private static EmptyLayout mEmptyView;
	private static SoftwareCatalogListAdapter mCatalogAdapter, mTagAdapter;
	private static SoftwareAdapter mSoftwareAdapter;
	private static int mState = STATE_NONE;
	private static int curScreen = SCREEN_CATALOG;// 默认当前屏幕
	private static int mCurrentTag;
	private static int mCurrentPage;

	private AsyncHttpResponseHandler mCatalogHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			try {
				SoftwareCatalogList list = XmlUtils.toBean(
						SoftwareCatalogList.class, new ByteArrayInputStream(
								arg2));
				if (mState == STATE_REFRESH)
					mCatalogAdapter.clear();
				List<SoftwareType> data = list.getSoftwarecataloglist();
				mCatalogAdapter.addData(data);
				mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
				if (data.size() == 0 && mState == STATE_REFRESH) {
					mEmptyView.setErrorType(EmptyLayout.NODATA);
				} else {
					mCatalogAdapter
							.setState(ListBaseAdapter.STATE_LESS_ONE_PAGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(arg0, arg1, arg2, e);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			mEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};

	private AsyncHttpResponseHandler mTagHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			try {
				SoftwareCatalogList list = XmlUtils.toBean(
						SoftwareCatalogList.class, new ByteArrayInputStream(
								arg2));
				if (mState == STATE_REFRESH)
					mTagAdapter.clear();
				List<SoftwareType> data = list.getSoftwarecataloglist();
				mTagAdapter.addData(data);
				mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
				if (data.size() == 0 && mState == STATE_REFRESH) {
					mEmptyView.setErrorType(EmptyLayout.NODATA);
				} else {
					mTagAdapter.setState(ListBaseAdapter.STATE_LESS_ONE_PAGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(arg0, arg1, arg2, e);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			mEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};

	private AsyncHttpResponseHandler mSoftwareHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			try {
				SoftwareList list = XmlUtils.toBean(SoftwareList.class,
						new ByteArrayInputStream(responseBytes));
				executeOnLoadDataSuccess(list.getSoftwarelist());

			} catch (Exception e) {
				e.printStackTrace();
				onFailure(statusCode, headers, responseBytes, null);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			mEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};

	private OnItemClickListener mCatalogOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SoftwareType type = (SoftwareType) mCatalogAdapter
					.getItem(position);
			if (type != null && type.getTag() > 0) {
				// 加载二级分类
				curScreen = SCREEN_TAG;
				mScrollLayout.scrollToScreen(curScreen);
				mCurrentTag = type.getTag();
				sendRequestCatalogData(mTagHandler);
			}
		}
	};

	private OnItemClickListener mTagOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SoftwareType type = (SoftwareType) mTagAdapter.getItem(position);
			if (type != null && type.getTag() > 0) {
				// 加载二级分类里面的软件列表
				curScreen = SCREEN_SOFTWARE;
				mScrollLayout.scrollToScreen(curScreen);
				mCurrentTag = type.getTag();
				mState = STATE_REFRESH;
				sendRequestTagData();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_opensoftware, container,
				false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		mScrollLayout = (ScrollLayout) view.findViewById(R.id.scrolllayout);
		mScrollLayout.setIsScroll(false);

		mEmptyView = (EmptyLayout) view.findViewById(R.id.error_layout);
		mLvCatalog = (ListView) view.findViewById(R.id.lv_catalog);
		mLvCatalog.setOnItemClickListener(mCatalogOnItemClick);
		mLvTag = (ListView) view.findViewById(R.id.lv_tag);
		mLvTag.setOnItemClickListener(mTagOnItemClick);
		if (mCatalogAdapter == null) {
			mCatalogAdapter = new SoftwareCatalogListAdapter();
			sendRequestCatalogData(mCatalogHandler);
		}
		mLvCatalog.setAdapter(mCatalogAdapter);

		if (mTagAdapter == null) {
			mTagAdapter = new SoftwareCatalogListAdapter();
		}
		mLvTag.setAdapter(mTagAdapter);

		if (mSoftwareAdapter == null) {
			mSoftwareAdapter = new SoftwareAdapter();
		}

		mLvSoftware = (ListView) view.findViewById(R.id.lv_software);
		mLvSoftware.setOnItemClickListener(this);
		mLvSoftware.setOnScrollListener(this);
		mLvSoftware.setAdapter(mSoftwareAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SoftwareDec software = (SoftwareDec) mSoftwareAdapter.getItem(position);
		if (software != null)
			UIHelper.showUrlRedirect(view.getContext(), software.getUrl());
	}

	@Override
	public boolean onBackPressed() {
		mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		mCurrentPage = 0;
		switch (curScreen) {
		case SCREEN_SOFTWARE:
			curScreen = SCREEN_TAG;
			mScrollLayout.scrollToScreen(SCREEN_TAG);
			return true;
		case SCREEN_TAG:
			curScreen = SCREEN_CATALOG;
			mScrollLayout.scrollToScreen(SCREEN_CATALOG);
			return true;
		case SCREEN_CATALOG:
			return false;
		}
		return super.onBackPressed();
	}

	private void sendRequestCatalogData(AsyncHttpResponseHandler handler) {
		mState = STATE_REFRESH;
		mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
		OSChinaApi.getSoftwareCatalogList(mCurrentTag, handler);
	}

	private void sendRequestTagData() {
		OSChinaApi.getSoftwareTagList(mCurrentTag, mCurrentPage,
				mSoftwareHandler);
	}

	private void executeOnLoadDataSuccess(List<SoftwareDec> data) {
		if (data == null) {
			return;
		}
		mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		
		if (mCurrentPage == 0) {
			mSoftwareAdapter.clear();
		}

		for (int i = 0; i < data.size(); i++) {
			if (compareTo(mSoftwareAdapter.getData(), data.get(i))) {
				data.remove(i);
			}
		}
		int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if (mSoftwareAdapter.getCount() == 0 && mState == STATE_NONE) {
        	mEmptyView.setErrorType(EmptyLayout.NODATA);
        } else if (data.size() == 0 || (data.size() < 20 && mCurrentPage == 0)) {
        	adapterState = ListBaseAdapter.STATE_NO_MORE;
        } else {
        	adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        mSoftwareAdapter.setState(adapterState);
        mSoftwareAdapter.addData(data);
	}

	private boolean compareTo(List<? extends Entity> data, SoftwareDec enity) {
		int s = data.size();
		if (enity != null) {
			for (int i = 0; i < s; i++) {
				if (enity.getName().equals(
						((SoftwareDec) data.get(i)).getName())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
		if (mState == STATE_NOMORE || mState == STATE_LOADMORE
				|| mState == STATE_REFRESH) {
			return;
		}
		if (mSoftwareAdapter != null
				&& mSoftwareAdapter.getDataSize() > 0
				&& mLvSoftware.getLastVisiblePosition() == (mLvSoftware
						.getCount() - 1)) {
			if (mState == STATE_NONE
					&& mSoftwareAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
				mState = STATE_LOADMORE;
				mCurrentPage++;
				sendRequestTagData();
			}
		}
	}
}
