package com.guoxiaoxing.kitty.presenter.base;


public abstract class PresenterManager {

    private static PresenterManager instance = new DefaultPresenterManager();

    public static PresenterManager getInstance() {
        return instance;
    }

    public abstract <T extends BasePresenter> T create(Object view);

    public abstract <T extends BasePresenter> T get(String id);

    public abstract void destroy(String id);

}
