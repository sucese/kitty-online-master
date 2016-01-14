package com.guoxiaoxing.kitty.viewpagerfragment;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.ViewPageFragmentAdapter;
import com.guoxiaoxing.kitty.ui.base.BaseViewPagerFragment;
import com.guoxiaoxing.kitty.bean.EventList;
import com.guoxiaoxing.kitty.ui.fragment.EventFragment;
import android.os.Bundle;
import android.view.View;

/** 
 * 活动viewpager页面
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年12月24日 下午4:46:04 
 * 
 */
public class EventViewPagerFragment extends BaseViewPagerFragment {

	@Override
	protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
		String[] title = getResources().getStringArray(R.array.events);
		adapter.addTab(title[0], "new_event", EventFragment.class, getBundle(EventList.EVENT_LIST_TYPE_NEW_EVENT));
		adapter.addTab(title[1], "my_event", EventFragment.class, getBundle(EventList.EVENT_LIST_TYPE_MY_EVENT));
	}
	
	private Bundle getBundle(int event_type) {
		Bundle bundle = new Bundle();
		bundle.putInt(EventFragment.BUNDLE_KEY_EVENT_TYPE, event_type);
		return bundle;
	}
	
	@Override
	public void onClick(View v) {

	}

	@Override
	public void initView(View view) {

	}

	@Override
	public void initData() {

	}
}
