package com.guoxiaoxing.kitty.presenter.base;

import android.app.Activity;

/**
 * @author guoxiaoxing
 */
public interface ActivityLifeCycleDelegateProvider {
    ActivityLifeCycleDelegate createActivityLifeCycleDelegate(Activity activity);
}
