package com.guoxiaoxing.kitty.bean;

import com.avos.avoscloud.AVObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.List;

/** 
 * 个人信息专用实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月29日 上午10:53:54 
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class UserInformation extends AVObject implements Serializable {
	
	@XStreamAlias("user")
	private User user;
	
	@XStreamAlias("pagesize")
	private int pageSize;
	
	@XStreamAlias("activies")
	private List<UserActive> userActiveList;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<UserActive> getUserActiveList() {
		return userActiveList;
	}

	public void setUserActiveList(List<UserActive> userActiveList) {
		this.userActiveList = userActiveList;
	}
}