package com.guoxiaoxing.kitty.bean;

/**
 * 用户登陆结果实体类
 *
 * @author guoxiaoxing
 */

public class LoginUserBean extends BaseObject {

    private Result result;
    private User user;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}