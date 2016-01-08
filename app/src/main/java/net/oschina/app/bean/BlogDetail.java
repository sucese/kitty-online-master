package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 博客详情
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月15日 上午10:51:11
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class BlogDetail extends Entity {
	
	@XStreamAlias("blog")
	private Blog blog;

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}
}
