package net.oschina.app.team.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaTeamApi;
import net.oschina.app.base.BaseActivity;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.Result;
import net.oschina.app.bean.ResultBean;
import net.oschina.app.emoji.OnSendClickListener;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.bean.TeamIssue;
import net.oschina.app.team.bean.TeamIssueCatalog;
import net.oschina.app.team.bean.TeamIssueDetail;
import net.oschina.app.team.bean.TeamRepliesList;
import net.oschina.app.team.bean.TeamReply;
import net.oschina.app.team.bean.TeamReplyBean;
import net.oschina.app.ui.DetailActivity;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.DialogHelp;
import net.oschina.app.util.HTMLUtil;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.TypefaceUtils;
import net.oschina.app.util.ViewUtils;
import net.oschina.app.util.XmlUtils;
import net.oschina.app.widget.AvatarView;

import cz.msebera.android.httpclient.Header;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * TeamIssueDetailFragmentNew.java
 *
 * @author 火蚁(http://my.oschina.net/u/253900)
 * @data 2015-2-12 下午3:44:47
 */
public class TeamIssueDetailFragment extends BaseFragment implements
        OnSendClickListener {

    private Team mTeam;

    private TeamIssue mTeamIssue;

    private TeamIssueCatalog mCatalog;

    @InjectView(R.id.content)
    View mContent;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    @InjectView(R.id.ll_issue_project)
    View mProjectView;
    @InjectView(R.id.tv_issue_project)
    TextView mTvProject;
    @InjectView(R.id.tv_issue_state_title)
    TextView mTvStateTitle;
    @InjectView(R.id.tv_issue_title)
    TextView mTvTitle;
    @InjectView(R.id.tv_issue_touser)
    TextView mTvToUser;
    @InjectView(R.id.tv_issue_cooperate_user)
    TextView mTvCooperateUser;
    @InjectView(R.id.tv_issue_die_time)
    TextView mTvDieTime;
    @InjectView(R.id.tv_issue_state)
    TextView mTvState;
    @InjectView(R.id.ll_issue_labels)
    LinearLayout mLLlabels;
    @InjectView(R.id.tv_issue_attachments)
    TextView mTvAttachments;
    @InjectView(R.id.tv_issue_relations)
    TextView mTvRelations;
    @InjectView(R.id.tv_issue_child)
    TextView mTvIssueChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_team_issue_detail,
                container, false);
        Intent args = getActivity().getIntent();
        if (args != null) {
            mTeam = (Team) args.getSerializableExtra("team");
            mTeamIssue = (TeamIssue) args.getSerializableExtra("issue");
            mCatalog = (TeamIssueCatalog) args
                    .getSerializableExtra("issue_catalog");
        }
        if (mCatalog != null) {

            ((BaseActivity) getActivity()).setActionBarTitle(mCatalog
                    .getTitle());
        }
        initView(root);
        initData();
        return root;
    }

    @Override
    public void initView(View view) {
        ButterKnife.inject(this, view);

        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_issue_fa_touser));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_issue_fa_cooperate_user));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_issue_fa_die_time));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_issue_fa_state));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_issue_fa_labels));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_issue_fa_child));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_issue_fa_relations));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_issue_fa_attachments));

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestDetail();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        requestDetail();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailActivity) getActivity()).emojiFragment.hideFlagButton();
    }

    private final AsyncHttpResponseHandler mDetailHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            // TODO Auto-generated method stub

            TeamIssueDetail teamIssueDetail = XmlUtils.toBean(
                    TeamIssueDetail.class, arg2);
            if (teamIssueDetail != null) {
                mContent.setVisibility(View.VISIBLE);
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                fillUI(teamIssueDetail.getTeamIssue());
                requestIssueComments();
            } else {
                mContent.setVisibility(View.INVISIBLE);
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                mErrorLayout.setErrorMessage("该任务可能已被删除");
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            // TODO Auto-generated method stub
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }

        @Override
        public void onStart() {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        }

        ;

    };

    private void requestDetail() {
        OSChinaTeamApi.getTeamIssueDetail(mTeam.getId(), mTeamIssue.getId(),
                mDetailHandler);
    }

    private void fillUI(TeamIssue teamIssue) {
        if (teamIssue == null)
            return;
        this.mTeamIssue = teamIssue;
        if (mTeamIssue.getProject() != null
                && mTeamIssue.getProject().getGit() != null) {
            mProjectView.setVisibility(View.VISIBLE);
            String pushState = mTeamIssue.getGitpush() != TeamIssue.TEAM_ISSUE_GITPUSHED ? " -未同步"
                    : "";
            mTvProject.setText(mTeamIssue.getProject().getGit().getName()
                    + pushState);
        } else {
            mProjectView.setVisibility(View.GONE);
        }

        mTvTitle.setText(mTeamIssue.getTitle());

        setIssueState();

        if (mTeamIssue.getToUser() != null
                && !TextUtils.isEmpty(mTeamIssue.getToUser().getName())) {
            mTvToUser.setText(mTeamIssue.getToUser().getName());
        } else {
            mTvToUser.setText("未指派");
        }

        if (!TextUtils.isEmpty(mTeamIssue.getDeadlineTime())) {
            mTvDieTime.setText(mTeamIssue.getDeadlineTimeText());
        } else {
            mTvDieTime.setText("未指定截止日期");
        }

        if (mTeamIssue.getAttachments().getTotalCount() != 0) {
            mTvAttachments.setText(mTeamIssue.getAttachments().getTotalCount()
                    + "");
        } else {
            mTvAttachments.setText("暂无附件");
        }

        if (mTeamIssue.getRelations().getTotalCount() != 0) {
            mTvRelations
                    .setText(mTeamIssue.getRelations().getTotalCount() + "");
        } else {
            mTvRelations.setText("暂无关联");
        }

        if (mTeamIssue.getChildIssues().getTotalCount() != 0) {
            String childIssueState = mTeamIssue.getChildIssues()
                    .getTotalCount()
                    + "个子任务，"
                    + mTeamIssue.getChildIssues().getClosedCount() + "个已完成";
            mTvIssueChild.setText(childIssueState);
        } else {
            mTvIssueChild.setText("暂无子任务");
        }

        setChildIssues(mTeamIssue.getChildIssues().getChildIssues());

        setLabels(mTeamIssue);
        setIssueCollaborator();
    }

    private void setIssueCollaborator() {
        StringBuffer cooperateUserStr = new StringBuffer();
        if (mTeamIssue.getCollaborators().size() > 0) {
            for (int i = 0; i < mTeamIssue.getCollaborators().size(); i++) {
                if (i == mTeamIssue.getCollaborators().size() - 1) {
                    cooperateUserStr.append(mTeamIssue.getCollaborators()
                            .get(i).getName());
                } else {
                    cooperateUserStr.append(mTeamIssue.getCollaborators()
                            .get(i).getName()
                            + "，");
                }
            }
            mTvCooperateUser.setText(cooperateUserStr.toString());
        } else {
            mTvCooperateUser.setText("暂无协作者");
        }
    }

    private void setIssueState() {
        TypefaceUtils.setTypeface(mTvStateTitle,
                mTeamIssue.getIssueStateFaTextId());

        if (mTeamIssue.getState().equals("closed")
                || mTeamIssue.getState().equals("accepted")) {
            ViewUtils
                    .setTextViewLineFlag(mTvTitle, Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            ViewUtils.setTextViewLineFlag(mTvTitle, 0);
        }

        mTvState.setText(mTeamIssue.getIssueStateText());
    }

    private void setLabels(TeamIssue issue) {
        if (issue.getLabels() == null || issue.getLabels().isEmpty()) {
            mLLlabels.setVisibility(View.GONE);
        } else {
            for (TeamIssue.Label label : issue.getLabels()) {
                TextView text = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.team_issue_lable, null, false);
                text.setText(label.getName());
                String colorStr = label.getColor();
                if (colorStr.equalsIgnoreCase("#ffffff")) {
                    colorStr = "#000000";
                }
                int color = Color.parseColor(colorStr);
                LayoutParams params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 0, 4, 0);

                GradientDrawable d = (GradientDrawable) text.getBackground();
                d.setStroke(1, color);
                text.setTextColor(color);

                mLLlabels.addView(text, params);
            }
        }
    }

    private final AsyncHttpResponseHandler mChangeIssueHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            // TODO Auto-generated method stub
            Result result = XmlUtils.toBean(ResultBean.class, arg2).getResult();
            if (result.OK()) {
                setIssueState();
            }
            AppContext.showToast(result.getErrorMessage());
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            // TODO Auto-generated method stub
            AppContext.showToast("更新失败");
        }

        @Override
        public void onStart() {
            showWaitDialog("正在修改...");
        }

        ;

        @Override
        public void onFinish() {
            hideWaitDialog();
        }

        ;
    };

    @Override
    // @OnClick({ R.id.ll_issue_state_title, R.id.ll_issue_touser,
    // R.id.ll_issue_cooperate_user, R.id.ll_issue_die_time,
    // R.id.ll_issue_state, R.id.ll_issue_child })
    @OnClick({R.id.ll_issue_state_title, R.id.ll_issue_state,
            R.id.ll_issue_child})
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ll_issue_state_title:
            case R.id.ll_issue_state:
                changeIssueState();
                break;
            case R.id.ll_issue_touser:
                // 暂时屏蔽修改任务指派
                // Bundle bundle = new Bundle();
                // bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_TEAM, mTeam);
                // bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT,
                // mTeamIssue.getProject());
                // UIHelper.showSimpleBack(getActivity(),
                // SimpleBackPage.TEAM_PROJECT_MEMBER_SELECT, bundle);
                break;
            case R.id.ll_issue_cooperate_user:

                break;
            case R.id.ll_issue_die_time:

                break;
            case R.id.ll_issue_child:
                if (mLLChildIssues.getVisibility() == View.GONE) {
                    mLLChildIssues.setVisibility(View.VISIBLE);
                } else {
                    mLLChildIssues.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }
    }

    private AlertDialog dialog;

    private void changeIssueState() {
        if (!mTeamIssue.getAuthority().isUpdateState()) {
            AppContext.showToast("抱歉，无更改权限");
            return;
        }

        final String[] items = getResources().getStringArray(
                R.array.team_issue_state);
        final String[] itemsEn = getResources().getStringArray(
                R.array.team_issue_state_en);
        int index = 0;
        for (int i = 0; i < itemsEn.length; i++) {
            if (itemsEn[i].equals(mTeamIssue.getState())) {
                index = i;
            }
        }
        final int selIndex = index;
        dialog = DialogHelp.getSingleChoiceDialog(getActivity(), "更改任务状态", items, selIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == selIndex) {
                    dialog.dismiss();
                    return;
                }
                mTeamIssue.setState(itemsEn[i].toString());
                OSChinaTeamApi.changeIssueState(mTeam.getId(), mTeamIssue,
                        "state", mChangeIssueHandler);
                dialog.dismiss();
            }
        }).show();
    }

    @InjectView(R.id.ll_issue_childs)
    LinearLayout mLLChildIssues;

    private void setChildIssues(List<TeamIssue> list) {
        if (list == null || list.isEmpty())
            return;

        for (TeamIssue teamIssue : list) {
            addChildIssue(teamIssue);
        }
    }

    private void addChildIssue(final TeamIssue teamIssue) {
        if (teamIssue == null)
            return;
        final View cell = LayoutInflater.from(getActivity()).inflate(
                R.layout.list_cell_team_child_issue, null, false);
        AvatarView avatarView = (AvatarView) cell.findViewById(R.id.iv_avatar);
        avatarView.setAvatarUrl(teamIssue.getToUser().getPortrait());
        final TextView content = (TextView) cell.findViewById(R.id.tv_content);
        content.setText(teamIssue.getTitle());
        setChildIssueState(cell, teamIssue);
        cell.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateChildIssueState(cell, teamIssue);
            }
        });
        mLLChildIssues.addView(cell);
    }

    private void setChildIssueState(View cell, TeamIssue childIssue) {
        TextView content = (TextView) cell.findViewById(R.id.tv_content);
        TextView state = (TextView) cell.findViewById(R.id.tv_state);

        if (childIssue.getState().equalsIgnoreCase("closed")) {
            ViewUtils.setTextViewLineFlag(content, Paint.STRIKE_THRU_TEXT_FLAG);
            TypefaceUtils.setTypeface(state, R.string.fa_check_circle_o);
        } else {
            ViewUtils.setTextViewLineFlag(content, 0);
            TypefaceUtils.setTypeface(state, R.string.fa_circle_o);
        }
    }

    private void updateChildIssueState(final View cell,
                                       final TeamIssue childIssue) {
        switchChildIssueState(childIssue);
        OSChinaTeamApi.updateChildIssue(mTeam.getId(), "state", childIssue,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        Result result = XmlUtils.toBean(ResultBean.class, arg2)
                                .getResult();
                        if (result.OK()) {
                            setChildIssueState(cell, childIssue);
                        } else {
                            switchChildIssueState(childIssue);
                        }
                        AppContext.showToast(result.getErrorMessage());
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        AppContext.showToast("更新失败");
                        switchChildIssueState(childIssue);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        showWaitDialog("正在更新状态...");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideWaitDialog();
                    }
                });
    }

    private void switchChildIssueState(TeamIssue childIssue) {
        if (childIssue.getState().equals("opened")) {
            childIssue.setState("closed");
        } else {
            childIssue.setState("opened");
        }
    }

    @InjectView(R.id.ll_issue_comments)
    LinearLayout mLLComments;

    // 请求任务的评论
    private void requestIssueComments() {
        OSChinaTeamApi.getTeamReplyList(mTeam.getId(), mTeamIssue.getId(),
                TeamReply.REPLY_TYPE_ISSUE, 0, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        TeamRepliesList list = XmlUtils.toBean(
                                TeamRepliesList.class, arg2);
                        if (list != null && !list.getList().isEmpty()) {
                            fillComments(list.getList());
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private void fillComments(List<TeamReply> list) {
        if (list == null || list.isEmpty())
            return;
        for (TeamReply teamReply : list) {
            addComment(teamReply);
        }
    }

    private void addComment(final TeamReply reply) {
        View cell = LayoutInflater.from(getActivity()).inflate(
                R.layout.list_cell_team_reply, null, false);
        AvatarView avatarView = (AvatarView) cell.findViewById(R.id.iv_avatar);
        avatarView.setAvatarUrl(reply.getAuthor().getPortrait());
        TextView name = (TextView) cell.findViewById(R.id.tv_name);
        name.setText(reply.getAuthor().getName());
        TextView content = (TextView) cell.findViewById(R.id.tv_content);
        content.setText(HTMLUtil.delHTMLTag(reply.getContent()));
        TextView time = (TextView) cell.findViewById(R.id.tv_time);
        time.setText(StringUtils.friendly_time(reply.getCreateTime()));
        mLLComments.addView(cell);
    }

    @Override
    public void onClickSendButton(Editable str) {
        if (mTeamIssue == null) {
            return;
        }
        showWaitDialog("提交评论中...");
        OSChinaTeamApi.pubTeamIssueReply(mTeam.getId(), mTeamIssue.getId(),
                str.toString(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        TeamReply reply = XmlUtils.toBean(TeamReplyBean.class,
                                arg2).getTeamReply();
                        if (reply != null) {
                            addComment(reply);
                        } else {
                            AppContext.showToast("评论失败");
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        Result result = XmlUtils.toBean(ResultBean.class, arg2)
                                .getResult();
                        AppContext.showToast(result.getErrorMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideWaitDialog();
                    }
                });
    }

    @Override
    public void onClickFlagButton() {
    }
}
