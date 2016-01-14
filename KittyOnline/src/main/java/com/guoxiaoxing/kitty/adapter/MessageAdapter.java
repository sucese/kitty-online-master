package com.guoxiaoxing.kitty.adapter;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.bean.Messages;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.widget.AvatarView;
import com.guoxiaoxing.kitty.widget.MyLinkMovementMethod;
import com.guoxiaoxing.kitty.widget.MyURLSpan;
import com.guoxiaoxing.kitty.widget.TweetTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageAdapter extends ListBaseAdapter<Messages> {

    @Override
    protected boolean loadMoreHasBg() {
        return false;
    }

    @Override
    protected View getRealView(int position, View convertView,
                               final ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_message, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Messages item = (Messages) mDatas.get(position);

        if (AppContext.getInstance().getLoginUid() == item.getSenderId()) {
            vh.sender.setVisibility(View.VISIBLE);
        } else {
            vh.sender.setVisibility(View.GONE);
        }

        vh.name.setText(item.getFriendName());

        vh.content.setMovementMethod(MyLinkMovementMethod.a());
        vh.content.setFocusable(false);
        vh.content.setDispatchToParent(true);
        vh.content.setLongClickable(false);
        Spanned span = Html.fromHtml(item.getContent());
        vh.content.setText(span);
        MyURLSpan.parseLinkText(vh.content, span);

        vh.time.setText(StringUtils.friendly_time(item.getPubDate()));
        vh.count.setText(parent.getResources().getString(
                R.string.message_count, item.getMessageCount()));

        vh.avatar.setAvatarUrl(item.getPortrait());
        vh.avatar.setUserInfo(item.getSenderId(), item.getSender());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_avatar)
        AvatarView avatar;
        @Bind(R.id.tv_name)
        TextView name;
        @Bind(R.id.tv_sender)
        TextView sender;
        @Bind(R.id.tv_time)
        TextView time;
        @Bind(R.id.tv_count)
        TextView count;
        @Bind(R.id.tv_content)
        TweetTextView content;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
