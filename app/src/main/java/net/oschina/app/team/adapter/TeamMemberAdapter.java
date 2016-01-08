package net.oschina.app.team.adapter;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.R;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.bean.TeamMember;
import net.oschina.app.util.UIHelper;
import net.oschina.app.widget.AvatarView;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 团队成员GridView适配器
 * 
 * @author kymjs (kymjs123@gmail.com)
 * 
 */
public class TeamMemberAdapter extends BaseAdapter {
    private final Context cxt;
    private List<TeamMember> datas;
    private final Team team;
    public static final String TEAM_MEMBER_KEY = "TeamMemberAdapter_teammemberkey";
    public static final String TEAM_ID_KEY = "TeamMemberAdapter_teaminfokey";

    public TeamMemberAdapter(Context context, List<TeamMember> datas, Team team) {
        this.cxt = context;
        this.team = team;
        if (datas == null) {
            datas = new ArrayList<TeamMember>(1);
        }
        this.datas = datas;
    }

    public void refresh(List<TeamMember> datas) {
        if (datas == null) {
            datas = new ArrayList<TeamMember>(1);
        }
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        ImageView img_tip;
        AvatarView img_head;
        TextView tv_name;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        ViewHolder holder = null;
        TeamMember data = datas.get(position);
        if (v == null) {
            v = View.inflate(cxt, R.layout.item_team_member, null);
            holder = new ViewHolder();
            holder.img_head = (AvatarView) v
                    .findViewById(R.id.item_team_member_head);
            holder.img_tip = (ImageView) v
                    .findViewById(R.id.item_team_membar_tip);
            holder.tv_name = (TextView) v
                    .findViewById(R.id.item_team_membar_name);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.tv_name.setText(data.getName());
        holder.img_head.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showTeamMemberInfo(cxt, team.getId(),
                        datas.get(position));
            }
        });
        holder.img_head.setAvatarUrl(data.getPortrait());
        if (127 == data.getTeamRole()) { // 创建人，红色
            holder.img_tip.setImageDrawable(new ColorDrawable(0xffff0000));
        } else if (126 == data.getTeamRole()) { // 管理者，黄色
            holder.img_tip.setImageDrawable(new ColorDrawable(0xffffb414));
        } else {
            holder.img_tip.setImageDrawable(null);
        }
        return v;
    }
}
