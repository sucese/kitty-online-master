package com.guoxiaoxing.kitty.ui.fragment;

import android.text.Editable;
import android.text.TextUtils;

import com.guoxiaoxing.kitty.AppContext;
import net.oschina.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.base.CommonDetailFragment;
import com.guoxiaoxing.kitty.bean.Blog;
import com.guoxiaoxing.kitty.bean.BlogDetail;
import com.guoxiaoxing.kitty.bean.CommentList;
import com.guoxiaoxing.kitty.bean.FavoriteList;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.TDevice;
import com.guoxiaoxing.kitty.util.ThemeSwitchUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.URLsUtils;
import com.guoxiaoxing.kitty.util.XmlUtils;

import java.io.InputStream;

/**
 * Created by 火蚁 on 15/5/25.
 */
public class BlogDetailFragment extends CommonDetailFragment<Blog> {
    @Override
    protected String getCacheKey() {
        return "blog_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        OSChinaApi.getBlogDetail(mId, mDetailHeandler);
    }

    @Override
    protected Blog parseData(InputStream is) {
        return XmlUtils.toBean(BlogDetail.class, is).getBlog();
    }

    @Override
    protected String getWebViewBody(Blog detail) {
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
        // 封尾
        body.append("</div></body>");
        return body.toString();
    }

    @Override
    protected void showCommentView() {
        if (mDetail != null) {
            UIHelper.showBlogComment(getActivity(), mId,
                    mDetail.getAuthorId());
        }
    }

    @Override
    protected int getCommentType() {
        return CommentList.CATALOG_MESSAGE;
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
        return String.format(URLsUtils.URL_MOBILE + "blog/%s", mId);
    }

    @Override
    protected int getFavoriteTargetType() {
        return FavoriteList.TYPE_BLOG;
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
        return mDetail.getCommentCount();
    }

    @Override
    public void onClickSendButton(Editable str) {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        if (TextUtils.isEmpty(str)) {
            AppContext.showToastShort(R.string.tip_comment_content_empty);
            return;
        }
        showWaitDialog(R.string.progress_submit);
        OSChinaApi.publicBlogComment(mId, AppContext.getInstance()
                .getLoginUid(), str.toString(), mCommentHandler);
    }

    @Override
    protected String getRepotrUrl() {
        return mDetail.getUrl();
    }
}
