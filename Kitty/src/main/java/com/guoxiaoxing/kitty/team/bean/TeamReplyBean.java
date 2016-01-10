package com.guoxiaoxing.kitty.team.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import com.guoxiaoxing.kitty.bean.Entity;

/**
 * TeamReplyBean.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-3-13 下午6:32:23
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TeamReplyBean extends Entity {
    
    @XStreamAlias("reply")
    private TeamReply teamReply;

    public TeamReply getTeamReply() {
        return teamReply;
    }

    public void setTeamReply(TeamReply teamReply) {
        this.teamReply = teamReply;
    }
    
}

