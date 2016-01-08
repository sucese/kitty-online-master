package net.oschina.app.team.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.api.remote.OSChinaTeamApi;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.emoji.OnSendClickListener;
import net.oschina.app.team.adapter.TeamDiaryDetailAdapter;
import net.oschina.app.team.bean.TeamDiary;
import net.oschina.app.team.bean.TeamDiaryDetailBean;
import net.oschina.app.team.bean.TeamRepliesList;
import net.oschina.app.team.bean.TeamReply;
import net.oschina.app.team.viewpagefragment.TeamDiaryFragment;
import net.oschina.app.ui.DetailActivity;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.ThemeSwitchUtils;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;
import net.oschina.app.widget.AvatarView;

import cz.msebera.android.httpclient.Header;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周报详情<br>
 * 逻辑介绍：用Listview来显示评论内容，在ListView的HeadView中添加本周报的详细内容与周报列表的item。
 * 周报的详细内容通过动态添加addView的方式
 * 
 * @author kymjs (https://github.com/kymjs)
 */
public class TeamDiaryDetailFragment extends BaseFragment implements
        OnSendClickListener {

    @InjectView(R.id.listview)
    ListView mList;
    @InjectView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwiperefreshlayout;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    private TeamDiary diaryData;
    private int teamid;
    private Activity aty;
    private TeamDiaryDetailAdapter adapter;

    private LinearLayout footerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = View.inflate(getActivity(),
                R.layout.fragment_pull_refresh_listview, null);
        aty = getActivity();
        ButterKnife.inject(this, rootView);
        initData();
        initView(rootView);
        return rootView;
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = aty.getIntent().getBundleExtra("diary");
        if (bundle != null) {
            teamid = bundle.getInt(TeamDiaryFragment.TEAMID_KEY);
            diaryData = (TeamDiary) bundle
                    .getSerializable(TeamDiaryFragment.DIARYDETAIL_KEY);
        } else {
            diaryData = new TeamDiary();
            Log.e("debug", getClass().getSimpleName() + "diaryData初始化异常");
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mList.setDivider(null);
        mList.setSelector(android.R.color.transparent);
        mList.addHeaderView(initHeaderView());
        mList.addFooterView(initFooterView());

        mSwiperefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mState == STATE_REFRESH) {
                    return;
                } else {
                    // 设置顶部正在刷新
                    setSwipeRefreshLoadingState(mSwiperefreshlayout);
                    /* !!! 设置耗时操作 !!! */
                    initCommitLayout();
                }
            }
        });
        mSwiperefreshlayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        initListData();
        initCommitLayout();
    }

    /**
     * 设置顶部正在加载的状态
     */
    private void setSwipeRefreshLoadingState(
            SwipeRefreshLayout mSwipeRefreshLayout) {
        mState = STATE_REFRESH;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    private void setSwipeRefreshLoadedState(
            SwipeRefreshLayout mSwipeRefreshLayout) {
        mState = STATE_NOMORE;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    /**
     * 初始化头部周报Title
     * 
     * @return
     */
    private View initHeaderView() {
        View headerView = inflateView(R.layout.item_team_diarydetail_head);
        AvatarView headImg = (AvatarView) headerView
                .findViewById(R.id.event_listitem_userface);
        TextView userName = (TextView) headerView
                .findViewById(R.id.event_listitem_username);
        WebView content = (WebView) headerView
                .findViewById(R.id.team_diary_webview);
        TextView time = (TextView) headerView
                .findViewById(R.id.event_listitem_date);
        headImg.setAvatarUrl(diaryData.getAuthor().getPortrait());
        userName.setText(diaryData.getAuthor().getName());

        UIHelper.initWebView(content);
        fillWebViewBody(content);
        time.setText(StringUtils.friendly_time(diaryData.getCreateTime()));
        return headerView;
    }

    /**
     * 填充webview内容
     */
    private void fillWebViewBody(WebView mContent) {

        StringBuffer body = new StringBuffer();
        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        body.append(ThemeSwitchUtils.getWebViewBodyString());
        // 添加图片点击放大支持
        body.append(UIHelper.setHtmlCotentSupportImagePreview(diaryData.getTitle()));
        // 封尾
        body.append("</div></body>");
        UIHelper.addWebImageShow(getActivity(), mContent);
        mContent.loadDataWithBaseURL(null, body.toString(), "text/html",
                "utf-8", null);
    }

    private View initFooterView() {
        footerView = new LinearLayout(aty);
        footerView.setPadding(20, 0, 20, 20);
        footerView.setOrientation(LinearLayout.VERTICAL);
        return footerView;
    }

    private void initListData() {
        OSChinaApi.getDiaryDetail(teamid, diaryData.getId(),
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        TeamDiaryDetailBean data = XmlUtils.toBean(
                                TeamDiaryDetailBean.class, arg2);
                        adapter = new TeamDiaryDetailAdapter(aty, data
                                .getTeamDiary().getTeamDiaryDetail());
                        mList.setAdapter(adapter);
                        mErrorLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                            Throwable arg3) {
                        mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                        mErrorLayout.setErrorMessage("网络不好，请稍后重试");
                    }

                });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailActivity) getActivity()).emojiFragment.hideFlagButton();
    }

    private void initCommitLayout() {
        OSChinaApi.getDiaryComment(teamid, diaryData.getId(),
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1,
                            final byte[] arg2) {
                        List<TeamReply> datas = XmlUtils.toBean(
                                TeamRepliesList.class, arg2).getList();
                        footerView.removeAllViews();
                        for (final TeamReply data : datas) {
                            View layout = View.inflate(aty,
                                    R.layout.list_cell_comment, null);
                            AvatarView head = (AvatarView) layout
                                    .findViewById(R.id.iv_avatar);
                            head.setAvatarUrl(data.getAuthor().getPortrait());
                            final TextView name = (TextView) layout
                                    .findViewById(R.id.tv_name);
                            name.setText(data.getAuthor().getName());
                            TextView time = (TextView) layout
                                    .findViewById(R.id.tv_time);
                            time.setText(StringUtils.friendly_time(data
                                    .getCreateTime()));
                            TextView content = (TextView) layout
                                    .findViewById(R.id.tv_content);
                            content.setText(stripTags(data.getContent()));
                            TextView from = (TextView) layout
                                    .findViewById(R.id.tv_from);
                            if (StringUtils.isEmpty(data.getAppName())) {
                                from.setVisibility(View.GONE);
                            } else {
                                from.setVisibility(View.VISIBLE);
                                from.setText(data.getAppName());
                            }

                            footerView.addView(layout);
                        }
                        footerView.invalidate();
                        if (adapter != null) {
                            adapter.notifyDataSetInvalidated();
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                            Throwable arg3) {}

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        setSwipeRefreshLoadedState(mSwiperefreshlayout);
                    }
                });
    }

    /**
     * 移除字符串中的Html标签
     * 
     * @author kymjs (https://github.com/kymjs)
     * @param pHTMLString
     * @return
     */
    public static Spanned stripTags(final String pHTMLString) {
        String str = pHTMLString.replaceAll("<\\s*>", "");
        str = str.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "").trim();
        return Html.fromHtml(str);
    }

    @Override
    public void onClickSendButton(Editable str) {
        OSChinaTeamApi.pubTeamTweetReply(teamid, 118, diaryData.getId(),
                str.toString(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {}

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                            Throwable arg3) {}
                });
    }

    @Override
    public void onClickFlagButton() {}
}
