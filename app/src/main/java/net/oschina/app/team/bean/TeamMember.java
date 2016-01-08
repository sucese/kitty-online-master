package net.oschina.app.team.bean;

import net.oschina.app.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Team成员
 * 
 * @author kymjs
 * 
 */
@XStreamAlias("member")
public class TeamMember extends Entity {
    private static final long serialVersionUID = 1L;

    @XStreamAlias("id")
    private int id;
    @XStreamAlias("name")
    private String name;
    @XStreamAlias("oscName")
    private String oscName;
    @XStreamAlias("portrait")
    private String portrait;
    @XStreamAlias("gender")
    private String gender;
    @XStreamAlias("teamEmail")
    private String teamEmail;
    @XStreamAlias("teamTelephone")
    private String teamTelephone;
    @XStreamAlias("teamJob")
    private String teamJob;
    @XStreamAlias("teamRole")
    private int teamRole;
    @XStreamAlias("space")
    private String space;
    @XStreamAlias("joinTime")
    private String joinTime;
    @XStreamAlias("location")
    private String location;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(int teamRole) {
        this.teamRole = teamRole;
    }

    public String getOscName() {
        return oscName;
    }

    public void setOscName(String oscName) {
        this.oscName = oscName;
    }

    public String getTeamEmail() {
        return teamEmail;
    }

    public void setTeamEmail(String teamEmail) {
        this.teamEmail = teamEmail;
    }

    public String getTeamTelephone() {
        return teamTelephone;
    }

    public void setTeamTelephone(String teamTelephone) {
        this.teamTelephone = teamTelephone;
    }

    public String getTeamJob() {
        return teamJob;
    }

    public void setTeamJob(String teamJob) {
        this.teamJob = teamJob;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
