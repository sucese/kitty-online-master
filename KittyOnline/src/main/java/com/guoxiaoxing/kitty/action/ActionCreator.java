package com.guoxiaoxing.kitty.action;

/**
 * @author guoxiaoxing
 */
public interface ActionCreator<T extends Action> {
    T createAction(String type, Object... params);
}
