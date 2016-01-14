package com.guoxiaoxing.kitty.team.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.team.bean.TeamMember;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.widget.AvatarView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 团队项目适配器
 *
 * @author guoxiaoxing
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
        @Bind(R.id.iv_avatar)
        AvatarView avatar;
        @Bind(R.id.tv_name)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
