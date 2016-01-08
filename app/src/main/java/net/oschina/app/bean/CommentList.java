package net.oschina.app.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 评论列表实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月14日 下午3:32:39
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class CommentList extends Entity implements ListEntity<Comment> {

    public final static int CATALOG_NEWS = 1;
    public final static int CATALOG_POST = 2;
    public final static int CATALOG_TWEET = 3;
    public final static int CATALOG_ACTIVE = 4;
    public final static int CATALOG_MESSAGE = 4;// 动态与留言都属于消息中心

    @XStreamAlias("pagesize")
    private int pageSize;
    @XStreamAlias("allCount")
    private int allCount;
    @XStreamAlias("comments")
    private final List<Comment> commentlist = new ArrayList<Comment>();

    public int getPageSize() {
        return pageSize;
    }

    public int getAllCount() {
        return allCount;
    }

    public List<Comment> getCommentlist() {
        return commentlist;
    }

    @Override
    public List<Comment> getList() {
        return commentlist;
    }

    public void sortList() {
        Collections.sort(commentlist, new Comparator<Comment>() {

            @Override
            public int compare(Comment lhs, Comment rhs) {
                return lhs.getPubDate().compareTo(rhs.getPubDate());
            }
        });
    }
}
