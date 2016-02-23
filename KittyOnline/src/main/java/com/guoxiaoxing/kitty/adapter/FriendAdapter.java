package com.guoxiaoxing.kitty.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.Friend;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.widget.AvatarView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 好友列表适配器
 * 
 * @author guoxiaoxing
 */
public class FriendAdapter extends ListBaseAdapter<Friend> {

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

        final Friend item = mDatas.get(position);

        vh.name.setText(item.getName());
        String from = item.getFrom();
        if (from != null || !StringUtils.isEmpty(from)) {
            vh.from.setText(from);
        } else {
            vh.from.setVisibility(View.GONE);
        }
        String desc = item.getExpertise();
        if (desc != null || !StringUtils.isEmpty(from) || !"<无>".equals(desc)) {
            vh.desc.setText(item.getExpertise());
        } else {
            vh.desc.setVisibility(View.GONE);
        }

        vh.gender
                .setImageResource(item.getGender() == 1 ? R.drawable.userinfo_icon_male
                        : R.drawable.userinfo_icon_female);

        vh.avatar.setAvatarUrl(item.getPortrait());
        vh.avatar.setUserInfo(item.getUserid(), item.getName());

        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.tv_name)
        TextView name;
        @Bind(R.id.tv_from)
        TextView from;
        @Bind(R.id.tv_desc)
        TextView desc;
        @Bind(R.id.iv_gender)
        ImageView gender;
        @Bind(R.id.iv_avatar)
        AvatarView avatar;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
