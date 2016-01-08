package net.oschina.app.bean;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.oschina.app.AppContext;
import net.oschina.app.base.BaseListFragment;
import net.oschina.app.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 动弹实体类
 *
 * @author liux (http://my.oschina.net/liux),kymjs(kymjs123@gmail.com)
 * @version 1.1 添加语音动弹功能
 * @created 2012-3-21
 * @changed 2014-12-1
 */
@SuppressWarnings("serial")
@XStreamAlias("tweet")
public class Tweet extends Entity implements Parcelable {

    @XStreamAlias("portrait")
    private String portrait;
    @XStreamAlias("author")
    private String author;
    @XStreamAlias("authorid")
    private int authorid;
    @XStreamAlias("body")
    private String body;
    @XStreamAlias("appclient")
    private int appclient;
    @XStreamAlias("commentCount")
    private String commentCount;
    @XStreamAlias("pubDate")
    private String pubDate;
    @XStreamAlias("imgSmall")
    private String imgSmall;
    @XStreamAlias("imgBig")
    private String imgBig;
    @XStreamAlias("attach")
    private String attach;

    @XStreamAlias("likeCount")
    private int likeCount;

    @XStreamAlias("isLike")
    private int isLike;

    @XStreamAlias("likeList")
    private List<User> likeUser = new ArrayList<User>();

    private String imageFilePath;
    private String audioPath;

    public Tweet() {
    }

    public Tweet(Parcel dest) {
        id = dest.readInt();
        portrait = dest.readString();
        author = dest.readString();
        authorid = dest.readInt();
        body = dest.readString();
        appclient = dest.readInt();
        commentCount = dest.readString();
        pubDate = dest.readString();
        imgSmall = dest.readString();
        imgBig = dest.readString();
        attach = dest.readString();
        imageFilePath = dest.readString();
        audioPath = dest.readString();
        isLike = dest.readInt();
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAppclient() {
        return appclient;
    }

    public void setAppclient(int appclient) {
        this.appclient = appclient;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }

    public String getImgBig() {
        return imgBig;
    }

    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public List<User> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(List<User> likeUser) {
        this.likeUser = likeUser;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(portrait);
        dest.writeString(author);
        dest.writeInt(authorid);
        dest.writeString(body);
        dest.writeInt(appclient);
        dest.writeString(commentCount);
        dest.writeString(pubDate);
        dest.writeString(imgSmall);
        dest.writeString(imgBig);
        dest.writeString(attach);
        dest.writeString(imageFilePath);
        dest.writeString(audioPath);
        dest.writeInt(isLike);
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Creator<Tweet>() {

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }

        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }
    };

    public void setLikeUsers(Context contet, TextView likeUser, boolean limit) {
        // 构造多个超链接的html, 通过选中的位置来获取用户名
        if (getLikeCount() > 0 && getLikeUser() != null
                && !getLikeUser().isEmpty()) {
            likeUser.setVisibility(View.VISIBLE);
            likeUser.setMovementMethod(LinkMovementMethod.getInstance());
            likeUser.setFocusable(false);
            likeUser.setLongClickable(false);
            likeUser.setText(addClickablePart(contet, limit), BufferType.SPANNABLE);
        } else {
            likeUser.setVisibility(View.GONE);
            likeUser.setText("");
        }
    }

    /**
     * @return
     */
    private SpannableStringBuilder addClickablePart(final Context context,
                                                    boolean limit) {

        StringBuilder sbBuilder = new StringBuilder();
        int showCunt = getLikeUser().size();
        if (limit && showCunt > 4) {
            showCunt = 4;
        }

        // 如果已经点赞，始终让该用户在首位
        if (getIsLike() == 1) {

            for (int i = 0; i < getLikeUser().size(); i++) {
                if (getLikeUser().get(i).getId() == AppContext.getInstance()
                        .getLoginUid()) {
                    getLikeUser().remove(i);
                }
            }
            getLikeUser().add(0, AppContext.getInstance().getLoginUser());
        }

        for (int i = 0; i < showCunt; i++) {
            sbBuilder.append(getLikeUser().get(i).getName()).append("、");
        }

        String likeUsersStr = sbBuilder.substring(0, sbBuilder.lastIndexOf("、"));

        // 第一个赞图标
        // ImageSpan span = new ImageSpan(AppContext.getInstance(),
        // R.drawable.ic_unlike_small);
        SpannableString spanStr = new SpannableString("");
        // spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(likeUsersStr);

        String[] likeUsers = likeUsersStr.split("、");

        if (likeUsers.length > 0) {
            // 最后一个
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = likeUsersStr.indexOf(name) + spanStr.length();
                final int index = i;
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        User user = getLikeUser().get(index);
                        UIHelper.showUserCenter(context, user.getId(),
                                user.getName());
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        // ds.setColor(R.color.link_color); // 设置文本颜色
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }
                }, start, start + name.length(), 0);
            }
        }
        if (likeUsers.length < getLikeCount()) {
            ssb.append("等");
            int start = ssb.length();
            String more = getLikeCount() + "人";
            ssb.append(more);
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, getId());
                    UIHelper.showSimpleBack(context,
                            SimpleBackPage.TWEET_LIKE_USER_LIST, bundle);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    // ds.setColor(R.color.link_color); // 设置文本颜色
                    // 去掉下划线
                    ds.setUnderlineText(false);
                }

            }, start, start + more.length(), 0);
            return ssb.append("觉得很赞");
        } else {
            return ssb.append("觉得很赞");
        }
    }
}
