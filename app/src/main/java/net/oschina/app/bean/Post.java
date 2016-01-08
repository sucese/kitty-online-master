package net.oschina.app.bean;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 帖子实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月9日 下午6:02:47
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("post")
public class Post extends Entity {

	public final static int CATALOG_ASK = 1;
	public final static int CATALOG_SHARE = 2;
	public final static int CATALOG_OTHER = 3;
	public final static int CATALOG_JOB = 4;
	public final static int CATALOG_SITE = 5;

	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("portrait")
	private String portrait;

	@XStreamAlias("url")
	private String url;

	@XStreamAlias("body")
	private String body;

	@XStreamAlias("author")
	private String author;

	@XStreamAlias("authorid")
	private int authorId;

	@XStreamAlias("answerCount")
	private int answerCount;

	@XStreamAlias("viewCount")
	private int viewCount;

	@XStreamAlias("pubDate")
	private String pubDate;

	@XStreamAlias("catalog")
	private int catalog;

	@XStreamAlias("isnoticeme")
	private int isNoticeMe;

	@XStreamAlias("favorite")
	private int favorite;

	@XStreamAlias("tags")
	private Tags tags;
	
	@XStreamAlias("answer")
	private Answer answer;
	
	@XStreamAlias("event")
	private Event event;

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
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

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public int getIsNoticeMe() {
		return isNoticeMe;
	}

	public void setIsNoticeMe(int isNoticeMe) {
		this.isNoticeMe = isNoticeMe;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public Post.Tags getTags() {
		return tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	
	@XStreamAlias("answer")
	public class Answer implements Serializable {
		
		@XStreamAlias("name")
		private String name;
		
		@XStreamAlias("time")
		private String time;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}
	
	public class Tags implements Serializable {
		@XStreamImplicit(itemFieldName="tag")
		private List<String> tags;

		public List<String> getTags() {
			return tags;
		}
		
		public void setTags(List<String> tags) {
			this.tags = tags;
		}
	}
}
