package com.guoxiaoxing.kitty.model;

import com.avos.avoscloud.AVObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/** 
 * 我的资料实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月30日 下午4:08:30 
 * 
 */

@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class MyInformation extends AVObject implements Serializable {
	
	@XStreamAlias("user")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
