package net.oschina.app.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.adapter.MessageDetailAdapter;
import net.oschina.app.api.OperationResponseHandler;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseActivity;
import net.oschina.app.base.BaseListFragment;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.CommentList;
import net.oschina.app.bean.Constants;
import net.oschina.app.bean.MessageDetail;
import net.oschina.app.bean.MessageDetailList;
import net.oschina.app.bean.Result;
import net.oschina.app.bean.ResultBean;
import net.oschina.app.bean.User;
import net.oschina.app.emoji.KJEmojiFragment;
import net.oschina.app.emoji.OnSendClickListener;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.DialogHelp;
import net.oschina.app.util.HTMLUtil;
import net.oschina.app.util.TDevice;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;

import cz.msebera.android.httpclient.Header;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 与某人的聊天记录界面（私信详情）
 *
 * @author kymjs (http://www.kymjs.com/)
 *
 */
public class MessageDetailFragment extends BaseListFragment<MessageDetail> implements
        OnItemLongClickListener, OnSendClickListener,MessageDetailAdapter.OnRetrySendMessageListener{
    protected static final String TAG = ActiveFragment.class.getSimpleName();
    public static final String BUNDLE_KEY_FID = "BUNDLE_KEY_FID";
    public static final String BUNDLE_KEY_FNAME = "BUNDLE_KEY_FNAME";
    private static final String CACHE_KEY_PREFIX = "message_detail_list";
    //时间间隔（要求：聊天时间显示，时间间隔为五分钟以上才显示出来）
    private final static long TIME_INTERVAL = 1000 * 60 * 5;

    private int mFid;
    private String mFName;
    private int mMsgTag;
    private int mPageCount;
    private long mLastShowDate; //最后显示出来的时间
    public KJEmojiFragment emojiFragment = new KJEmojiFragment();
    //存放正在发送的消息，key 为生成的一个临时messageID(msgTag)，value为Message实体
    //当消息发送成功后，从mSendingMsgs删除对应的Message实体
    private SparseArray<MessageDetail> mSendingMsgs = new SparseArray<MessageDetail>();

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mErrorLayout != null) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mFid = args.getInt(BUNDLE_KEY_FID);
            mFName = args.getString(BUNDLE_KEY_FNAME);
            mCatalog = CommentList.CATALOG_MESSAGE;
        }
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, filter);

        ((BaseActivity) getActivity()).setActionBarTitle(mFName);

        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getActivity().getWindow().setSoftInputMode(mode);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.emoji_container, emojiFragment).commit();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        if (emojiFragment.isShowEmojiKeyBoard()) {
            emojiFragment.hideAllKeyBoard();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    @Override
    protected MessageDetailAdapter getListAdapter() {
        MessageDetailAdapter adapter = new MessageDetailAdapter();
        adapter.setOnRetrySendMessageListener(this);
        return adapter;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mFid;
    }

    @Override
    protected MessageDetailList parseList(InputStream is) throws Exception {
        MessageDetailList list = XmlUtils.toBean(MessageDetailList.class, is);
        handleShowDate(list.getList());
        mPageCount = (int) Math.ceil((float) list.getMessageCount() / getPageSize());
        return list;
    }

    @Override
    protected MessageDetailList readList(Serializable seri) {
        MessageDetailList list = ((MessageDetailList) seri);
        handleShowDate(list.getList());
        return list;
    }

    /**
     * 处理时间显示，设置哪些需要显示时间，哪些不需要显示时间
     * @param list
     */
    private void handleShowDate(List<MessageDetail> list) {
        MessageDetail msg = null;
        long lastGroupTime = 0l;
        //因为获得的列表是按时间降序的，所以需要倒着遍历
        for (int i = list.size() - 1; i >= 0; i--) {
            msg = list.get(i);
            Date date = net.oschina.app.util.StringUtils.toDate(msg.getPubDate());
            if (date != null && isNeedShowDate(date.getTime(), lastGroupTime)) {
                lastGroupTime = date.getTime();
                msg.setShowDate(true);
            }
        }
        //只设置最新的时间
        if (lastGroupTime > mLastShowDate) {
            mLastShowDate = lastGroupTime;
        }
    }

    private boolean isNeedShowDate(long currentTime,long lastTime){
        return currentTime - lastTime > TIME_INTERVAL;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        if(!AppContext.getNightModeSwitch()) {
            mListView.setBackgroundColor(Color.parseColor("#ebebeb"));
        }
        mListView.setOnItemLongClickListener(this);
        //移除父类设置的OnScrollListener，这里不需要下拉加载
        mListView.setOnScrollListener(null);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getInstance().isLogin()) {
                    requestData(false);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
            }
        });
    }

    @Override
    protected void requestData(boolean refresh) {
        mErrorLayout.setErrorMessage("");
        if (AppContext.getInstance().isLogin()) {
            super.requestData(refresh);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getChatMessageList(mFid, mCurrentPage, mHandler);
    }

    @Override
    protected boolean isReadCacheData(boolean refresh) {
        if (!TDevice.hasInternet()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(List<MessageDetail> data) {
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        if (data == null) {
            data = new ArrayList<MessageDetail>(1);
        }
        if (mAdapter != null) {
            if (mCurrentPage == 0)
                mAdapter.clear();
            mAdapter.addData(data);
            if (data.size() == 0 && mState == STATE_REFRESH) {
                mErrorLayout.setErrorType(EmptyLayout.NODATA);
            } else if (data.size() < getPageSize()) {
                mAdapter.setState(ListBaseAdapter.STATE_OTHER);
            } else {
                mAdapter.setState(ListBaseAdapter.STATE_LOAD_MORE);
            }
            mAdapter.notifyDataSetChanged();
            //只有第一次加载，才需要滚动到底部
            if (mCurrentPage == 0)
                mListView.setSelection(mListView.getBottom());
            else if (data.size() > 1) {
                mListView.setSelection(data.size() - 1);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {}

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        final MessageDetail message = mAdapter.getItem(position);
        DialogHelp.getSelectDialog(getActivity(), getResources().getStringArray(R.array.message_list_options), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        TDevice.copyTextToBoard(HTMLUtil.delHTMLTag(message
                                .getContent()));
                        break;
                    case 1:
                        handleDeleteMessage(message);
                        break;
                    default:
                        break;
                }
            }
        }).show();
        return true;
    }

    // 下拉加载数据
    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH) {
            return;
        }
        if(mCurrentPage==mPageCount-1){
            AppContext.showToastShort("已加载全部数据！");
            setSwipeRefreshLoadedState();
            return;
        }
        // 设置顶部正在刷新
        mListView.setSelection(0);
        setSwipeRefreshLoadingState();
        mState = STATE_REFRESH;
        mCurrentPage++;
        requestData(true);
    }

    public void showFriendUserCenter(){
        UIHelper.showUserCenter(getActivity(), mFid, mFName);
    }

    @Override
    public void onResume() {
        super.onResume();
        emojiFragment.hideFlagButton();
    }

    private void handleDeleteMessage(final MessageDetail message) {
        DialogHelp.getConfirmDialog(getActivity(), "是否删除该私信?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showWaitDialog(R.string.progress_submit);
                OSChinaApi.deleteComment(mFid,
                        CommentList.CATALOG_MESSAGE, message.getId(),
                        message.getAuthorId(),
                        new DeleteMessageOperationHandler(message));
            }
        }).show();
    }

    class DeleteMessageOperationHandler extends OperationResponseHandler {

        public DeleteMessageOperationHandler(Object... args) {
            super(args);
        }

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
                throws Exception {
            Result res = XmlUtils.toBean(ResultBean.class, is).getResult();
            if (res.OK()) {
                Comment msg = (Comment) args[0];
                mAdapter.removeItem(msg);
                mAdapter.notifyDataSetChanged();
                hideWaitDialog();
                AppContext.showToastShort(R.string.tip_delete_success);
            } else {
                AppContext.showToastShort(res.getErrorMessage());
                hideWaitDialog();
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            AppContext.showToastShort(R.string.tip_delete_faile);
            hideWaitDialog();
        }
    }

    @Override
    public void onClickSendButton(Editable str) {
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        if (StringUtils.isEmpty(str)) {
            AppContext.showToastShort(R.string.tip_content_empty);
            return;
        }
        MessageDetail message = new MessageDetail();
        User user = AppContext.getInstance().getLoginUser();
        int msgTag = mMsgTag++;
        message.setId(msgTag);
        message.setPortrait(user.getPortrait());
        message.setAuthor(user.getName());
        message.setAuthorId(user.getId());
        message.setContent(str.toString());
        sendMessage(message);
    }

    /**
     * 发送消息
     * @param msg
     */
    private void sendMessage(MessageDetail msg){
        msg.setStatus(MessageDetail.MessageStatus.SENDING);
        Date date = new Date();
        msg.setPubDate(net.oschina.app.util.StringUtils.getDateString(date));
        //如果此次发表的时间距离上次的时间达到了 TIME_INTERVAL 的间隔要求，则显示时间
        if(isNeedShowDate(date.getTime(),mLastShowDate)) {
            msg.setShowDate(true);
            mLastShowDate = date.getTime();
        }

        //如果待发送列表没有此条消息，说明是新消息，不是发送失败再次发送的，不需要再次添加
        if(mSendingMsgs.indexOfKey(msg.getId())<0) {
            mSendingMsgs.put(msg.getId(), msg);
            mAdapter.addItem(0, msg);
            mListView.setSelection(mListView.getBottom());
        }else{
            mAdapter.notifyDataSetChanged();
        }
        OSChinaApi.publicMessage(msg.getAuthorId(), mFid, msg.getContent(), new SendMessageResponseHandler(msg.getId()));
    }

    @Override
    public void onClickFlagButton() {}

    @Override
    public void onRetrySendMessage(int msgId) {
        MessageDetail message = mSendingMsgs.get(msgId);
        if (message != null) {
            sendMessage(message);
        }
    }

    class SendMessageResponseHandler extends AsyncHttpResponseHandler{

        private int msgTag;

        public SendMessageResponseHandler(int msgTag) {
            this.msgTag = msgTag;
        }

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            try {
                ResultBean resb = XmlUtils.toBean(ResultBean.class,
                        new ByteArrayInputStream(arg2));
                Result res = resb.getResult();
                if (res.OK()) {
                    //从mSendingMsgs获取发送时放入的MessageDetail实体
                    MessageDetail message = mSendingMsgs.get(this.msgTag);
                    MessageDetail serverMsg = resb.getMessage();
                    //把id设置为服务器返回的id
                    message.setId(serverMsg.getId());
                    message.setStatus(MessageDetail.MessageStatus.NORMAL);
                    //从待发送列表移除
                    mSendingMsgs.remove(this.msgTag);
                    mAdapter.notifyDataSetChanged();
                } else {
                    error();
                    AppContext.showToastShort(res.getErrorMessage());
                }
                emojiFragment.clean();
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(arg0, arg1, arg2, e);
            }
        }


        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            error();
        }

        private void error(){
            mSendingMsgs.get(this.msgTag).setStatus(MessageDetail.MessageStatus.ERROR);
            mAdapter.notifyDataSetChanged();
        }
    }
}
