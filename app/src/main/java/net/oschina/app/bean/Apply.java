package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 活动报名者实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月16日 下午3:27:04 
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("apply")
public class Apply extends Entity {
	
	@XStreamAlias("uid")
	private int userid;
	
	@XStreamAlias("name")
	private String name;
	
	@XStreamAlias("portrait")
	private String portrait;
	
	@XStreamAlias("company")
	private String company;
	
	@XStreamAlias("job")
	private String job;
	
	public int getId() {
		return userid;
	}
	public void setId(int userid) {
		this.userid = userid;
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
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
}

