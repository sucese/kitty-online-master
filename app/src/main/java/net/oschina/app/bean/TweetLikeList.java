package net.oschina.app.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * LikeTweetList.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-4-10 上午10:11:48
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class TweetLikeList implements ListEntity<TweetLike> {

    @XStreamAlias("likeList")
    private List<TweetLike> list;
    
    @Override
    public List<TweetLike> getList() {
	// TODO Auto-generated method stub
	return list;
    }

    public void setList(List<TweetLike> list) {
        this.list = list;
    }

}

