package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年9月27日 下午2:45:57 
 * 
 */

@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class LoginUserBean extends Entity {
	
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