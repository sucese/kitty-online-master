package com.guoxiaoxing.kitty.ui.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.guoxiaoxing.kitty.adapter.SoftwareAdapter;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.bean.BaseObject;
import com.guoxiaoxing.kitty.bean.ListEntity;
import com.guoxiaoxing.kitty.bean.SoftwareDec;
import com.guoxiaoxing.kitty.bean.SoftwareList;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class SoftwareListFragment extends BaseListFragment<SoftwareDec> {

    public static final String BUNDLE_SOFTWARE = "BUNDLE_SOFTWARE";

    protected static final String TAG = SoftwareListFragment.class
	    .getSimpleName();
    private static final String CACHE_KEY_PREFIX = "softwarelist_";

    private String softwareType = "recommend";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Bundle args = getArguments();
	if (args != null) {
	    softwareType = args.getString(BUNDLE_SOFTWARE);
	}
    }

    @Override
    protected SoftwareAdapter getListAdapter() {
	return new SoftwareAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
	return CACHE_KEY_PREFIX + softwareType;
    }

    @Override
    protected SoftwareList parseList(InputStream is) throws Exception {
	SoftwareList list = XmlUtils.toBean(SoftwareList.class, is);
	return list;
    }

    @Override
    protected ListEntity readList(Serializable seri) {
	return ((SoftwareList) seri);
    }

    @Override
    protected void sendRequestData() {
	OSChinaApi.getSoftwareList(softwareType, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	SoftwareDec softwaredec = (SoftwareDec) mAdapter.getItem(position);
	if (softwaredec != null) {
	    String ident = softwaredec.getUrl().substring(softwaredec.getUrl().lastIndexOf("/") + 1);
	    UIHelper.showSoftwareDetail(getActivity(), ident);
	    // 放入已读列表
	    saveToReadedList(view, SoftwareList.PREF_READED_SOFTWARE_LIST,
		    softwaredec.getName());
	}
    }

    @Override
    protected boolean compareTo(List<? extends BaseObject> data, BaseObject enity) {
	int s = data.size();
	if (enity != null) {
	    for (int i = 0; i < s; i++) {
		if (((SoftwareDec) enity).getName().equals(
			((SoftwareDec) data.get(i)).getName())) {
		    return true;
		}
	    }
	}
	return false;
    }

}
