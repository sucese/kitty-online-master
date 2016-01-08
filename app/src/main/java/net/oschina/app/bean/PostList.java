package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 帖子实体类列表
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月9日 下午6:10:11
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class PostList extends Entity implements ListEntity<Post> {
	
	public final static String PREF_READED_POST_LIST = "readed_post_list.pref";

	@XStreamAlias("pagesize")
	private int pageSize;
	
	@XStreamAlias("postCount")
	private int postCount;
	
	@XStreamAlias("posts")
	private List<Post> postlist = new ArrayList<Post>();
	
	public int getPageSize() {
		return pageSize;
	}
	public int getPostCount() {
		return postCount;
	}
	public List<Post> getPostlist() {
		return postlist;
	}
	@Override
	public List<Post> getList() {
		return postlist;
	}
}
