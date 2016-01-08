package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 软件实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月23日 下午3:03:25
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("software")
public class Software extends Entity {
	
	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("author")
	private String author;
	
	@XStreamAlias("authorid")
	private int authorId;
	
	@XStreamAlias("recommended")
	private int recommended;
	
	@XStreamAlias("extensiontitle")
	private String extensionTitle;

	@XStreamAlias("license")
	private String license;

	@XStreamAlias("body")
	private String body;

	@XStreamAlias("homepage")
	private String homepage;

	@XStreamAlias("document")
	private String document;

	@XStreamAlias("download")
	private String download;

	@XStreamAlias("logo")
	private String logo;

	@XStreamAlias("language")
	private String language;

	@XStreamAlias("os")
	private String os;

	@XStreamAlias("recordtime")
	private String recordtime;

	@XStreamAlias("url")
	private String url;

	@XStreamAlias("favorite")
	private int favorite;

	@XStreamAlias("tweetCount")
	private int tweetCount;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public int getRecommended() {
		return recommended;
	}

	public void setRecommended(int recommended) {
		this.recommended = recommended;
	}

	public String getExtensionTitle() {
		return extensionTitle;
	}

	public void setExtensionTitle(String extensionTitle) {
		this.extensionTitle = extensionTitle;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getRecordtime() {
		return recordtime;
	}

	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public int getTweetCount() {
		return tweetCount;
	}

	public void setTweetCount(int tweetCount) {
		this.tweetCount = tweetCount;
	}
}
