package com.guoxiaoxing.kitty.team.bean;

import com.guoxiaoxing.kitty.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Team模块的动态JavaBean
 * 
 * @author kymjs
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TeamActiveDetail extends Entity {

    @XStreamAlias("active")
    private TeamActive teamActive;

    public TeamActive getTeamActive() {
        return teamActive;
    }

    public void setTeamActive(TeamActive teamActive) {
        this.teamActive = teamActive;
    }
}