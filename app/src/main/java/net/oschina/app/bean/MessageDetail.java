package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 聊天详细信息实体类
 * @author 铂金小鸟（http://my.oschina.net/fants）
 * @Created 2015年9月16日 上午4:20:01
 */
@SuppressWarnings("serial")
@XStreamAlias("message")
public class MessageDetail extends Entity  {

    //是否显示时间
    private boolean showDate;
    //消息状态
    private MessageStatus status;

    @XStreamAlias("portrait")
    private String portrait;

    @XStreamAlias("author")
    private String author;

    @XStreamAlias("authorid")
    private int authorId;

    @XStreamAlias("content")
    private String content;

    @XStreamAlias("fileName")
    private String fileName;

    @XStreamAlias("btype")
    private int btype;

    @XStreamAlias("pubDate")
    private String pubDate;

    public enum MessageStatus{
        NORMAL,SENDING,ERROR
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

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
