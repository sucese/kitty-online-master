package com.guoxiaoxing.kitty.team.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import com.guoxiaoxing.kitty.bean.BaseObject;

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
public class TeamIssueDetail extends BaseObject {

    @XStreamAlias("issue")
    private TeamIssue teamIssue;

    public TeamIssue getTeamIssue() {
        return teamIssue;
    }

    public void setTeamIssue(TeamIssue teamIssue) {
        this.teamIssue = teamIssue;
    }

}
