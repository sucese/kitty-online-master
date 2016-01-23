package com.guoxiaoxing.kitty;

import android.app.Activity;
import android.content.Context;

import com.guoxiaoxing.kitty.ui.expansion.BeamBaseActivity;
import com.guoxiaoxing.kitty.ui.expansion.overlay.ViewExpansionDelegate;
import com.guoxiaoxing.kitty.ui.expansion.overlay.ViewExpansionDelegateProvider;
import com.guoxiaoxing.kitty.model.base.ModelManager;
import com.guoxiaoxing.kitty.presenter.base.ActivityLifeCycleDelegate;
import com.guoxiaoxing.kitty.presenter.base.ActivityLifeCycleDelegateProvider;


/**
 * Created by Mr.Jude on 2015/7/26.
 */
public final class Beam {
    private static ActivityLifeCycleDelegateProvider mActivityLIfeCycleDelegateProvider;

    private static ViewExpansionDelegateProvider mViewExpansionDelegateProvider;


    public static ActivityLifeCycleDelegate createActivityLifeCycleDelegate(Activity activity) {
        if (mActivityLIfeCycleDelegateProvider!=null)
            return mActivityLIfeCycleDelegateProvider.createActivityLifeCycleDelegate(activity);
        else return null;
    }

    public static ViewExpansionDelegate createViewExpansionDelegate(BeamBaseActivity activity){
        if (mViewExpansionDelegateProvider==null)
            return ViewExpansionDelegateProvider.DEFAULT.createViewExpansionDelegate(activity);
        else return mViewExpansionDelegateProvider.createViewExpansionDelegate(activity);
    }

    public static void setActivityLifeCycleDelegateProvider(ActivityLifeCycleDelegateProvider activityLifeCycleDelegateClass){
        mActivityLIfeCycleDelegateProvider = activityLifeCycleDelegateClass;
    }

    public static void setViewExpansionDelegateProvider(ViewExpansionDelegateProvider viewExpansionDelegateProvider) {
        mViewExpansionDelegateProvider = viewExpansionDelegateProvider;
    }
    public static void init(Context ctx){
        ModelManager.init(ctx);
    }

}
