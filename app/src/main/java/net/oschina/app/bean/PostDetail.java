package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * 帖子详情
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月11日 下午3:28:33 
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class PostDetail extends Entity {
	
	@XStreamAlias("post")
	private Post post;

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

}
