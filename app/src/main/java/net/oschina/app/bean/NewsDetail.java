package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * 资讯详情
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月11日 下午3:28:33 
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class NewsDetail extends Entity {
	
	@XStreamAlias("news")
	private News news;

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}
}
