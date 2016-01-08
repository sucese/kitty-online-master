package net.oschina.app.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 通知信息实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("serial")
@XStreamAlias("notice")
public class Notice implements Serializable {

    public final static String UTF8 = "UTF-8";
    public final static String NODE_ROOT = "oschina";

    public final static int TYPE_ATME = 1;
    public final static int TYPE_MESSAGE = 2;
    public final static int TYPE_COMMENT = 3;
    public final static int TYPE_NEWFAN = 4;
    public final static int TYPE_NEWLIKE = 5;

    @XStreamAlias("atmeCount")
    private int atmeCount;

    @XStreamAlias("msgCount")
    private int msgCount;

    @XStreamAlias("reviewCount")
    private int reviewCount;

    @XStreamAlias("newFansCount")
    private int newFansCount;

    @XStreamAlias("newLikeCount")
    private int newLikeCount;

    public int getAtmeCount() {
	return atmeCount;
    }

    public void setAtmeCount(int atmeCount) {
	this.atmeCount = atmeCount;
    }

    public int getMsgCount() {
	return msgCount;
    }

    public void setMsgCount(int msgCount) {
	this.msgCount = msgCount;
    }

    public int getReviewCount() {
	return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
	this.reviewCount = reviewCount;
    }

    public int getNewFansCount() {
	return newFansCount;
    }

    public void setNewFansCount(int newFansCount) {
	this.newFansCount = newFansCount;
    }

    public int getNewLikeCount() {
        return newLikeCount;
    }

    public void setNewLikeCount(int newLikeCount) {
        this.newLikeCount = newLikeCount;
    }
}
