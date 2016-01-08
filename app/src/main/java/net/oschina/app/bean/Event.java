package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 活动实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年12月12日 下午3:18:08
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("event")
public class Event extends Entity {
	
	public final static int EVNET_STATUS_APPLYING = 0x02;
	public final static int EVNET_STATUS_END = 0x01;
	
	public final static int APPLYSTATUS_CHECKING = 0x00;// 审核中
	public final static int APPLYSTATUS_CHECKED = 0x01;// 已经确认
	public final static int APPLYSTATUS_ATTEND = 0x02;// 已经出席
	public final static int APPLYSTATUS_CANCLE = 0x03;// 已取消
	public final static int APPLYSTATUS_REJECT = 0X04;// 已拒绝
	
	@XStreamAlias("cover")
	private String cover;
	
	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("url")
	private String url;
	
	@XStreamAlias("createTime")
	private String createTime;
	
	@XStreamAlias("startTime")
	private String startTime;
	
	@XStreamAlias("endTime")
	private String endTime;
	
	@XStreamAlias("spot")
	private String spot;
	
	@XStreamAlias("actor_count")
	private int actor_count;
	
	@XStreamAlias("location")
	private String location;
	
	@XStreamAlias("city")
	private String city;
	
	@XStreamAlias("status")
	private int status;
	
	@XStreamAlias("applyStatus")
	private int applyStatus;
	
	@XStreamAlias("category")
	private int category;// 活动类型 1源创会 2技术交流 3其他 4站外活动

	@XStreamAlias("remark")
	private EventRemark eventRemark;

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSpot() {
		return spot;
	}

	public void setSpot(String spot) {
		this.spot = spot;
	}

	public int getActor_count() {
		return actor_count;
	}

	public void setActor_count(int actor_count) {
		this.actor_count = actor_count;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(int applyStatus) {
		this.applyStatus = applyStatus;
	}

	public EventRemark getEventRemark() {
		return eventRemark;
	}

	public void setEventRemark(EventRemark eventRemark) {
		this.eventRemark = eventRemark;
	}
}
