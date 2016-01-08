package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 好友实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月6日 上午11:17:36
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class FindUserList extends Entity implements ListEntity<User> {

	public final static int TYPE_FANS = 0x00;
	public final static int TYPE_FOLLOWER = 0x01;
	
	@XStreamAlias("users")
	private List<User> friendlist = new ArrayList<User>();

	public List<User> getFriendlist() {
		return friendlist;
	}

	public void setFriendlist(List<User> resultlist) {
		this.friendlist = resultlist;
	}

	@Override
	public List<User> getList() {
		return friendlist;
	}
}
