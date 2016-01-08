package net.oschina.app.bean;

/**
 * 举报实体类
 * 
 * @author 火蚁（http://my.oschina.net/LittleDY）
 * @version 1.0
 * @created 2014-02-13
 */
public class Report extends Entity {
    private static final long serialVersionUID = 1L;

    public static final byte TYPE_QUESTION = 0x02;// 问题

    private int objId;//需要举报的id
    private String url;// 举报的链接地址
    private byte objType;// 举报的类型
    private int reason;// 原因
    private String otherReason;// 其他原因

    public int getObjId() {
        return objId;
    }

    public void setObjId(int objId) {
        this.objId = objId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte getObjType() {
        return objType;
    }

    public void setObjType(byte objType) {
        this.objType = objType;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }
}
