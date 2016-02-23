package com.guoxiaoxing.kitty.ui.fragment;

import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.ui.base.CommonDetailFragment;
import com.guoxiaoxing.kitty.model.CommentList;
import com.guoxiaoxing.kitty.model.FavoriteList;
import com.guoxiaoxing.kitty.model.News;
import com.guoxiaoxing.kitty.model.NewsDetail;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.ThemeSwitchUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;

import java.io.InputStream;

/**
 * Created by 火蚁 on 15/5/25.
 */
public class NewsDetailFragment extends CommonDetailFragment<News> {

    @Override
    protected String getCacheKey() {
        return "news_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        OSChinaApi.getNewsDetail(mId, mDetailHeandler);
    }

    @Override
    protected News parseData(InputStream is) {
        return XmlUtils.toBean(NewsDetail.class, is).getNews();
    }

    @Override
    protected String getWebViewBody(News detail) {
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


        // 更多关于***软件的信息
        String softwareName = mDetail.getSoftwareName();
        String softwareLink = mDetail.getSoftwareLink();
        if (!StringUtils.isEmpty(softwareName)
                && !StringUtils.isEmpty(softwareLink))
            body.append(String
                    .format("<div class='oschina_software' style='margin-top:8px;font-weight:bold'>更多关于:&nbsp;<a href='%s'>%s</a>&nbsp;的详细信息</div>",
                            softwareLink, softwareName));

        // 相关新闻
        if (mDetail != null && mDetail.getRelatives() != null
                && mDetail.getRelatives().size() > 0) {
            String strRelative = "";
            for (News.Relative relative : mDetail.getRelatives()) {
                strRelative += String.format(
                        "<li><a href='%s' style='text-decoration:none'>%s</a></li>",
                        relative.url, relative.title);
            }
            body.append("<p/><div style=\"height:1px;width:100%;background:#DADADA;margin-bottom:10px;\"/>"
                    + String.format("<br/> <b>相关资讯</b><ul class='about'>%s</ul>",
                    strRelative));
        }
        body.append("<br/>");
        // 封尾
        body.append("</div></body>");
        return  body.toString();
    }

    @Override
    protected void showCommentView() {
        if (mDetail != null)
            UIHelper.showComment(getActivity(), mId,
                    CommentList.CATALOG_NEWS);
    }

    @Override
    protected int getCommentType() {
        return CommentList.CATALOG_NEWS;
    }

    @Override
    protected int getFavoriteTargetType() {
        return FavoriteList.TYPE_NEWS;
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
        return mDetail.getUrl().replace("http://www", "http://m");
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
}
