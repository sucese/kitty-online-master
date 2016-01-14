package com.guoxiaoxing.kitty.team.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.team.bean.TeamProject;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.util.TypefaceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 团队项目适配器
 *
 * @author guoxiaoxing
 */

public class TeamProjectListAdapterNew extends ListBaseAdapter<TeamProject> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.list_cell_team_project, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        TeamProject item = mDatas.get(position);

        String source = item.getSource();
        TextView tvSource = vh.source;
        if (TextUtils.isEmpty(source)) {
            if (item.getGit().getId() == -1) {
                TypefaceUtils.setTypeface(tvSource, R.string.fa_tasks);
            } else {
                TypefaceUtils.setTypeface(tvSource, R.string.fa_inbox);
            }
        } else if (source.equalsIgnoreCase(TeamProject.GITOSC)) {
            TypefaceUtils.setTypeface(tvSource, R.string.fa_gitosc);
        } else if (source.equalsIgnoreCase(TeamProject.GITHUB)) {
            TypefaceUtils.setTypeface(tvSource, R.string.fa_github);
        } else {
            TypefaceUtils.setTypeface(tvSource, R.string.fa_list_alt);
        }

        vh.name.setText(item.getGit().getOwnerName() + " / " + item.getGit().getName());
        vh.issue.setText(item.getIssue().getOpened() + "/" + item.getIssue().getAll());

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.iv_source)
        TextView source;
        @Bind(R.id.tv_project_name)
        TextView name;
        @Bind(R.id.tv_project_issue)
        TextView issue;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
