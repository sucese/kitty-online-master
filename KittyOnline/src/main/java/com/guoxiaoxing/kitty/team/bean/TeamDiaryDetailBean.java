package com.guoxiaoxing.kitty.team.bean;

import com.guoxiaoxing.kitty.bean.BaseEntity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("oschina")
public class TeamDiaryDetailBean extends BaseEntity {

    @XStreamAlias("diary")
    private TeamDiary teamDiary;

    public TeamDiary getTeamDiary() {
        return teamDiary;
    }

    public void setTeamDiary(TeamDiary teamDiary) {
        this.teamDiary = teamDiary;
    }

}
