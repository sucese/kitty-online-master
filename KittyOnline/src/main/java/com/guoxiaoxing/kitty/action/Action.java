package com.guoxiaoxing.kitty.action;


/**
 * @author guoxiaoxing
 */
public interface Action<P> {

    P getData();

    String getType();

}
