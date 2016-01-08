package net.oschina.app.adapter;

import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.User;
import net.oschina.app.widget.AvatarView;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

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

	vh.name.setText(item.getName());

	vh.from.setText(item.getFrom());
	vh.desc.setVisibility(View.GONE);
	int genderIcon = R.drawable.userinfo_icon_male;
	if ("女".equals(item.getGender())) {
	    genderIcon = R.drawable.userinfo_icon_female;
	}

	vh.gender.setImageResource(genderIcon);

	vh.avatar.setAvatarUrl(item.getPortrait());
	vh.avatar.setUserInfo(item.getId(), item.getName());

	return convertView;
    }

    static class ViewHolder {

	@InjectView(R.id.tv_name)
	TextView name;
	@InjectView(R.id.tv_from)
	TextView from;
	@InjectView(R.id.tv_desc)
	TextView desc;
	@InjectView(R.id.iv_gender)
	ImageView gender;
	@InjectView(R.id.iv_avatar)
	AvatarView avatar;

	public ViewHolder(View view) {
	    ButterKnife.inject(this, view);
	}
    }
}
