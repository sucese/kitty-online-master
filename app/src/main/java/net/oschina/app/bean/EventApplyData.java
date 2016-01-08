package net.oschina.app.bean;

/**
 * 活动报名实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年12月17日 下午3:13:07
 * 
 */
@SuppressWarnings("serial")
public class EventApplyData extends Entity {
	
	private int event;// 活动的id
	
	private int user;// 用户的id
	
	private String name;// 名字
	
	private String gender;// 性别
	
	private String mobile;// 电话
	
	private String company;// 单位名称
	
	private String job;// 职业名称

	private String remark;// 备注

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return mobile;
	}

	public void setPhone(String phone) {
		this.mobile = phone;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
