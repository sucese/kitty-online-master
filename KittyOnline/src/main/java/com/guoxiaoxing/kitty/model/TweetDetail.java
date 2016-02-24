package com.guoxiaoxing.kitty.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author HuangWenwei
 *
 * @date 2014年10月16日
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TweetDetail extends BaseObject {
	
	@XStreamAlias("userTweet")
	private UserTweet userTweet;

	public UserTweet getUserTweet() {
		return userTweet;
	}
	public void setUserTweet(UserTweet userTweet) {
		this.userTweet = userTweet;
	}
}
