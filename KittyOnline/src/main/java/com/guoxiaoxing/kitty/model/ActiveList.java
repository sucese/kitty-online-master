package com.guoxiaoxing.kitty.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 动态实体列表
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月22日 下午3:34:21
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class ActiveList extends BaseObject implements ListEntity<UserActive> {

    public final static int CATALOG_LASTEST = 1;// 最新
    public final static int CATALOG_ATME = 2;// @我
    public final static int CATALOG_COMMENT = 3;// 评论
    public final static int CATALOG_MYSELF = 4;// 我自己

    @XStreamAlias("pagesize")
    private int pageSize;

    @XStreamAlias("activeCount")
    private int activeCount;

    @XStreamAlias("activies")
    private List<UserActive> activelist = new ArrayList<UserActive>();
    
    @XStreamAlias("result")
    private Result result;

    public int getPageSize() {
	return pageSize;
    }

    public int getActiveCount() {
	return activeCount;
    }

    public List<UserActive> getActivelist() {
	return activelist;
    }

    @Override
    public List<UserActive> getList() {
	return activelist;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
