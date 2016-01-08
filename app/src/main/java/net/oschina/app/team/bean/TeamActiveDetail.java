package net.oschina.app.team.bean;

import net.oschina.app.bean.Entity;

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
