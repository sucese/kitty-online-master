package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author HuangWenwei
 *
 * @date 2014年10月16日
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TweetDetail extends Entity {
	
	@XStreamAlias("tweet")
	private Tweet tweet;

	public Tweet getTweet() {
		return tweet;
	}
	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}
}
