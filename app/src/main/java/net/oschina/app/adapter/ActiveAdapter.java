package net.oschina.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Active;
import net.oschina.app.bean.Active.ObjectReply;
import net.oschina.app.emoji.InputHelper;
import net.oschina.app.ui.ImagePreviewActivity;
import net.oschina.app.util.BitmapHelper;
import net.oschina.app.util.ImageUtils;
import net.oschina.app.util.PlatfromUtil;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.UIHelper;
import net.oschina.app.widget.AvatarView;
import net.oschina.app.widget.MyLinkMovementMethod;
import net.oschina.app.widget.MyURLSpan;
import net.oschina.app.widget.TweetTextView;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.utils.DensityUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ActiveAdapter extends ListBaseAdapter {
    private final static String AT_HOST_PRE = "http://my.oschina.net";
    private final static String MAIN_HOST = "http://www.oschina.net";

    public ActiveAdapter() {
    }

    private Bitmap recordBitmap;
    private int rectSize;

    private void initRecordImg(Context cxt) {
        recordBitmap = BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.audio3);
        recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                DensityUtils.dip2px(cxt, 20f), DensityUtils.dip2px(cxt, 20f));
    }

    private void initImageSize(Context cxt) {
        if (cxt != null && rectSize == 0) {
            rectSize = (int) cxt.getResources().getDimension(R.dimen.space_100);
        } else {
            rectSize = 300;
        }
    }

    @Override
    @SuppressLint("InflateParams")
    protected View getRealView(int position, View convertView,
                               final ViewGroup parent) {
        ViewHolder vh;
        initImageSize(parent.getContext());
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_active, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Active item = (Active) mDatas.get(position);

        vh.name.setText(item.getAuthor());

        vh.action.setText(UIHelper.parseActiveAction(item.getObjectType(),
                item.getObjectCatalog(), item.getObjectTitle()));

        if (TextUtils.isEmpty(item.getMessage())) {
            vh.body.setVisibility(View.GONE);
        } else {
            vh.body.setMovementMethod(MyLinkMovementMethod.a());
            vh.body.setFocusable(false);
            vh.body.setDispatchToParent(true);
            vh.body.setLongClickable(false);

            Spanned span = Html.fromHtml(modifyPath(item.getMessage()));

            if (!StringUtils.isEmpty(item.getTweetattach())) {
                if (recordBitmap == null) {
                    initRecordImg(parent.getContext());
                }
                ImageSpan recordImg = new ImageSpan(parent.getContext(),
                        recordBitmap);
                SpannableString str = new SpannableString("c");
                str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                vh.body.setText(str);
                span = InputHelper.displayEmoji(parent.getContext()
                        .getResources(), span);
                vh.body.append(span);
            } else {
                span = InputHelper.displayEmoji(parent.getContext()
                        .getResources(), span);
                vh.body.setText(span);
            }
            MyURLSpan.parseLinkText(vh.body, span);
        }

        ObjectReply reply = item.getObjectReply();
        if (reply != null) {
            vh.reply.setMovementMethod(MyLinkMovementMethod.a());
            vh.reply.setFocusable(false);
            vh.reply.setDispatchToParent(true);
            vh.reply.setLongClickable(false);
            Spanned span = UIHelper.parseActiveReply(reply.objectName,
                    reply.objectBody);
            vh.reply.setText(span);//
            MyURLSpan.parseLinkText(vh.reply, span);
            vh.lyReply.setVisibility(TextView.VISIBLE);
        } else {
            vh.reply.setText("");
            vh.lyReply.setVisibility(TextView.GONE);
        }

        vh.time.setText(StringUtils.friendly_time(item.getPubDate()));

        PlatfromUtil.setPlatFromString(vh.from, item.getAppClient());

        vh.commentCount.setText(item.getCommentCount() + "");

        vh.avatar.setUserInfo(item.getAuthorId(), item.getAuthor());
        vh.avatar.setAvatarUrl(item.getPortrait());

        if (!TextUtils.isEmpty(item.getTweetimage())) {
            setTweetImage(parent, vh, item);
        } else {
            vh.pic.setVisibility(View.GONE);
            vh.pic.setImageBitmap(null);
        }

        return convertView;
    }

    /**
     * 动态设置图片显示样式
     */
    private void setTweetImage(final ViewGroup parent, final ViewHolder vh,
                               final Active item) {
        vh.pic.setVisibility(View.VISIBLE);

        new Core.Builder().url(item.getTweetimage()).view(vh.pic).loadBitmapRes(R.drawable
                .pic_bg).size(rectSize, rectSize).bitmapCallBack(new BitmapCallBack() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                super.onSuccess(bitmap);
                if (bitmap != null) {
                    bitmap = BitmapHelper.scaleWithXY(bitmap, rectSize / bitmap.getHeight());
                    vh.pic.setImageBitmap(bitmap);
                }
            }
        }).doTask();

        vh.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewActivity.showImagePrivew(parent.getContext(), 0,
                        new String[]{getOriginalUrl(item.getTweetimage())});
            }
        });
    }

    private String modifyPath(String message) {
        message = message.replaceAll("(<a[^>]+href=\")/([\\S]+)\"", "$1"
                + AT_HOST_PRE + "/$2\"");
        message = message.replaceAll(
                "(<a[^>]+href=\")http://m.oschina.net([\\S]+)\"", "$1"
                        + MAIN_HOST + "$2\"");
        return message;
    }

    private String getOriginalUrl(String url) {
        return url.replaceAll("_thumb", "");
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
        @InjectView(R.id.tv_action_name)
        TextView actionName;
        @InjectView(R.id.tv_comment_count)
        TextView commentCount;
        @InjectView(R.id.tv_body)
        TweetTextView body;
        @InjectView(R.id.tv_reply)
        TweetTextView reply;
        @InjectView(R.id.iv_pic)
        ImageView pic;
        @InjectView(R.id.ly_reply)
        View lyReply;
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
