package net.oschina.app.team.adapter;

import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.team.bean.TeamIssueCatalog;
import net.oschina.app.util.StringUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * TeamIssueCatalogAdapter.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 * 
 * @data 2015-3-1 下午3:37:03
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

	@InjectView(R.id.tv_team_issue_catalog_title)
	TextView title;
	@InjectView(R.id.tv_team_issue_catalog_desc)
	TextView description;
	@InjectView(R.id.tv_team_issue_catalog_state)
	TextView state;

	public ViewHolder(View view) {
	    ButterKnife.inject(this, view);
	}
    }
}
