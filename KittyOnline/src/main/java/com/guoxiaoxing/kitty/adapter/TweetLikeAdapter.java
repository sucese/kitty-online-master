package com.guoxiaoxing.kitty.adapter;

import android.annotation.SuppressLint;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.TweetLike;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.util.PlatfromUtil;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.widget.AvatarView;
import com.guoxiaoxing.kitty.widget.MyLinkMovementMethod;
import com.guoxiaoxing.kitty.widget.MyURLSpan;
import com.guoxiaoxing.kitty.widget.TweetTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 动弹点赞适配器
 *
 * @author guoxiaoxing
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

        vh.name.setText(item.getUser().getUsername().trim());

        vh.action.setText("赞了我的动弹");

        vh.time.setText(StringUtils.friendly_time(item.getDatatime().trim()));

        PlatfromUtil.setPlatFromString(vh.from, item.getAppClient());
        vh.avatar.setUserInfo(item.getUser().getId(), item.getUser().getUsername());
        vh.avatar.setAvatarUrl(item.getUser().getFace());

        vh.reply.setMovementMethod(MyLinkMovementMethod.a());
        vh.reply.setFocusable(false);
        vh.reply.setDispatchToParent(true);
        vh.reply.setLongClickable(false);
        Spanned span = UIHelper.parseActiveReply(item.getUserTalk().getAuthor(),
                item.getUserTalk().getBody());
        vh.reply.setText(span);
        MyURLSpan.parseLinkText(vh.reply, span);

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView name;
        @Bind(R.id.tv_from)
        TextView from;
        @Bind(R.id.tv_time)
        TextView time;
        @Bind(R.id.tv_action)
        TextView action;
        @Bind(R.id.tv_reply)
        TweetTextView reply;
        @Bind(R.id.iv_avatar)
        AvatarView avatar;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
