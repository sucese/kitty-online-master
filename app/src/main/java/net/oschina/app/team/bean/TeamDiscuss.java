package net.oschina.app.team.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.oschina.app.bean.Entity;

@SuppressWarnings("serial")
@XStreamAlias("discuss")
public class TeamDiscuss extends Entity {

    @XStreamAlias("type")
    private String type;

    @XStreamAlias("title")
    private String title;

    @XStreamAlias("body")
    private String body;

    @XStreamAlias("createTime")
    private String createTime;

    @XStreamAlias("answerCount")
    private int answerCount;// 回复数量

    @XStreamAlias("voteUp")
    private int voteUp;// 点赞数

    @XStreamAlias("author")
    private Author author;

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

    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public int getAnswerCount() {
	return answerCount;
    }

    public void setAnswerCount(int answerCount) {
	this.answerCount = answerCount;
    }

    public int getVoteUp() {
	return voteUp;
    }

    public void setVoteUp(int voteUp) {
	this.voteUp = voteUp;
    }

    public Author getAuthor() {
	return author;
    }

    public void setAuthor(Author author) {
	this.author = author;
    }
}
