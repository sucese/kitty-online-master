package com.guoxiaoxing.kitty.adapter;

import android.annotation.SuppressLint;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.oschina.kitty.R;
import com.guoxiaoxing.kitty.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.bean.TweetLike;
import com.guoxiaoxing.kitty.util.PlatfromUtil;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.widget.AvatarView;
import com.guoxiaoxing.kitty.widget.MyLinkMovementMethod;
import com.guoxiaoxing.kitty.widget.MyURLSpan;
import com.guoxiaoxing.kitty.widget.TweetTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 动弹点赞适配器 TweetLikeAdapter.java
 *
 * @author 火蚁(http://my.oschina.net/u/253900)
 * @data 2015-4-10 上午10:19:19
 */
public class TweetLikeAdapter extends ListBaseAdapter<TweetLike> {

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView,
                               final ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_tweet_like, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final TweetLike item = (TweetLike) mDatas.get(position);

        vh.name.setText(item.getUser().getName().trim());

        vh.action.setText("赞了我的动弹");

        vh.time.setText(StringUtils.friendly_time(item.getDatatime().trim()));

        PlatfromUtil.setPlatFromString(vh.from, item.getAppClient());
        vh.avatar.setUserInfo(item.getUser().getId(), item.getUser().getName());
        vh.avatar.setAvatarUrl(item.getUser().getPortrait());

        vh.reply.setMovementMethod(MyLinkMovementMethod.a());
        vh.reply.setFocusable(false);
        vh.reply.setDispatchToParent(true);
        vh.reply.setLongClickable(false);
        Spanned span = UIHelper.parseActiveReply(item.getTweet().getAuthor(),
                item.getTweet().getBody());
        vh.reply.setText(span);
        MyURLSpan.parseLinkText(vh.reply, span);

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.tv_from)
        TextView from;
        @InjectView(R.id.tv_time)
        TextView time;
        @InjectView(R.id.tv_action)
        TextView action;
        @InjectView(R.id.tv_reply)
        TweetTextView reply;
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
