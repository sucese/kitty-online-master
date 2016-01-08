package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 点赞消息实体类
 * LikeTweet.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-4-10 上午10:09:15
 */
@SuppressWarnings("serial")
@XStreamAlias("mytweet")
public class TweetLike extends Entity {
    
    @XStreamAlias("user")
    private User user;
    
    @XStreamAlias("tweet")
    private Tweet tweet;
    
    @XStreamAlias("datatime")
    private String datatime;
    
    @XStreamAlias("appclient")
    private int appClient;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public int getAppClient() {
        return appClient;
    }

    public void setAppClient(int appClient) {
        this.appClient = appClient;
    }
}

