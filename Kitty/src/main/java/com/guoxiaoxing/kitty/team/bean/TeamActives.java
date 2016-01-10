package com.guoxiaoxing.kitty.team.bean;

import java.util.ArrayList;
import java.util.List;

import com.guoxiaoxing.kitty.bean.Entity;
import com.guoxiaoxing.kitty.bean.ListEntity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 动态列表
 * 
 * @author kymjs
 * 
 */
@XStreamAlias("oschina")
public class TeamActives extends Entity implements ListEntity<TeamActive> {
    private static final long serialVersionUID = 1L;

    @XStreamAlias("actives")
    ArrayList<TeamActive> actives = new ArrayList<TeamActive>();

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public List<TeamActive> getList() {
        return actives;
    }

    public ArrayList<TeamActive> getActives() {
        return actives;
    }

    public void setActives(ArrayList<TeamActive> actives) {
        this.actives = actives;
    }

}
