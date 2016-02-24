package com.guoxiaoxing.kitty.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guoxiaoxing
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TweetsList extends BaseObject implements ListEntity<UserTweet> {

    public final static int CATALOG_LATEST = 0;
    public final static int CATALOG_HOT = -1;
    public final static int CATALOG_ME = 1;

    @XStreamAlias("tweetcount")
    private int tweetCount;
    @XStreamAlias("pagesize")
    private int pagesize;
    @XStreamAlias("tweets")
    private List<UserTweet> tweetslist = new ArrayList<UserTweet>();

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

    public List<UserTweet> getTweetslist() {
        return tweetslist;
    }

    public void setTweetslist(List<UserTweet> tweetslist) {
        this.tweetslist = tweetslist;
    }

    @Override
    public List<UserTweet> getList() {
        return tweetslist;
    }

}
