package net.oschina.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.Comment.Refer;
import net.oschina.app.bean.Comment.Reply;
import net.oschina.app.emoji.InputHelper;
import net.oschina.app.util.PlatfromUtil;
import net.oschina.app.util.StringUtils;
import net.oschina.app.widget.AvatarView;
import net.oschina.app.widget.FloorView;
import net.oschina.app.widget.MyLinkMovementMethod;
import net.oschina.app.widget.MyURLSpan;
import net.oschina.app.widget.TweetTextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommentAdapter extends ListBaseAdapter<Comment> {

    @SuppressLint({ "InflateParams", "CutPasteId" })
    @Override
    protected View getRealView(int position, View convertView,
            final ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_comment, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {

            final Comment item = mDatas.get(position);

            // 若Authorid为0，则显示非会员
            vh.name.setText(item.getAuthor()
                    + (item.getAuthorId() == 0 ? "(非会员)" : ""));

            vh.content.setMovementMethod(MyLinkMovementMethod.a());
            vh.content.setFocusable(false);
            vh.content.setDispatchToParent(true);
            vh.content.setLongClickable(false);
            Spanned span = Html.fromHtml(TweetTextView.modifyPath(item
                    .getContent()));
            span = InputHelper.displayEmoji(parent.getContext().getResources(),
                    span.toString());
            vh.content.setText(span);
            MyURLSpan.parseLinkText(vh.content, span);

            vh.time.setText(StringUtils.friendly_time(item.getPubDate()));

            PlatfromUtil.setPlatFromString(vh.from, item.getAppClient());

            // setup refers
            setupRefers(parent.getContext(), vh, item.getRefers());

            // setup replies
            setupReplies(parent.getContext(), vh, item.getReplies());

            vh.avatar.setAvatarUrl(item.getPortrait());
            vh.avatar.setUserInfo(item.getAuthorId(), item.getAuthor());
        } catch (Exception e) {
        }
        return convertView;
    }

    private void setupRefers(Context context, ViewHolder vh, List<Refer> refers) {
        vh.refers.removeAllViews();
        if (refers == null || refers.size() <= 0) {
            vh.refers.setVisibility(View.GONE);
        } else {
            vh.refers.setVisibility(View.VISIBLE);

            vh.refers.setComments(refers);
        }
    }

    private void setupReplies(Context context, ViewHolder vh,
            List<Reply> replies) {
        vh.relies.removeAllViews();
        if (replies == null || replies.size() <= 0) {
            vh.relies.setVisibility(View.GONE);
        } else {
            vh.relies.setVisibility(View.VISIBLE);

            // add count layout
            View countView = getLayoutInflater(context).inflate(
                    R.layout.list_cell_reply_count, null, false);
            TextView count = (TextView) countView
                    .findViewById(R.id.tv_comment_reply_count);
            count.setText(context.getResources().getString(
                    R.string.comment_reply_count, replies.size()));
            vh.relies.addView(countView);

            // add reply item
            for (Reply reply : replies) {
                LinearLayout replyItemView = (LinearLayout) getLayoutInflater(
                        context).inflate(R.layout.list_cell_reply_name_content,
                        null, false);

                replyItemView.setOrientation(LinearLayout.HORIZONTAL);

                replyItemView
                        .setBackgroundResource(R.drawable.comment_background);

                TextView name = (TextView) replyItemView
                        .findViewById(R.id.tv_reply_name);
                name.setText(reply.rauthor + ":");

                TweetTextView replyContent = (TweetTextView) replyItemView
                        .findViewById(R.id.tv_reply_content);
                replyContent.setMovementMethod(MyLinkMovementMethod.a());
                replyContent.setFocusable(false);
                replyContent.setDispatchToParent(true);
                replyContent.setLongClickable(false);
                Spanned rcontent = Html.fromHtml(reply.rcontent);
                replyContent.setText(rcontent);
                MyURLSpan.parseLinkText(replyContent, rcontent);

                vh.relies.addView(replyItemView);
            }
        }
    }

    static class ViewHolder {
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.tv_time)
        TextView time;
        @InjectView(R.id.tv_from)
        TextView from;
        @InjectView(R.id.tv_content)
        TweetTextView content;
        @InjectView(R.id.ly_relies)
        LinearLayout relies;
        @InjectView(R.id.ly_refers)
        FloorView refers;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
