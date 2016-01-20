package com.guoxiaoxing.kitty.team.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import com.guoxiaoxing.kitty.bean.BaseEntity;

/**
 * 帖子、任务、讨论的创建者，
 * 注：节点
 *
 * @author fireant
 */
@SuppressWarnings("serial")
@XStreamAlias("author")
public class Author extends BaseEntity {

    @XStreamAlias("name")
    private String name;

    @XStreamAlias("portrait")
    private String portrait;

    @XStreamAlias("")
    private String oscName;

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

}
