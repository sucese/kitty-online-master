package net.oschina.app.team.bean;

import net.oschina.app.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 我的任务中要显示的状态
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class MyIssueState extends Entity {
    @XStreamAlias("opened")
    private String opened; // 待办中
    @XStreamAlias("outdate")
    private String outdate; // 已过期
    @XStreamAlias("closed")
    private String closed; // 已完成
    @XStreamAlias("finished")
    private String finished; // 已完成 + 已验收
    @XStreamAlias("underway")
    private String underway; // 进行中
    @XStreamAlias("accepted")
    private String accepted; // 已验收
    @XStreamAlias("all")
    private String all;
    
    @XStreamAlias("member")
    private TeamMember user;

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getUnderway() {
        return underway;
    }

    public void setUnderway(String underway) {
        this.underway = underway;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getOutdate() {
        return outdate;
    }

    public void setOutdate(String outdate) {
        this.outdate = outdate;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public TeamMember getUser() {
        return user;
    }

    public void setUser(TeamMember user) {
        this.user = user;
    }
}
