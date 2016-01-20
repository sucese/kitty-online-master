package com.guoxiaoxing.kitty.team.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import com.guoxiaoxing.kitty.bean.BaseEntity;
import com.guoxiaoxing.kitty.bean.ListEntity;

@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TeamDiscussList extends BaseEntity implements ListEntity<TeamDiscuss> {
	
	@XStreamAlias("pagesize")
	private int pageSize;
	
	@XStreamAlias("totalCount")
	private int totalCount;
	
	@XStreamAlias("discusses")
	private List<TeamDiscuss> list = new ArrayList<TeamDiscuss>();

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<TeamDiscuss> getList() {
		return list;
	}

	public void setList(List<TeamDiscuss> list) {
		this.list = list;
	}
	
}
