package net.oschina.app.adapter;

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

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Tweet;
import net.oschina.app.emoji.InputHelper;
import net.oschina.app.ui.ImagePreviewActivity;
import net.oschina.app.util.DialogHelp;
import net.oschina.app.util.ImageUtils;
import net.oschina.app.util.KJAnimations;
import net.oschina.app.util.PlatfromUtil;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.TypefaceUtils;
import net.oschina.app.util.UIHelper;
import net.oschina.app.widget.AvatarView;
import net.oschina.app.widget.MyLinkMovementMethod;
import net.oschina.app.widget.TweetTextView;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.utils.DensityUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends ListBaseAdapter<Tweet> {

    static class ViewHolder {
        @InjectView(R.id.tv_tweet_name)
        TextView author;
        @InjectView(R.id.tv_tweet_time)
        TextView time;
        @InjectView(R.id.tweet_item)
        TweetTextView content;
        @InjectView(R.id.tv_tweet_comment_count)
        TextView commentcount;
        @InjectView(R.id.tv_tweet_platform)
        TextView platform;
        @InjectView(R.id.iv_tweet_face)
        AvatarView face;
        @InjectView(R.id.iv_tweet_image)
        ImageView image;
        @InjectView(R.id.tv_like_state)
        TextView tvLikeState;
        @InjectView(R.id.tv_del)
        TextView del;
        @InjectView(R.id.tv_likeusers)
        TextView likeUsers;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
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
        final Tweet tweet = mDatas.get(position);

        if (tweet.getAuthorid() == AppContext.getInstance().getLoginUid()) {
            vh.del.setVisibility(View.VISIBLE);
            vh.del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionDel(context, tweet, position);
                }
            });
        } else {
            vh.del.setVisibility(View.GONE);
        }

        vh.face.setUserInfo(tweet.getAuthorid(), tweet.getAuthor());
        vh.face.setAvatarUrl(tweet.getPortrait());
        vh.author.setText(tweet.getAuthor());
        vh.time.setText(StringUtils.friendly_time(tweet.getPubDate()));
        vh.content.setMovementMethod(MyLinkMovementMethod.a());
        vh.content.setFocusable(false);
        vh.content.setDispatchToParent(true);
        vh.content.setLongClickable(false);

        Spanned span = Html.fromHtml(tweet.getBody().trim());

        if (!StringUtils.isEmpty(tweet.getAttach())) {
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

        vh.commentcount.setText(tweet.getCommentCount());

        showTweetImage(vh, tweet.getImgSmall(), tweet.getImgBig());
        tweet.setLikeUsers(context, vh.likeUsers, true);

        if (tweet.getLikeUser() == null) {
            vh.tvLikeState.setVisibility(View.GONE);
        } else {
            vh.tvLikeState.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppContext.getInstance().isLogin()) {
                        updateLikeState(vh, tweet);
                    } else {
                        AppContext.showToast("先登陆再赞~");
                        UIHelper.showLoginActivity(context);
                    }
                }
            });
        }

        TypefaceUtils.setTypeface(vh.tvLikeState);
        if (tweet.getIsLike() == 1) {
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .day_colorPrimary));
        } else {
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .gray));
        }
        PlatfromUtil.setPlatFromString(vh.platform, tweet.getAppclient());
        return convertView;
    }

    private void updateLikeState(ViewHolder vh, Tweet tweet) {
        if (tweet.getIsLike() == 1) {
            tweet.setIsLike(0);
            tweet.setLikeCount(tweet.getLikeCount() - 1);
            if (!tweet.getLikeUser().isEmpty()) {
                tweet.getLikeUser().remove(0);
            }
            OSChinaApi.pubUnLikeTweet(tweet.getId(), tweet.getAuthorid(),
                    handler);
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .gray));
        } else {
            vh.tvLikeState.setAnimation(KJAnimations.getScaleAnimation(1.5f, 300));
            tweet.getLikeUser().add(0, AppContext.getInstance().getLoginUser());
            OSChinaApi.pubLikeTweet(tweet.getId(), tweet.getAuthorid(), handler);
            vh.tvLikeState.setTextColor(AppContext.getInstance().getResources().getColor(R.color
                    .day_colorPrimary));
            tweet.setIsLike(1);
            tweet.setLikeCount(tweet.getLikeCount() + 1);
        }
        tweet.setLikeUsers(context, vh.likeUsers, true);
    }

    private void optionDel(Context context, final Tweet tweet, final int position) {

        DialogHelp.getConfirmDialog(context, "确定删除吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OSChinaApi.deleteTweet(tweet.getAuthorid(), tweet.getId(),
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
