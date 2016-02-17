package com.guoxiaoxing.kitty.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import cz.msebera.android.httpclient.Header;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.TweetAdapter;
import com.guoxiaoxing.kitty.api.OperationResponseHandler;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.bean.UserTalk;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.bean.Constants;
import com.guoxiaoxing.kitty.bean.Result;
import com.guoxiaoxing.kitty.bean.ResultBean;
import com.guoxiaoxing.kitty.bean.TweetsList;
import com.guoxiaoxing.kitty.ui.base.OnTabReselectListener;
import com.guoxiaoxing.kitty.ui.empty.EmptyLayout;
import com.guoxiaoxing.kitty.util.DialogHelp;
import com.guoxiaoxing.kitty.util.HTMLUtil;
import com.guoxiaoxing.kitty.util.TDevice;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 *
 * @author guoxiaoxing
 */
public class TalksFragment extends BaseListFragment<UserTalk> implements
        OnItemLongClickListener, OnTabReselectListener {

    private static final String TAG = TalksFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "tweetslist_";

    class DeleteTweetResponseHandler extends OperationResponseHandler {

        DeleteTweetResponseHandler(Object... args) {
            super(args);
        }

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
                throws Exception {
            try {
                Result res = XmlUtils.toBean(ResultBean.class, is).getResult();
                if (res != null && res.OK()) {
                    AppContext.showToastShort(R.string.delete_success);
                    UserTalk userTalk = (UserTalk) args[0];
                    mAdapter.removeItem(userTalk);
                    mAdapter.notifyDataSetChanged();
                } else {
                    onFailure(code, res.getErrorMessage(), args);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(code, e.getMessage(), args);
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            AppContext.showToastShort(R.string.delete_faile);
        }
    }

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
    protected TweetAdapter getListAdapter() {
        return new TweetAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String str = bundle.getString("topic");
            if (str != null) {
                return str;
            }
        }
        return CACHE_KEY_PREFIX + mCatalog;
    }

    public String getTopic() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String str = bundle.getString("topic");
            if (str != null) {
                return str;
            }
        }
        return "";
    }

    @Override
    protected TweetsList parseList(InputStream is) throws Exception {
        TweetsList list = XmlUtils.toBean(TweetsList.class, is);
        return list;
    }

    @Override
    protected TweetsList readList(Serializable seri) {
        return ((TweetsList) seri);
    }

    @Override
    protected void sendRequestData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String str = bundle.getString("topic");
            if (str != null) {
                OSChinaApi.getTweetTopicList(mCurrentPage, str, mHandler);
                return;
            }
        }
        OSChinaApi.getTweetList(mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        UserTalk userTalk = mAdapter.getItem(position);
        if (userTalk != null) {
            UIHelper.showTweetDetail(view.getContext(), null, userTalk.getId());
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setupContent();
        }
    };

    private void setupContent() {
        if (AppContext.getInstance().isLogin()) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            requestData(true);
        } else {
            mCatalog = TweetsList.CATALOG_ME;
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

    @Override
    protected void requestData(boolean refresh) {
        if (mCatalog > 0) {
            if (AppContext.getInstance().isLogin()) {
                mCatalog = AppContext.getInstance().getLoginUid();
                super.requestData(refresh);
            } else {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
            }
        } else {
            super.requestData(refresh);
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setOnItemLongClickListener(this);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCatalog > 0) {
                    if (AppContext.getInstance().isLogin()) {
                        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                        requestData(true);
                    } else {
                        UIHelper.showLoginActivity(getActivity());
                    }
                } else {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    requestData(true);
                }
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        UserTalk userTalk = mAdapter.getItem(position);
        if (userTalk != null) {
            handleLongClick(userTalk);
            return true;
        }
        return false;
    }

    private void handleLongClick(final UserTalk userTalk) {
        String[] items = null;
        if (AppContext.getInstance().getLoginUid() == userTalk.getAuthorid()) {
            items = new String[] { getResources().getString(R.string.copy),
                    getResources().getString(R.string.delete) };
        } else {
            items = new String[] { getResources().getString(R.string.copy) };
        }

        DialogHelp.getSelectDialog(getActivity(), items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    TDevice.copyTextToBoard(HTMLUtil.delHTMLTag(userTalk.getBody()));
                } else if (i == 1) {
                    handleDeleteTweet(userTalk);
                }
            }
        }).show();
    }

    private void handleDeleteTweet(final UserTalk userTalk) {
        DialogHelp.getConfirmDialog(getActivity(), "是否删除该动弹?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OSChinaApi.deleteTweet(userTalk.getAuthorid(), userTalk
                        .getId(), new DeleteTweetResponseHandler(userTalk));
            }
        }).show();
    }

    @Override
    public void onTabReselect() {
        onRefresh();
    }

    @Override
    protected long getAutoRefreshTime() {
        // 最新动弹3分钟刷新一次
        if (mCatalog == TweetsList.CATALOG_LATEST) {
            return 3 * 60;
        }
        return super.getAutoRefreshTime();
    }
}