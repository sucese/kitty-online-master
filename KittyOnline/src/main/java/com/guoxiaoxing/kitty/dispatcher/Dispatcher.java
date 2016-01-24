package com.guoxiaoxing.kitty.dispatcher;

import com.guoxiaoxing.kitty.action.Action;
import com.guoxiaoxing.kitty.store.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guoxiaoxing
 */
public class Dispatcher {

    private static Dispatcher instance;

    /*这里仅仅用一个ArrayList来管理Stores，对于一个更复杂的App可能需要精心设计数据结构来
    管理Stores组织和相互间的依赖关系*/
    private final List<Store> stores = new ArrayList<>();

    public static Dispatcher getInstance() {
        if (instance == null) {
            instance = new Dispatcher();
        }
        return instance;
    }

    Dispatcher() {

    }

    /**
     * 注册每个Store的回调接口
     *
     * @param store
     */
    public void register(final Store store) {
        if (!stores.contains(store)) {
            stores.add(store);
        }
    }

    /**
     * 取消每个Store的毁掉接口
     *
     * @param store
     */
    public void unregister(final Store store) {
        stores.remove(store);
    }

    /**
     * 分发Action，触发Store注册的回调接口
     *
     * @param action
     */
    public void dispatch(Action action) {
        post(action);
    }

    private void post(final Action action) {
        for (Store store : stores) {
            store.onAction(action);
        }
    }
}
