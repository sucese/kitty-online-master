package com.guoxiaoxing.specialitycore;

import android.app.Application;

import com.guoxiaoxing.specialitycore.util.VolleyRequest;

/**
 * Created by guoxiaoxing on 15-12-30.
 */
public class AppApplication extends Application{


    public AppApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        VolleyRequest.buildRequestQueue(this);
    }
}
