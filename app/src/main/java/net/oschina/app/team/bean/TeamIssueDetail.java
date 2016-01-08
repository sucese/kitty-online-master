package net.oschina.app.team.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.oschina.app.bean.Entity;

/**
 * 团队任务实体类
 * 
 * TeamIssueDetail.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 * 
 * @data 2015-1-27 下午7:44:38
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TeamIssueDetail extends Entity {

    @XStreamAlias("issue")
    private TeamIssue teamIssue;

    public TeamIssue getTeamIssue() {
        return teamIssue;
    }

    public void setTeamIssue(TeamIssue teamIssue) {
        this.teamIssue = teamIssue;
    }

}
