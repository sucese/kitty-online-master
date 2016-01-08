package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天详细信息实体类
 * @author 铂金小鸟（http://my.oschina.net/fants）
 * @Created 2015年9月16日 上午4:20:01
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class MessageDetailList extends Entity implements ListEntity<MessageDetail> {

    @XStreamAlias("allCount")
    private int allCount;

    @XStreamAlias("pagesize")
    private int pageSize;

    @XStreamAlias("messages")
    private List<MessageDetail> messagelist = new ArrayList<MessageDetail>();

    public int getPageSize() {
        return pageSize;
    }

    public int getMessageCount() {
        return allCount;
    }

    public List<MessageDetail> getMessagelist() {
        return messagelist;
    }

    @Override
    public List<MessageDetail> getList() {
        return messagelist;
    }

}
