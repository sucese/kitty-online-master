package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 搜索结果实体类
 */
@SuppressWarnings("serial")
@XStreamAlias("result")
public class SearchResult extends Entity {
	
	@XStreamAlias("objid")
	private int id;
	
	@XStreamAlias("type")
	private String type;
	
	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("description")
	private String description;
	
	@XStreamAlias("url")
	private String url;
	
	@XStreamAlias("pubDate")
	private String pubDate;
	
	@XStreamAlias("author")
	private String author;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
