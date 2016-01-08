package net.oschina.app.team.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.team.bean.TeamDiary;
import net.oschina.app.util.StringUtils;
import net.oschina.app.widget.AvatarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 周报的ListView适配器
 * 
 * @author kymjs (http://www.kymjs.com)
 * 
 */
public class TeamDiaryListAdapter extends BaseAdapter {
    private final Context cxt;
    private List<TeamDiary> list;

    public TeamDiaryListAdapter(Context cxt, List<TeamDiary> list) {
        this.cxt = cxt;
        if (list == null) {
            list = new ArrayList<TeamDiary>(1);
        }
        this.list = list;
    }

    public void refresh(List<TeamDiary> list) {
        if (list == null) {
            list = new ArrayList<TeamDiary>(1);
        }
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        TeamDiary data = list.get(position);
        ViewHolder holder = null;
        if (v == null) {
            v = View.inflate(cxt, R.layout.list_cell_team_diary, null);
            holder = new ViewHolder();
            holder.iv_face = (AvatarView) v.findViewById(R.id.iv_face);
            holder.tv_author = (TextView) v.findViewById(R.id.tv_author);
            holder.tv_title = (TextView) v.findViewById(R.id.tv_title);
            holder.tv_date = (TextView) v.findViewById(R.id.tv_date);
            holder.tv_count = (TextView) v.findViewById(R.id.tv_count);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.iv_face.setAvatarUrl(data.getAuthor().getPortrait());
        holder.tv_author.setText(data.getAuthor().getName());
        holder.tv_date.setText(StringUtils.friendly_time(data.getCreateTime()));
        holder.tv_count.setText(data.getReply() + "");
        holder.tv_title.setText(Html.fromHtml(data.getTitle()).toString()
                .trim());
        return v;
    }

    static class ViewHolder {
        AvatarView iv_face;
        TextView tv_author;
        TextView tv_title;
        TextView tv_date;
        TextView tv_count;
    }
}
