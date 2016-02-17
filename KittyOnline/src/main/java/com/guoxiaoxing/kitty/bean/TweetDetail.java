package com.guoxiaoxing.kitty.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author HuangWenwei
 *
 * @date 2014年10月16日
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TweetDetail extends BaseEntity {
	
	@XStreamAlias("userTalk")
	private UserTalk userTalk;

	public UserTalk getUserTalk() {
		return userTalk;
	}
	public void setUserTalk(UserTalk userTalk) {
		this.userTalk = userTalk;
	}
}
