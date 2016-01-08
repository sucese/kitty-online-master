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
public class FriendsList extends Entity implements ListEntity<Friend> {

    public final static int TYPE_FANS = 0x00;
    public final static int TYPE_FOLLOWER = 0x01;

    @XStreamAlias("friends")
    private List<Friend> friendlist = new ArrayList<Friend>();

    public List<Friend> getFriendlist() {
	return friendlist;
    }

    public void setFriendlist(List<Friend> resultlist) {
	this.friendlist = resultlist;
    }

    @Override
    public List<Friend> getList() {
	return friendlist;
    }
}
