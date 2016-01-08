package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 留言实体类列表
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月22日 下午4:38:49
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class MessageList extends Entity implements ListEntity<Messages> {

	@XStreamAlias("pagesize")
	private int pageSize;
	
	@XStreamAlias("messageCount")
	private int messageCount;
	
	@XStreamAlias("messages")
	private List<Messages> messagelist = new ArrayList<Messages>();

	public int getPageSize() {
		return pageSize;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public List<Messages> getMessagelist() {
		return messagelist;
	}

	@Override
	public List<Messages> getList() {
		return messagelist;
	}
}
