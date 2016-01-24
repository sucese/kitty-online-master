package com.guoxiaoxing.kitty.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.bean.CommentList;
import com.guoxiaoxing.kitty.bean.Event;
import com.guoxiaoxing.kitty.bean.EventApplyData;
import com.guoxiaoxing.kitty.bean.FavoriteList;
import com.guoxiaoxing.kitty.bean.Post;
import com.guoxiaoxing.kitty.bean.PostDetail;
import com.guoxiaoxing.kitty.bean.Result;
import com.guoxiaoxing.kitty.bean.ResultBean;
import com.guoxiaoxing.kitty.bean.SimpleBackPage;
import com.guoxiaoxing.kitty.ui.dialog.EventApplyDialog;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.ui.base.CommonDetailFragment;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.ThemeSwitchUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.URLsUtils;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * Created by 火蚁 on 15/5/28.
 */
public class EventDetailFragment extends CommonDetailFragment<Post> {

    @Bind(R.id.tv_event_title)
    TextView mTvTitle;

    @Bind(R.id.tv_event_start_time)
    TextView mTvStartTime;

    @Bind(R.id.tv_event_end_time)
    TextView mTvEndTime;

    @Bind(R.id.tv_event_spot)
    TextView mTvSpot;

    @Bind(R.id.rl_event_location)
    View mLocation;

    @Bind(R.id.bt_event_attend)
    Button mBtAttend;// 出席人员

    @Bind(R.id.bt_event_apply)
    Button mBtEventApply;// 活动报名

    @Bind(R.id.tv_event_tip)
    TextView mEventTip;

    private EventApplyDialog mEventApplyDialog;

    @Override
    public void initView(View view) {
        super.initView(view);
        mLocation.setOnClickListener(this);
        mBtAttend.setOnClickListener(this);
        mBtEventApply.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_event_detail;
    }

    @Override
    protected String getCacheKey() {
        return "post_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        OSChinaApi.getPostDetail(mId, mDetailHeandler);
    }

    @Override
    protected Post parseData(InputStream is) {
        return XmlUtils.toBean(PostDetail.class, is).getPost();
    }

    @Override
    protected String getWebViewBody(Post detail) {
        StringBuffer body = new StringBuffer();
        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        body.append(ThemeSwitchUtils.getWebViewBodyString());
        // 添加title
        body.append(String.format("<div class='title'>%s</div>", mDetail.getTitle()));
        // 添加作者和时间
        String time = StringUtils.friendly_time(mDetail.getPubDate());
        String author = String.format("<a class='author' href='http://my.oschina.net/u/%s'>%s</a>", mDetail.getAuthorId(), mDetail.getAuthor());
        body.append(String.format("<div class='authortime'>%s&nbsp;&nbsp;&nbsp;&nbsp;%s</div>", author, time));
        // 添加图片点击放大支持
        body.append(UIHelper.setHtmlCotentSupportImagePreview(mDetail.getBody()));
        // 封尾
        body.append("</div></body>");
        return body.toString();
    }

    @Override
    protected void executeOnLoadDataSuccess(Post detail) {
        super.executeOnLoadDataSuccess(detail);
        mTvTitle.setText(mDetail.getTitle());
        mTvStartTime.setText(String.format(
                getString(R.string.event_start_time), mDetail.getEvent()
                        .getStartTime()));
        mTvEndTime.setText(String.format(getString(R.string.event_end_time),
                mDetail.getEvent().getEndTime()));
        mTvSpot.setText(mDetail.getEvent().getCity() + " "
                + mDetail.getEvent().getSpot());

        // 站外活动
        if (mDetail.getEvent().getCategory() == 4) {
            mBtEventApply.setVisibility(View.VISIBLE);
            mBtAttend.setVisibility(View.GONE);
            mBtEventApply.setText("报名链接");
        } else {
            notifyEventStatus();
        }
    }

