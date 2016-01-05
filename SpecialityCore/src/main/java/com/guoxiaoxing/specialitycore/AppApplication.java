package com.guoxiaoxing.specialitycore;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by guoxiaoxing on 15-12-30.
 */
public class AppApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //科大讯飞初始化,请勿在“=”与 appid 之间添加任务空字符或者转义符
        SpeechUtility.createUtility(AppApplication.this, SpeechConstant.APPID + "=568b60b0");
    }
}
