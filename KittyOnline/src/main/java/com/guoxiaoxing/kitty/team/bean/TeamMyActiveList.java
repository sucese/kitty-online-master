package com.guoxiaoxing.kitty.team.bean;

import java.util.ArrayList;
import java.util.List;

import com.guoxiaoxing.kitty.bean.Entity;
import com.guoxiaoxing.kitty.bean.ListEntity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TeamMyActiveList extends Entity implements ListEntity<TeamMyActive> {

    @XStreamAlias("actives")
    private List<TeamMyActive> list = new ArrayList<TeamMyActive>();

    @Override
    public List<TeamMyActive> getList() {
        return list;
    }

    public void setList(List<TeamMyActive> list) {
        this.list = list;
    }

}
