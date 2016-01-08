package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 搜索实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年12月5日 上午11:19:44
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class SearchList extends Entity implements ListEntity<SearchResult> {

	public final static String CATALOG_ALL = "all";
	public final static String CATALOG_NEWS = "news";
	public final static String CATALOG_POST = "post";
	public final static String CATALOG_SOFTWARE = "software";
	public final static String CATALOG_BLOG = "blog";

	@XStreamAlias("pagesize")
	private int pageSize;

	@XStreamAlias("results")
	private List<SearchResult> list = new ArrayList<SearchResult>();

	public int getPageSize() {
		return pageSize;
	}

	@Override
	public List<SearchResult> getList() {
		return list;
	}

	public void setList(List<SearchResult> list) {
		this.list = list;
	}
}
