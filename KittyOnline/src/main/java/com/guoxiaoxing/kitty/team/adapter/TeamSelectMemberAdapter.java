package com.guoxiaoxing.kitty.team.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.team.bean.TeamMember;
import com.guoxiaoxing.kitty.widget.AvatarView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TeamSelectMemberAdapter extends BaseAdapter {

    static class ViewHolder {
	@Bind(R.id.iv_avatar)
	AvatarView aView;
	@Bind(R.id.tv_name)
	TextView name;

	ViewHolder(View view) {
	    ButterKnife.bind(this, view);
	}
    }

    private List<TeamMember> members;

    public TeamSelectMemberAdapter(List<TeamMember> members) {
	this.members = members;
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return members.size();
    }

    @Override
    public Object getItem(int position) {
	// TODO Auto-generated method stub
	return members.get(position);
    }

    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	ViewHolder vh = null;
	if (convertView == null || convertView.getTag() == null) {
	    LayoutInflater inflater = (LayoutInflater) parent.getContext()
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.list_cell_team_project_member, null);
	    vh = new ViewHolder(convertView);
	    convertView.setTag(vh);
	} else {
	    vh = (ViewHolder) convertView.getTag();
	}
	TeamMember item = members.get(position);
	vh.aView.setAvatarUrl(item.getPortrait());
	vh.name.setText(item.getName());
	return convertView;
    }
}
