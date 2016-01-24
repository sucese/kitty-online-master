package com.guoxiaoxing.kitty.store;

import com.guoxiaoxing.kitty.action.Action;
import com.squareup.otto.Bus;

/**
 * @author guoxiaoxing
 */
public abstract class Store<T extends Action> {

    /*otto eventbus*/
    private static final Bus bus = new Bus();

    protected Store() {
    }

    public void register(final Object view) {
        this.bus.register(view);
    }

    public void unregister(final Object view) {
        this.bus.unregister(view);
    }

    void emitStoreChange() {
        this.bus.post(changeEvent());
    }

    public abstract StoreChangeEvent changeEvent();

    /**
     * 注册在Dispatcher里面的回调接口，当Dispatcher有数据派发过来的时候，可以在这里处理
     *
     * @param action
     */
    public abstract void onAction(T action);

    public class StoreChangeEvent {
    }
}
