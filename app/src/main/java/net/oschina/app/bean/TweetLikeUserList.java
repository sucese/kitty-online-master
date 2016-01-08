package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * TweetLikeUserList.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-3-26 下午4:23:32
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TweetLikeUserList implements ListEntity<User> {
    
    @XStreamAlias("likeList")
    private List<User> list = new ArrayList<User>();

    public List<User> getList() {
        return list;
    }

    public void getList(List<User> list) {
        this.list = list;
    }
}

