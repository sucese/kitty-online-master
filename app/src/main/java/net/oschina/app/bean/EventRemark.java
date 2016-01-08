package net.oschina.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * 活动备注选择
 * Created by zhangdeyi on 15/8/19.
 */
@XStreamAlias("remark")
public class EventRemark implements Serializable {

    @XStreamAlias("remarkTip")
    private String remarkTip;

    @XStreamAlias("remarkSelect")
    private RemarksSelet select;

    public class RemarksSelet implements Serializable {
        @XStreamImplicit(itemFieldName = "select")
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

    }

    public String getRemarkTip() {
        return remarkTip;
    }

    public void setRemarkTip(String remarkTip) {
        this.remarkTip = remarkTip;
    }

    public RemarksSelet getSelect() {
        return select;
    }

    public void setSelect(RemarksSelet select) {
        this.select = select;
    }
}
