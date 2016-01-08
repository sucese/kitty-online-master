package net.oschina.app.team.bean;

import java.io.Serializable;
import java.util.List;

import net.oschina.app.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Reply.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 * 
 * @data 2015-1-30 下午3:58:27
 */
@SuppressWarnings("serial")
@XStreamAlias("reply")
public class TeamReply extends Entity {

    public final static String REPLY_TYPE_ISSUE = "issue";
    public final static String REPLY_TYPE_DIARY = "diary";
    public final static String REPLY_TYPE_DISCUSS = "discuss";

    public final static int CLIENT_MOBILE = 2;
    public final static int CLIENT_ANDROID = 3;
    public final static int CLIENT_IPHONE = 4;
    public final static int CLIENT_WINDOWS_PHONE = 5;

    public final static byte REPLY_PUB_TYPE_ACTIVE = 110;
    public final static byte REPLY_PUB_TYPE_ISSUE = 112;
    public final static byte REPLY_PUB_TYPE_DIARY = 118;
    public final static byte REPLY_PUB_TYPE_SHARE = 114;

    @XStreamAlias("content")
    private String content;

    @XStreamAlias("createTime")
    private String createTime;

    @XStreamAlias("author")
    private Author author;

    @XStreamAlias("appclient")
    private int appClient;

    @XStreamAlias("appName")
    private String appName;
    
    @XStreamAlias("replies")
    private List<TeamReply> replies;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppClient() {
        return appClient;
    }

    public void setAppClient(int appClient) {
        this.appClient = appClient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
    
    public List<TeamReply> getReplies() {
        return replies;
    }

    public void setReplies(List<TeamReply> replies) {
        this.replies = replies;
    }
}
