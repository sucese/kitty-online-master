package com.guoxiaoxing.kitty.team.bean;

import java.util.ArrayList;
import java.util.List;

import com.guoxiaoxing.kitty.model.BaseObject;
import com.guoxiaoxing.kitty.model.ListEntity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Team成员列表
 * 
 * @author kymjs
 * 
 */
@XStreamAlias("oschina")
public class TeamMemberList extends BaseObject implements ListEntity<TeamMember> {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("members")
    private List<TeamMember> list = new ArrayList<TeamMember>();

    public void setList(List<TeamMember> list) {
        this.list = list;
    }

    @Override
    public List<TeamMember> getList() {
        return list;
    }

}
