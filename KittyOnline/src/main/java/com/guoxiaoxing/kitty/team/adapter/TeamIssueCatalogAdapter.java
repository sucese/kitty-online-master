package com.guoxiaoxing.kitty.team.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.team.bean.TeamIssueCatalog;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author guoxiaoxing
 */
public class TeamIssueCatalogAdapter extends ListBaseAdapter<TeamIssueCatalog> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_team_issue_catalog, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        TeamIssueCatalog item = mDatas.get(position);

        vh.title.setText(item.getTitle());
        vh.state.setText(item.getOpenedIssueCount() + "/"
                + item.getAllIssueCount());

        String description = item.getDescription();
        if (description != null && !StringUtils.isEmpty(description)) {
            vh.description.setText(description);
        } else {
            vh.description.setText("暂无描述");
        }

        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.tv_team_issue_catalog_title)
        TextView title;
        @Bind(R.id.tv_team_issue_catalog_desc)
        TextView description;
        @Bind(R.id.tv_team_issue_catalog_state)
        TextView state;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
