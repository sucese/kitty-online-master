package net.oschina.app.team.adapter;

import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.team.bean.TeamMember;
import net.oschina.app.widget.AvatarView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 团队项目适配器
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月19日 下午6:00:33
 * 
 */

public class TeamProjectMemberAdapter extends ListBaseAdapter<TeamMember> {
    
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	ViewHolder vh;
	if (convertView == null || convertView.getTag() == null) {
	    convertView = View.inflate(parent.getContext(),
		    R.layout.list_cell_team_project_member, null);
	    vh = new ViewHolder(convertView);
	    convertView.setTag(vh);
	} else {
	    vh = (ViewHolder) convertView.getTag();
	}

	TeamMember item = mDatas.get(position);
	
	vh.avatar.setAvatarUrl(item.getPortrait());
	vh.name.setText(item.getName());

	return convertView;
    }

    public static class ViewHolder {
	@InjectView(R.id.iv_avatar)
	AvatarView avatar;
	@InjectView(R.id.tv_name)
	TextView name;

	public ViewHolder(View view) {
	    ButterKnife.inject(this, view);
	}
    }

}
