package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 好友实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月6日 上午11:37:31
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("friend")
public class Friend extends Entity {
	
    @XStreamAlias("userid")
    private int userid;
    
    @XStreamAlias("name")
    private String name;
    
    @XStreamAlias("from")
    private String from;
    
    @XStreamAlias("portrait")
    private String portrait;
    
    @XStreamAlias("expertise")
    private String expertise;
    
    @XStreamAlias("gender")
    private int gender;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
