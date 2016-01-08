package net.oschina.app.adapter;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Messages;
import net.oschina.app.util.StringUtils;
import net.oschina.app.widget.AvatarView;
import net.oschina.app.widget.MyLinkMovementMethod;
import net.oschina.app.widget.MyURLSpan;
import net.oschina.app.widget.TweetTextView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

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
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.tv_sender)
        TextView sender;
        @InjectView(R.id.tv_time)
        TextView time;
        @InjectView(R.id.tv_count)
        TextView count;
        @InjectView(R.id.tv_content)
        TweetTextView content;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
