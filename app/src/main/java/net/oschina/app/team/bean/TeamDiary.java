package net.oschina.app.team.bean;

import net.oschina.app.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("diary")
public class TeamDiary extends Entity {

    @XStreamAlias("title")
    private String title;

    @XStreamAlias("reply")
    private int reply;

    @XStreamAlias("createTime")
    private String createTime;

    @XStreamAlias("author")
    private Author author;

    @XStreamAlias("detail")
    private TeamDiaryDetail teamDiaryDetail;

    public TeamDiaryDetail getTeamDiaryDetail() {
        return teamDiaryDetail;
    }

    public void setTeamDiaryDetail(TeamDiaryDetail teamDiaryDetail) {
        this.teamDiaryDetail = teamDiaryDetail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

}
