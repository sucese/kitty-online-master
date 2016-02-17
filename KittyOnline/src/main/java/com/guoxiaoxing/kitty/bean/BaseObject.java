package com.guoxiaoxing.kitty.bean;


import com.avos.avoscloud.AVObject;

import java.io.Serializable;

/**
 * 实体类基类
 *
 * @author guoxiaoxing
 */
public abstract class BaseObject extends AVObject implements Serializable {

    protected int id;
    protected String cacheKey;
    protected Notice notice;

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

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
