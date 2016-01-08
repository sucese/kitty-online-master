package net.oschina.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.util.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 新闻、软件、帖子、博客实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月28日 上午10:16:59
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("news")
public class News extends Entity {
	
	public final static int NEWSTYPE_NEWS = 0x00;//0 新闻
	public final static int NEWSTYPE_SOFTWARE = 0x01;//1 软件
	public final static int NEWSTYPE_POST = 0x02;//2 帖子
	public final static int NEWSTYPE_BLOG = 0x03;//3 博客
	
	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("url")
	private String url;
	
	@XStreamAlias("body")
	private String body;
	
	@XStreamAlias("author")
	private String author;
	
	@XStreamAlias("authorid")
	private int authorId;
	
	@XStreamAlias("commentcount")
	private int commentCount;
	
	@XStreamAlias("pubdate")
	private String pubDate;
	
	@XStreamAlias("softwarelink")
	private String softwareLink;
	
	@XStreamAlias("softwarename")
	private String softwareName;
	
	@XStreamAlias("favorite")
	private int favorite;
	
	@XStreamAlias("newstype")
	private NewsType newsType;
	
	@XStreamAlias("relativies")
	private List<Relative> relatives = new ArrayList<Relative>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = StringUtils.toInt(authorId, 0);
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getSoftwareLink() {
		return softwareLink;
	}

	public void setSoftwareLink(String softwareLink) {
		this.softwareLink = softwareLink;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public NewsType getNewType() {
		return newsType;
	}

	public void setNewType(NewsType newType) {
		this.newsType = newType;
	}

	public List<Relative> getRelatives() {
		return relatives;
	}

	public void setRelatives(List<Relative> relatives) {
		this.relatives = relatives;
	}
	
	@XStreamAlias("newstype")
	public class NewsType implements Serializable{
		@XStreamAlias("type")
		private int type;
		@XStreamAlias("attachment")
		private String attachment;
		@XStreamAlias("authoruid2")
		private int authoruid2;
		@XStreamAlias("eventurl")
		private String eventUrl;
		
		public String getEventUrl() {
			return eventUrl;
		}
		public void setEventUrl(String eventUrl) {
			this.eventUrl = eventUrl;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getAttachment() {
			return attachment;
		}
		public void setAttachment(String attachment) {
			this.attachment = attachment;
		}
		public int getAuthoruid2() {
			return authoruid2;
		}
		public void setAuthoruid2(int authoruid2) {
			this.authoruid2 = authoruid2;
		}
	} 
	
	@XStreamAlias("relative")
	public class Relative implements Serializable{
		
		@XStreamAlias("rtitle")
		public String title;
		
		@XStreamAlias("rurl")
		public String url;
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	} 
}
