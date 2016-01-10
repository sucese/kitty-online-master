package com.guoxiaoxing.kitty.adapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.oschina.kitty.R;
import com.guoxiaoxing.kitty.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.bean.User;
import com.guoxiaoxing.kitty.widget.AvatarView;

/**
 * TweetLikeUsersAdapter.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-3-26 下午4:11:25
 */
public class TweetLikeUsersAdapter extends ListBaseAdapter<User> {
    
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
	ViewHolder vh = null;
	if (convertView == null || convertView.getTag() == null) {
	    convertView = getLayoutInflater(parent.getContext()).inflate(
		    R.layout.list_cell_tweet_like_user, null);
	    vh = new ViewHolder(convertView);
	    convertView.setTag(vh);
	} else {
	    vh = (ViewHolder) convertView.getTag();
	}
	User item = mDatas.get(position);
	vh.avatar.setAvatarUrl(item.getPortrait());
	vh.name.setText(item.getName());
        return convertView;
    }
    
    static class ViewHolder {
	
	@InjectView(R.id.iv_avatar)
	AvatarView avatar;
	@InjectView(R.id.tv_name)
	TextView name;
	
	public ViewHolder(View view) {
	    ButterKnife.inject(this, view);
	}
    }   
}