    // 显示活动 以及报名的状态
    private void notifyEventStatus() {
        int eventStatus = mDetail.getEvent().getStatus();
        int applyStatus = mDetail.getEvent().getApplyStatus();

        if (applyStatus == Event.APPLYSTATUS_ATTEND) {
            mBtAttend.setVisibility(View.VISIBLE);
        } else {
            mBtAttend.setVisibility(View.GONE);
        }

        if (eventStatus == Event.EVNET_STATUS_APPLYING) {
            mBtEventApply.setVisibility(View.VISIBLE);
            mBtEventApply.setEnabled(false);
            switch (applyStatus) {
                case Event.APPLYSTATUS_CHECKING:
                    mBtEventApply.setText("待确认");
                    break;
                case Event.APPLYSTATUS_CHECKED:
                    mBtEventApply.setText("已确认");
                    mBtEventApply.setVisibility(View.GONE);
                    mEventTip.setVisibility(View.VISIBLE);
                    break;
                case Event.APPLYSTATUS_ATTEND:
                    mBtEventApply.setText("已出席");
                    break;
                case Event.APPLYSTATUS_CANCLE:
                    mBtEventApply.setText("已取消");
                    mBtEventApply.setEnabled(true);
                    break;
                case Event.APPLYSTATUS_REJECT:
                    mBtEventApply.setText("已拒绝");
                    break;
                default:
                    mBtEventApply.setText("我要报名");
                    mBtEventApply.setEnabled(true);
                    break;
            }
        } else {
            mBtEventApply.setVisibility(View.GONE);
        }
    }

    @Override
    protected void showCommentView() {
        if (mDetail != null) {
            UIHelper.showComment(getActivity(), mId, CommentList.CATALOG_POST);
        }
    }

    @Override
    protected int getCommentType() {
        return CommentList.CATALOG_POST;
    }

    @Override
    protected int getFavoriteTargetType() {
        return FavoriteList.TYPE_POST;
    }

    @Override
    protected int getFavoriteState() {
        return mDetail.getFavorite();
    }

    @Override
    protected void updateFavoriteChanged(int newFavoritedState) {
        mDetail.setFavorite(newFavoritedState);
        saveCache(mDetail);
    }

    @Override
    protected int getCommentCount() {
        return mDetail.getAnswerCount();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_event_location:
                UIHelper.showEventLocation(getActivity(), mDetail.getEvent()
                        .getCity(), mDetail.getEvent().getSpot());
                break;
            case R.id.bt_event_attend:
                showEventApplies();
                break;
            case R.id.bt_event_apply:
                showEventApply();
                break;
            default:
                break;
        }
    }

    private void showEventApplies() {
        Bundle args = new Bundle();
        args.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, mDetail.getEvent()
                .getId());
        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.EVENT_APPLY, args);
    }

    private final AsyncHttpResponseHandler mApplyHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            Result rs = XmlUtils.toBean(ResultBean.class,
                    new ByteArrayInputStream(arg2)).getResult();
            if (rs.OK()) {
                AppContext.showToast("报名成功");
                mEventApplyDialog.dismiss();
                mDetail.getEvent().setApplyStatus(Event.APPLYSTATUS_CHECKING);
            } else {
                AppContext.showToast(rs.getErrorMessage());
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            AppContext.showToast("报名失败");
        }

        @Override
        public void onFinish() {
            hideWaitDialog();
        }
    };

    /**
     * 显示活动报名对话框
     */
    private void showEventApply() {

        if (mDetail.getEvent().getCategory() == 4) {
            UIHelper.openSysBrowser(getActivity(), mDetail.getEvent().getUrl());
            return;
        }

        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        if (mEventApplyDialog == null) {
            mEventApplyDialog = new EventApplyDialog(getActivity(), mDetail.getEvent());
            mEventApplyDialog.setCanceledOnTouchOutside(true);
            mEventApplyDialog.setCancelable(true);
            mEventApplyDialog.setTitle("活动报名");
            mEventApplyDialog.setCanceledOnTouchOutside(true);
            mEventApplyDialog.setNegativeButton(R.string.cancle, null);
            mEventApplyDialog.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface d, int which) {
                            EventApplyData data = null;
                            if ((data = mEventApplyDialog.getApplyData()) != null) {
                                data.setEvent(mId);
                                data.setUser(AppContext.getInstance()
                                        .getLoginUid());
                                showWaitDialog(R.string.progress_submit);
                                OSChinaApi.eventApply(data, mApplyHandler);
                            }
                        }
                    });
        }

        mEventApplyDialog.show();
    }

    @Override
    protected String getShareTitle() {
        return mDetail.getTitle();
    }

    @Override
    protected String getShareContent() {
        return StringUtils.getSubString(0, 55,
                getFilterHtmlBody(mDetail.getBody()));
    }

    @Override
    protected String getShareUrl() {
        return  String.format(URLsUtils.URL_MOBILE + "question/%s_%s", mDetail.getAuthorId(), mId);
    }
}
