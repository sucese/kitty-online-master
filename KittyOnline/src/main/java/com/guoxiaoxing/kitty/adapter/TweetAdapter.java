package com.guoxiaoxing.kitty.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.model.UserTalk;
import com.guoxiaoxing.kitty.emoji.InputHelper;
import com.guoxiaoxing.kitty.ui.activity.ImagePreviewActivity;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.util.DialogHelp;
import com.guoxiaoxing.kitty.util.ImageUtils;
import com.guoxiaoxing.kitty.util.KJAnimations;
import com.guoxiaoxing.kitty.util.PlatfromUtil;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.TypefaceUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.widget.AvatarView;
import com.guoxiaoxing.kitty.widget.MyLinkMovementMethod;
import com.guoxiaoxing.kitty.widget.TweetTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.utils.DensityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends ListBaseAdapter<UserTalk> {

    static class ViewHolder {
        @Bind(R.id.tv_tweet_name)
        TextView author;
        @Bind(R.id.tv_tweet_time)
        TextView time;
        @Bind(R.id.tweet_item)
        TweetTextView content;
        @Bind(R.id.tv_tweet_comment_count)
        TextView commentcount;
        @Bind(R.id.tv_tweet_platform)
        TextView platform;
        @Bind(R.id.iv_tweet_face)
        AvatarView face;
        @Bind(R.id.iv_tweet_image)
        ImageView image;
        @Bind(R.id.tv_like_state)
        TextView tvLikeState;
        @Bind(R.id.tv_del)
        TextView del;
        @Bind(R.id.tv_likeusers)
        TextView likeUsers;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = (String) v.getTag();
                    int index = url.lastIndexOf("?");
                    if (index > 0) {
                        url = url.substring(0, index);
                    }
                    ImagePreviewActivity.showImagePrivew(v.getContext(), 0, new String[]{url});
                }
            });
        }
    }

    private Bitmap recordBitmap;
    private Context context;

    final private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
        }
    };

    private void initRecordImg(Context cxt) {
        recordBitmap = BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.audio3);
        recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                DensityUtils.dip2px(cxt, 20f), DensityUtils.dip2px(cxt, 20f));
    }

    @Override
    protected View getRealView(final int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        final ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = View.inflate(context, R.layout.list_cell_tweet, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final UserTalk userTalk = mDatas.get(position);

        if (userTalk.getAuthorid() == AppContext.getInstance().getLoginUid()) {
            vh.del.setVisibility(View.VISIBLE);
            vh.del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionDel(context, userTalk, position);
                }
            });
        } else {
            vh.del.setVisibility(View.GONE);
        }

        vh.face.setUserInfo(userTalk.getAuthorid(), userTalk.getAuthor());
        vh.face.setAvatarUrl(userTalk.getPortrait());
        vh.author.setText(userTalk.getAuthor());
        vh.time.setText(StringUtils.friendly_time(userTalk.getPubDate()));
        vh.content.setMovementMethod(MyLinkMovementMethod.a());
        vh.content.setFocusable(false);
        vh.content.setDispatchToParent(true);
        vh.content.setLongClickable(false);

        Spanned span = Html.fromHtml(userTalk.getBody().trim());

        if (!StringUtils.isEmpty(userTalk.getAttach())) {
            if (recordBitmap == null) {
                initRecordImg(context);
            }
            ImageSpan recordImg = new ImageSpan(context, recordBitmap);
            SpannableString str = new SpannableString("c");
            str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            vh.content.setText(str);
            span = InputHelper.displayEmoji(context.getResources(), span);
            vh.content.append(span);
        } else {
            span = InputHelper.displayEmoji(context.getResources(), span);
            vh.content.setText(span);
        }

        vh.commentcount.setText(userTalk.getCommentCount());

        showTweetImage(vh, userTalk.getImgSmall(), userTalk.getImgBig());
        userTalk.setLikeUsers(context, vh.likeUsers, true);

        if (userTalk.getLikeUser() == null) {
            vh.tvLikeState.setVisibility(View.GONE);
        } else {
            vh.tvLikeState.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppContext.getInstance().isLogin()) {
                        updateLikeState(vh, userTalk);
                    } else {
                        AppContext.showToast("先登陆再赞~");
                        UIHelper.showLoginActivity(context);
                    }
                }
            });
        }

        TypefaceUtils.setTypeface(vh.tvLikeState);
        if (userTalk.getIsLike() == 1) {
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .day_colorPrimary));
        } else {
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .gray));
        }
        PlatfromUtil.setPlatFromString(vh.platform, userTalk.getAppclient());
        return convertView;
    }

    private void updateLikeState(ViewHolder vh, UserTalk userTalk) {
        if (userTalk.getIsLike() == 1) {
            userTalk.setIsLike(0);
            userTalk.setLikeCount(userTalk.getLikeCount() - 1);
            if (!userTalk.getLikeUser().isEmpty()) {
                userTalk.getLikeUser().remove(0);
            }
            OSChinaApi.pubUnLikeTweet(userTalk.getId(), userTalk.getAuthorid(),
                    handler);
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .gray));
        } else {
            vh.tvLikeState.setAnimation(KJAnimations.getScaleAnimation(1.5f, 300));
            userTalk.getLikeUser().add(0, AppContext.getInstance().getLoginUser());
            OSChinaApi.pubLikeTweet(userTalk.getId(), userTalk.getAuthorid(), handler);
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .day_colorPrimary));
            userTalk.setIsLike(1);
            userTalk.setLikeCount(userTalk.getLikeCount() + 1);
        }
        userTalk.setLikeUsers(context, vh.likeUsers, true);
    }

    private void optionDel(Context context, final UserTalk userTalk, final int position) {

        DialogHelp.getConfirmDialog(context, "确定删除吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OSChinaApi.deleteTweet(userTalk.getAuthorid(), userTalk.getId(),
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int arg0, Header[] arg1,
                                                  byte[] arg2) {
                                mDatas.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1,
                                                  byte[] arg2, Throwable arg3) {
                            }
                        });
            }
        }).show();
    }

    /**
     * 动态设置动弹列表图片显示规则
     */
    private void showTweetImage(final ViewHolder vh, String imgSmall, final String imgBig) {
        if (!TextUtils.isEmpty(imgBig)) {
            vh.image.setTag(imgBig);
            new Core.Builder().view(vh.image).size(300, 300).url(imgBig + "?300X300")
                    .loadBitmapRes(R.drawable.pic_bg).doTask();
            vh.image.setVisibility(AvatarView.VISIBLE);
        } else {
            vh.image.setVisibility(AvatarView.GONE);
        }
    }
}
