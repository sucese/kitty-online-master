package com.guoxiaoxing.kitty.viewpagerfragment;

import net.oschina.kitty.R;
import com.guoxiaoxing.kitty.adapter.ViewPageFragmentAdapter;
import com.guoxiaoxing.kitty.base.BaseListFragment;
import com.guoxiaoxing.kitty.base.BaseViewPagerFragment;
import com.guoxiaoxing.kitty.bean.SearchList;
import com.guoxiaoxing.kitty.ui.fragment.SearchFragment;
import com.guoxiaoxing.kitty.util.TDevice;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SearchViewPageFragment extends BaseViewPagerFragment {
	
	
	private SearchView mSearchView;
	
	public static SearchViewPageFragment newInstance(){
		return new SearchViewPageFragment();
	}
	
	@Override
	protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
		String[] title = getResources().getStringArray(R.array.search);
		adapter.addTab(title[0], "search_soft", SearchFragment.class, getBundle(SearchList.CATALOG_SOFTWARE));
		adapter.addTab(title[1], "search_quest", SearchFragment.class, getBundle(SearchList.CATALOG_POST));
		adapter.addTab(title[2], "search_blog", SearchFragment.class, getBundle(SearchList.CATALOG_BLOG));
		adapter.addTab(title[3], "search_news", SearchFragment.class, getBundle(SearchList.CATALOG_NEWS));
	}
	
	private Bundle getBundle(String catalog) {
		Bundle bundle = new Bundle();
		bundle.putString(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
		return bundle;
	}
	

	@Override
	protected void setScreenPageLimit() {
		mViewPager.setOffscreenPageLimit(3);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.search_menu, menu);
		MenuItem search=menu.findItem(R.id.search_content);
		mSearchView=(SearchView) search.getActionView();
		mSearchView.setIconifiedByDefault(false);
		setSearch();
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	private void setSearch() {
		mSearchView.setQueryHint("搜索");
		TextView textView = (TextView) mSearchView.findViewById(R.id.search_src_text);
		textView.setTextColor(Color.WHITE);
		
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				TDevice.hideSoftKeyboard(mSearchView);
				search(arg0);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				return false;
			}
		});
		mSearchView.requestFocus();
	}
	
	private void search(String content) {
		int index = mViewPager.getChildCount();
		for (int i = 0; i < index; i++) {
			SearchFragment fragment = (SearchFragment) getChildFragmentManager().getFragments().get(i);
			if (fragment != null) {
				fragment.search(content);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
