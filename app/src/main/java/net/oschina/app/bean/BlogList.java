package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author HuangWenwei
 *
 * @date 2014年9月28日
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class BlogList extends Entity implements ListEntity<Blog> {
	
	public final static String PREF_READED_BLOG_LIST = "readed_blog_list.pref";
	
	public static final String CATALOG_LATEST = "latest";
	public static final String  CATALOG_RECOMMEND = "recommend";
	
	@XStreamAlias("pagesize")
	private int pagesize;
	
	@XStreamAlias("blogs")
	private List<Blog> bloglist = new ArrayList<Blog>();
	
	@XStreamAlias("blogsCount")
	private int blogsCount;

	public int getPageSize() {
		return pagesize;
	}

	public void setPageSize(int pageSize) {
		this.pagesize = pageSize;
	}

	public List<Blog> getBloglist() {
		return bloglist;
	}

	public void setBloglist(List<Blog> bloglist) {
		this.bloglist = bloglist;
	}

	@Override
	public List<Blog> getList() {
		return bloglist;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getBlogsCount() {
		return blogsCount;
	}

	public void setBlogsCount(int blogsCount) {
		this.blogsCount = blogsCount;
	}
}
