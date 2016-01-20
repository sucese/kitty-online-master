package com.guoxiaoxing.kitty.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author guoxiaoxing
 */

@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class LoginUserBean extends BaseEntity {
	
	@XStreamAlias("result")
	private Result result;
	
	@XStreamAlias("user")
	private User user;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}