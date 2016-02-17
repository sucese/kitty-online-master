package com.guoxiaoxing.kitty.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.bean.User;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.widget.AvatarView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 好友列表适配器
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月6日 上午11:22:27
 * 
 */
public class FindUserAdapter extends ListBaseAdapter<User> {

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

	final User item = (User) mDatas.get(position);

	vh.name.setText(item.getUsername());

//	vh.from.setText(item.getFrom());
	vh.desc.setVisibility(View.GONE);
	int genderIcon = R.drawable.userinfo_icon_male;
	if ("女".equals(item.getGender())) {
	    genderIcon = R.drawable.userinfo_icon_female;
	}

	vh.gender.setImageResource(genderIcon);

	vh.avatar.setAvatarUrl(item.getFace());
	vh.avatar.setUserInfo(item.getId(), item.getUsername());

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
