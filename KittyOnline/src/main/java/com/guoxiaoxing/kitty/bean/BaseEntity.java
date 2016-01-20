package com.guoxiaoxing.kitty.bean;


/**
 * 实体类
 * @author guoxiaoxing
 */
@SuppressWarnings("serial")
public abstract class BaseEntity extends Base {

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
