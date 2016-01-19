package com.guoxiaoxing.kitty.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 实体类
 * @author guoxiaoxing
 */
@SuppressWarnings("serial")
public abstract class Entity extends Base {

    @XStreamAlias("id")
    protected int id;

    protected String cacheKey;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getCacheKey() {
	return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
	this.cacheKey = cacheKey;
    }
}
