package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 活动实体类列表
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年12月10日 下午2:28:54
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class EventList extends Entity implements ListEntity<Event> {

	public final static int EVENT_LIST_TYPE_NEW_EVENT = 0X00;// 近期活动

	public final static int EVENT_LIST_TYPE_MY_EVENT = 0X01;// 我的活动

	@XStreamAlias("events")
	private List<Event> friendlist = new ArrayList<Event>();

	public List<Event> getFriendlist() {
		return friendlist;
	}

	public void setFriendlist(List<Event> resultlist) {
		this.friendlist = resultlist;
	}

	@Override
	public List<Event> getList() {
		return friendlist;
	}
}
