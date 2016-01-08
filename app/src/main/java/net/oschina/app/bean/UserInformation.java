package net.oschina.app.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * 个人信息专用实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月29日 上午10:53:54 
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class UserInformation extends Base {
	
	@XStreamAlias("user")
	private User user;
	
	@XStreamAlias("pagesize")
	private int pageSize;
	
	@XStreamAlias("activies")
	private List<Active> activeList;

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

	public List<Active> getActiveList() {
		return activeList;
	}

	public void setActiveList(List<Active> activeList) {
		this.activeList = activeList;
	}
}