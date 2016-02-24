package com.guoxiaoxing.kitty.model;

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
public class TweetLike extends BaseObject {
    
    @XStreamAlias("user")
    private User user;
    
    @XStreamAlias("userTweet")
    private UserTweet userTweet;
    
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

    public UserTweet getUserTweet() {
        return userTweet;
    }

    public void setUserTweet(UserTweet userTweet) {
        this.userTweet = userTweet;
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

