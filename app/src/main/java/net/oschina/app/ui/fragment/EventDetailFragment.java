package net.oschina.app.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseListFragment;
import net.oschina.app.base.CommonDetailFragment;
import net.oschina.app.bean.CommentList;
import net.oschina.app.bean.Event;
import net.oschina.app.bean.EventApplyData;
import net.oschina.app.bean.FavoriteList;
import net.oschina.app.bean.Post;
import net.oschina.app.bean.PostDetail;
import net.oschina.app.bean.Result;
import net.oschina.app.bean.ResultBean;
import net.oschina.app.bean.SimpleBackPage;
import net.oschina.app.ui.EventApplyDialog;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.ThemeSwitchUtils;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.URLsUtils;
import net.oschina.app.util.XmlUtils;

import cz.msebera.android.httpclient.Header;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import butterknife.InjectView;

/**
 * Created by 火蚁 on 15/5/28.
 */
public class EventDetailFragment extends CommonDetailFragment<Post> {

    @InjectView(R.id.tv_event_title)
    TextView mTvTitle;

    @InjectView(R.id.tv_event_start_time)
    TextView mTvStartTime;

    @InjectView(R.id.tv_event_end_time)
    TextView mTvEndTime;

    @InjectView(R.id.tv_event_spot)
    TextView mTvSpot;

    @InjectView(R.id.rl_event_location)
    View mLocation;

    @InjectView(R.id.bt_event_attend)
    Button mBtAttend;// 出席人员

    @InjectView(R.id.bt_event_apply)
    Button mBtEventApply;// 活动报名

    @InjectView(R.id.tv_event_tip)
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
