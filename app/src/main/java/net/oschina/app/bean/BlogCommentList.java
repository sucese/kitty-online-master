package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 博客评论列表实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月17日 上午10:28:04
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class BlogCommentList extends Entity implements ListEntity<Comment> {
	
	@XStreamAlias("pagesize")
	private int pageSize;
	@XStreamAlias("allCount")
	private int allCount;
	@XStreamAlias("comments")
	private List<Comment> commentlist = new ArrayList<Comment>();

	public int getPageSize() {
		return pageSize;
	}

	public int getAllCount() {
		return allCount;
	}

	public List<Comment> getCommentlist() {
		return commentlist;
	}

	@Override
	public List<Comment> getList() {
		return commentlist;
	}
}
