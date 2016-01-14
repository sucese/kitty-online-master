package com.guoxiaoxing.kitty.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.bean.User;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.widget.AvatarView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 *@author guoxiaoxing
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
	
	@Bind(R.id.iv_avatar)
	AvatarView avatar;
	@Bind(R.id.tv_name)
	TextView name;
	
	public ViewHolder(View view) {
	    ButterKnife.bind(this, view);
	}
    }   
}

