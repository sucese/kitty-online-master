package com.guoxiaoxing.kitty.ui.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.adapter.EventApplyAdapter;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.model.Apply;
import com.guoxiaoxing.kitty.model.EventAppliesList;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 活动出席人员列表
 *
 * @author guoxiaoxing
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EventAppliesFragment extends BaseListFragment<Apply> {

    protected static final String TAG = EventAppliesFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "event_apply_user_list";

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    protected EventApplyAdapter getListAdapter() {
        return new EventApplyAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + "_" + mCatalog;
    }

    @Override
    protected EventAppliesList parseList(InputStream is) throws Exception {
        EventAppliesList list = XmlUtils.toBean(EventAppliesList.class, is);
        return list;
    }

    @Override
    protected EventAppliesList readList(Serializable seri) {
        return ((EventAppliesList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getEventApplies(mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Apply item = (Apply) mAdapter.getItem(position);
        if (item != null) {
            if (AppContext.getInstance().isLogin()) {
                UIHelper.showMessageDetail(getActivity(), item.getId(), item.getName());
                return;
            }
            UIHelper.showUserCenter(getActivity(), item.getId(), item.getName());
        }

    }
}
