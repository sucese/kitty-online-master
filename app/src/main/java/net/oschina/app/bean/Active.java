package net.oschina.app.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 动态实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月22日 下午3:22:09
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("active")
public class Active extends Entity {

	public final static int CATALOG_OTHER = 0;// 其他
	public final static int CATALOG_NEWS = 1;// 新闻
	public final static int CATALOG_POST = 2;// 帖子
	public final static int CATALOG_TWEET = 3;// 动弹
	public final static int CATALOG_BLOG = 4;// 博客

	public final static int CLIENT_MOBILE = 2;
	public final static int CLIENT_ANDROID = 3;
	public final static int CLIENT_IPHONE = 4;
	public final static int CLIENT_WINDOWS_PHONE = 5;
	
	@XStreamAlias("portrait")
	private String portrait;

	@XStreamAlias("message")
	private String message;

	@XStreamAlias("author")
	private String author;

	@XStreamAlias("authorid")
	private int authorId;

	@XStreamAlias("activetype")
	private int activeType;

	@XStreamAlias("objectID")
	private int objectId;

	@XStreamAlias("catalog")
	private int catalog;

	@XStreamAlias("objecttype")
	private int objectType;

	@XStreamAlias("objectcatalog")
	private int objectCatalog;

	@XStreamAlias("objecttitle")
	private String objectTitle;

	@XStreamAlias("objectreply")
	private ObjectReply objectReply;

	@XStreamAlias("commentCount")
	private int commentCount;

	@XStreamAlias("pubDate")
	private String pubDate;

	@XStreamAlias("tweetimage")
	private String tweetimage;
	
	@XStreamAlias("tweetattach")
	private String tweetattach;

	@XStreamAlias("appclient")
	private int appClient;

	@XStreamAlias("url")
	private String url;

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public int getActiveType() {
		return activeType;
	}

	public void setActiveType(int activeType) {
		this.activeType = activeType;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public int getObjectCatalog() {
		return objectCatalog;
	}

	public void setObjectCatalog(int objectCatalog) {
		this.objectCatalog = objectCatalog;
	}

	public String getObjectTitle() {
		return objectTitle;
	}

	public void setObjectTitle(String objectTitle) {
		this.objectTitle = objectTitle;
	}

	public ObjectReply getObjectReply() {
		return objectReply;
	}

	public void setObjectReply(ObjectReply objectReply) {
		this.objectReply = objectReply;
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

	public String getTweetattach() {
		return tweetattach;
	}

	public void setTweetattach(String tweetattach) {
		this.tweetattach = tweetattach;
	}

	public String getTweetimage() {
		return tweetimage;
	}

	public void setTweetimage(String tweetimage) {
		this.tweetimage = tweetimage;
	}

	public int getAppClient() {
		return appClient;
	}

	public void setAppClient(int appClient) {
		this.appClient = appClient;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XStreamAlias("objectreply")
	public static class ObjectReply implements Serializable {
		@XStreamAlias("objectname")
		public String objectName;

		@XStreamAlias("objectbody")
		public String objectBody;

		public String getObjectName() {
			return objectName;
		}

		public void setObjectName(String objectName) {
			this.objectName = objectName;
		}

		public String getObjectBody() {
			return objectBody;
		}

		public void setObjectBody(String objectBody) {
			this.objectBody = objectBody;
		}
	}
}
