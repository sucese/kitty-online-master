package com.guoxiaoxing.kitty.ui.fragment;

import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.ui.base.CommonDetailFragment;
import com.guoxiaoxing.kitty.bean.CommentList;
import com.guoxiaoxing.kitty.bean.FavoriteList;
import com.guoxiaoxing.kitty.bean.Post;
import com.guoxiaoxing.kitty.bean.PostDetail;
import com.guoxiaoxing.kitty.ui.DetailActivity;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.ThemeSwitchUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.URLsUtils;
import com.guoxiaoxing.kitty.util.XmlUtils;

import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created by 火蚁 on 15/5/25.
 */
public class PostDetailFragment extends CommonDetailFragment<Post> {
    @Override
    protected String getCacheKey() {
        return "post_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        OSChinaApi.getPostDetail(mId, mDetailHeandler);
    }

    @Override
    protected Post parseData(InputStream is) {
        return XmlUtils.toBean(PostDetail.class, is).getPost();
    }

    @Override
    protected String getWebViewBody(Post detail) {
        StringBuffer body = new StringBuffer();
        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        body.append(ThemeSwitchUtils.getWebViewBodyString());
        // 添加title
        body.append(String.format("<div class='title'>%s</div>", mDetail.getTitle()));
        // 添加作者和时间
        String time = StringUtils.friendly_time(mDetail.getPubDate());
        String author = String.format("<a class='author' href='http://my.oschina.net/u/%s'>%s</a>", mDetail.getAuthorId(), mDetail.getAuthor());
        body.append(String.format("<div class='authortime'>%s&nbsp;&nbsp;&nbsp;&nbsp;%s</div>", author, time));
        // 添加图片点击放大支持
        body.append(UIHelper.setHtmlCotentSupportImagePreview(mDetail.getBody()));
        body.append(getPostTags(mDetail.getTags()));
        // 封尾
        body.append("</div></body>");
        return body.toString();
    }

    @SuppressWarnings("deprecation")
    private String getPostTags(Post.Tags taglist) {
        if (taglist == null)
            return "";
        StringBuffer tags = new StringBuffer();
        for (String tag : taglist.getTags()) {
            tags.append(String
                    .format("<a class='tag' href='http://www.oschina.net/question/tag/%s' >&nbsp;%s&nbsp;</a>&nbsp;&nbsp;",
                            URLEncoder.encode(tag), tag));
        }
        return String.format("<div style='margin-top:10px;'>%s</div>", tags);
    }

    @Override
    protected void showCommentView() {
        if (mDetail != null) {
            UIHelper.showComment(getActivity(), mId, CommentList.CATALOG_POST);
        }
    }

    @Override
    protected int getCommentType() {
        return CommentList.CATALOG_POST;
    }

    @Override
    protected String getShareTitle() {
        return mDetail.getTitle();
    }

    @Override
    protected String getShareContent() {
        return StringUtils.getSubString(0, 55,
                getFilterHtmlBody(mDetail.getBody()));
    }

    @Override
    protected String getShareUrl() {
        return  String.format(URLsUtils.URL_MOBILE + "question/%s_%s", mDetail.getAuthorId(), mId);
    }

    @Override
    protected int getFavoriteTargetType() {
        return FavoriteList.TYPE_POST;
    }

    @Override
    protected int getFavoriteState() {
        return mDetail.getFavorite();
    }

    @Override
    protected void updateFavoriteChanged(int newFavoritedState) {
        mDetail.setFavorite(newFavoritedState);
        saveCache(mDetail);
    }

    @Override
    protected int getCommentCount() {
        return mDetail.getAnswerCount();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailActivity) getActivity()).toolFragment.showReportButton();
    }

    @Override
    protected String getRepotrUrl() {
        return mDetail.getUrl();
    }
}
