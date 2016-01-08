package net.oschina.app.team.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.team.bean.TeamProject;
import net.oschina.app.util.TypefaceUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 团队项目适配器
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月19日 下午6:00:33
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
        @InjectView(R.id.iv_source)
        TextView source;
        @InjectView(R.id.tv_project_name)
        TextView name;
        @InjectView(R.id.tv_project_issue)
        TextView issue;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
