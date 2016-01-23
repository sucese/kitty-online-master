package com.guoxiaoxing.kitty.model.base;

import android.content.Context;

/**
 * @author guoxiaoxing
 */
public abstract class BaseModel {
    public static final <T extends BaseModel> T getInstance(Class<T> clazz) {
        BaseModel model = ModelManager.mModelMap.get(clazz);
        if (model == null)
            throw new RuntimeException(clazz.getName() + " No Found , Have you declare MODEL in the manifests?");
        return (T) model;
    }

    protected <N> N getNetManager() {
        return null;
    }

    protected void onAppCreate(Context ctx) {
    }

    protected void onAppCreateOnBackground(Context ctx) {
    }

    protected final void runOnBackground(Runnable runnable) {
        ModelManager.runOnBackThread(runnable);
    }
}
