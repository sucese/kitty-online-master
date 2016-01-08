package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 活动参与者列表实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年12月12日 下午8:06:30
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class EventAppliesList extends Entity implements ListEntity<Apply> {

	public final static int TYPE_FANS = 0x00;
	public final static int TYPE_FOLLOWER = 0x01;
	
	@XStreamAlias("applies")
	private List<Apply> list = new ArrayList<Apply>();

	@Override
	public List<Apply> getList() {
		return list;
	}

	public void setList(List<Apply> list) {
		this.list = list;
	}
}
