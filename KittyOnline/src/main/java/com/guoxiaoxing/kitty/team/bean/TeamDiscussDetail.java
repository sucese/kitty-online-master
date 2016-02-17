package com.guoxiaoxing.kitty.team.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import com.guoxiaoxing.kitty.bean.BaseObject;

/**
 * TeamDisscussDetail.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-2-2 下午5:33:07
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TeamDiscussDetail extends BaseObject {
    
    @XStreamAlias("discuss")
    private TeamDiscuss discuss;

    public TeamDiscuss getDiscuss() {
        return discuss;
    }

    public void setDiscuss(TeamDiscuss discuss) {
        this.discuss = discuss;
    }
}

