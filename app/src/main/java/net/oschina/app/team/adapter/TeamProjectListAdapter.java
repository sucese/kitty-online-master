package net.oschina.app.team.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.team.bean.TeamProject;
import net.oschina.app.util.TypefaceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 团队项目适配器
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月19日 下午6:00:33
 * 
 */

public class TeamProjectListAdapter extends BaseAdapter {

    private final ArrayList<TeamProject> datas = new ArrayList<TeamProject>();

    public void add(TeamProject project) {
        this.datas.add(project);
        notifyDataSetChanged();
    }

    public void add(List<TeamProject> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public TeamProject getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.list_cell_team_project, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        TeamProject item = datas.get(position);

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
        vh.name.setText(item.getGit().getName());

        return convertView;
    }

    public static class ViewHolder {
        @InjectView(R.id.iv_source)
        TextView source;
        @InjectView(R.id.tv_project_name)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}