package net.oschina.app.team.bean;

import net.oschina.app.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("oschina")
public class TeamDiaryDetailBean extends Entity {

    @XStreamAlias("diary")
    private TeamDiary teamDiary;

    public TeamDiary getTeamDiary() {
        return teamDiary;
    }

    public void setTeamDiary(TeamDiary teamDiary) {
        this.teamDiary = teamDiary;
    }

}
