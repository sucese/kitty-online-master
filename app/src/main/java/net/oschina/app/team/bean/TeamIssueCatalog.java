package net.oschina.app.team.bean;

import net.oschina.app.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 项目任务列表实体类 即指定项目下的任务列表
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月15日 上午10:40:09
 * 
 */

@SuppressWarnings("serial")
@XStreamAlias("catalog")
public class TeamIssueCatalog extends Entity {

    @XStreamAlias("title")
    private String title;

    @XStreamAlias("archive")
    private int archive;// 1:归档;0:未归档
    
    @XStreamAlias("description")
    private String description;// 描述

    @XStreamAlias("openedIssueCount")
    private int openedIssueCount;// 未完成的任务数量

    @XStreamAlias("closedIssueCount")
    private int closedIssueCount;// 已完成的任务数量

    @XStreamAlias("allIssueCount")
    private int allIssueCount;// 所有任务数量

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public int getArchive() {
	return archive;
    }

    public void setArchive(int archive) {
	this.archive = archive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOpenedIssueCount() {
	return openedIssueCount;
    }

    public void setOpenedIssueCount(int openedIssueCount) {
	this.openedIssueCount = openedIssueCount;
    }

    public int getClosedIssueCount() {
	return closedIssueCount;
    }

    public void setClosedIssueCount(int closedIssueCount) {
	this.closedIssueCount = closedIssueCount;
    }

    public int getAllIssueCount() {
	return allIssueCount;
    }

    public void setAllIssueCount(int allIssueCount) {
	this.allIssueCount = allIssueCount;
    }
}
