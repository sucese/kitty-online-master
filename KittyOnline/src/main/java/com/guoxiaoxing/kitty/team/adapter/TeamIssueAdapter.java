package com.guoxiaoxing.kitty.team.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.team.bean.TeamIssue;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.TypefaceUtils;
import com.guoxiaoxing.kitty.util.ViewUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 任务列表适配器
 *
 * @author guoxiaoxing
 */
public class TeamIssueAdapter extends ListBaseAdapter<TeamIssue> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        int type = getItemViewType(position);
        if (convertView == null || convertView.getTag() == null) {

            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_team_issue, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        TeamIssue item = mDatas.get(position);

        vh.title.setText(item.getTitle());

        String date = StringUtils.friendly_time2(item.getCreateTime());
        String preDate = "";
        if (position > 0) {
            preDate = StringUtils.friendly_time2(mDatas.get(position - 1)
                    .getCreateTime());
        }
        if (preDate.equals(date)) {
            vh.title_line.setVisibility(View.GONE);
        } else {
            vh.title_line.setText(date);
            vh.title_line.setVisibility(View.VISIBLE);
        }

        setIssueState(vh, item);

        setIssueSource(vh, item);

        vh.author.setText(item.getAuthor().getName());
        if (item.getToUser() == null
                || TextUtils.isEmpty(item.getToUser().getName())) {
            vh.to.setText("未指派");
            vh.touser.setVisibility(View.GONE);
        } else {
            vh.to.setText("指派给");
            vh.touser.setVisibility(View.VISIBLE);
            vh.touser.setText(item.getToUser().getName());
        }

        vh.time.setText(StringUtils.friendly_time(item.getCreateTime()));
        vh.comment.setText(item.getReplyCount() + "");

        if (item.getProject() != null && item.getProject().getGit() != null) {
            vh.project.setVisibility(View.VISIBLE);
            String gitState = item.getGitpush() == TeamIssue.TEAM_ISSUE_GITPUSHED ? ""
                    : " -未同步";
            setText(vh.project, item.getProject().getGit().getName() + gitState);
        } else {
            vh.project.setVisibility(View.GONE);
        }

        String deadlineTime = item.getDeadlineTime();
        if (!StringUtils.isEmpty(deadlineTime)) {
            vh.accept_time.setVisibility(View.VISIBLE);
            setText(vh.accept_time, getDeadlineTime(item), true);
        } else {
            vh.accept_time.setVisibility(View.GONE);
        }

        if (item.getAttachments().getTotalCount() != 0) {
            vh.attachments.setVisibility(View.VISIBLE);
            vh.attachments.setText("附件" + item.getAttachments().getTotalCount()
                    + "");
        } else {
            vh.attachments.setVisibility(View.GONE);
        }

        if (item.getChildIssues() != null
                && item.getChildIssues().getTotalCount() != 0) {
            vh.childissues.setVisibility(View.VISIBLE);
            setText(vh.childissues, "子任务("
                    + item.getChildIssues().getClosedCount() + "/"
                    + item.getChildIssues().getTotalCount() + ")");
        } else {
            vh.childissues.setVisibility(View.GONE);
        }

        if (item.getRelations().getTotalCount() != 0) {
            vh.relations.setVisibility(View.VISIBLE);
            vh.relations.setText("关联" + item.getRelations().getTotalCount()
                    + "");
        } else {
            vh.relations.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void setIssueState(ViewHolder vh, TeamIssue teamIssue) {
        String state = teamIssue.getState();
        if (TextUtils.isEmpty(state))
            return;
        TypefaceUtils.setTypeface(vh.state, teamIssue.getIssueStateFaTextId());

        if (teamIssue.getState().equals("closed")
                || teamIssue.getState().equals("accepted")) {
            ViewUtils.setTextViewLineFlag(vh.title, Paint.STRIKE_THRU_TEXT_FLAG
                    | Paint.ANTI_ALIAS_FLAG);
        } else {
            ViewUtils.setTextViewLineFlag(vh.title, 0 | Paint.ANTI_ALIAS_FLAG);
        }
    }

    private void setIssueSource(ViewHolder vh, TeamIssue teamIssue) {
        String source = teamIssue.getSource();
        if (TextUtils.isEmpty(source))
            return;
        TextView tv = vh.issueSource;
        if (source.equalsIgnoreCase(TeamIssue.TEAM_ISSUE_SOURCE_GITOSC)) {
            // 来自gitosc
            TypefaceUtils.setTypeface(tv, R.string.fa_gitosc);
        } else if (source.equalsIgnoreCase(TeamIssue.TEAM_ISSUE_SOURCE_GITHUB)) {
            // 来自github
            TypefaceUtils.setTypeface(tv, R.string.fa_github);
        } else {
            // 来自teamosc
            TypefaceUtils.setTypeface(tv, R.string.fa_team);
        }
    }

    private String getDeadlineTime(TeamIssue teamIssue) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = StringUtils.toDate(teamIssue.getUpdateTime(), dataFormat);
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    static class ViewHolder {
        @Bind(R.id.iv_issue_state)
        TextView state;
        @Bind(R.id.tv_title)
        TextView title;
        @Bind(R.id.iv_issue_source)
        TextView issueSource;
        @Bind(R.id.tv_project)
        TextView project;
        @Bind(R.id.tv_attachments)
        TextView attachments;// 附件
        @Bind(R.id.tv_childissues)
        TextView childissues;// 子任务
        @Bind(R.id.tv_relations)
        TextView relations;// 关联任务
        @Bind(R.id.tv_accept_time)
        TextView accept_time;
        @Bind(R.id.tv_author)
        TextView author;
        @Bind(R.id.tv_to)
        TextView to;
        @Bind(R.id.tv_touser)
        TextView touser;
        @Bind(R.id.tv_time)
        TextView time;
        @Bind(R.id.tv_comment_count)
        TextView comment;
        @Bind(R.id.title)
        TextView title_line;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
