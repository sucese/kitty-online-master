package com.guoxiaoxing.kitty.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guoxiaoxing
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TweetsList extends BaseEntity implements ListEntity<UserTalk> {

    public final static int CATALOG_LATEST = 0;
    public final static int CATALOG_HOT = -1;
    public final static int CATALOG_ME = 1;

    @XStreamAlias("tweetcount")
    private int tweetCount;
    @XStreamAlias("pagesize")
    private int pagesize;
    @XStreamAlias("tweets")
    private List<UserTalk> tweetslist = new ArrayList<UserTalk>();

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public List<UserTalk> getTweetslist() {
        return tweetslist;
    }

    public void setTweetslist(List<UserTalk> tweetslist) {
        this.tweetslist = tweetslist;
    }

    @Override
    public List<UserTalk> getList() {
        return tweetslist;
    }

}
