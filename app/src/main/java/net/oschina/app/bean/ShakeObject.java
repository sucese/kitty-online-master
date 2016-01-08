package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("oschina")
public class ShakeObject {

    @XStreamAlias("randomtype")
    private String randomtype; // 数据类型
    @XStreamAlias("id")
    private String id; // 数据id
    @XStreamAlias("title")
    private String title; // 帖子标题
    @XStreamAlias("detail")
    private String detail; // 内容
    @XStreamAlias("author")
    private String author; // 作者
    @XStreamAlias("authorid")
    private String authorid; // 作者id
    @XStreamAlias("image")
    private String image; // 头像地址
    @XStreamAlias("pubDate")
    private String pubDate; // 收录日期
    @XStreamAlias("commentCount")
    private String commentCount;
    @XStreamAlias("url")
    private String url;

    public static final String RANDOMTYPE_NEWS = "1";
    public static final String RANDOMTYPE_BLOG = "2";
    public static final String RANDOMTYPE_SOFTWARE = "3";

    public String getRandomtype() {
        return randomtype;
    }

    public void setRandomtype(String randomtype) {
        this.randomtype = randomtype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
