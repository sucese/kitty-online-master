package net.oschina.app.adapter;

import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Apply;
import net.oschina.app.widget.AvatarView;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 活动参会人员适配器
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年12月12日 下午8:10:43
 * 
 */
public class EventApplyAdapter extends ListBaseAdapter<Apply> {

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView,
            final ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_friend, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Apply item = (Apply) mDatas.get(position);

        vh.name.setText(item.getName());
        vh.avatar.setUserInfo(item.getId(), item.getName());
        vh.avatar.setAvatarUrl(item.getPortrait());
        vh.from.setVisibility(View.GONE);
        vh.desc.setText(item.getCompany() + " " + item.getJob());

        return convertView;
    }

    static class ViewHolder {

        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.tv_desc)
        TextView desc;
        @InjectView(R.id.tv_from) TextView from;
        @InjectView(R.id.iv_gender)
        ImageView gender;
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
