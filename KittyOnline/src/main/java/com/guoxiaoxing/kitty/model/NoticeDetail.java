package com.guoxiaoxing.kitty.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 通知实体类
 *
 * @author guoxiaoxing
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class NoticeDetail extends BaseObject {

    @XStreamAlias("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
