package net.oschina.app.team.bean;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.oschina.app.bean.Entity;
import net.oschina.app.bean.ListEntity;

/**
 * 团队任务列表实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月14日 下午5:09:11
 * 
 */
@SuppressWarnings({ "serial" })
@XStreamAlias("oschina")
public class TeamIssueList extends Entity implements ListEntity<TeamIssue> {

    @XStreamAlias("pagesize")
    private int pageSize;

    @XStreamAlias("totalCount")
    private int totalCount;

    @XStreamAlias("issues")
    private ArrayList<TeamIssue> list = new ArrayList<TeamIssue>();

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

    @Override
    public ArrayList<TeamIssue> getList() {
	return list;
    }

    public void setList(ArrayList<TeamIssue> list) {
	this.list = list;
    }
}
