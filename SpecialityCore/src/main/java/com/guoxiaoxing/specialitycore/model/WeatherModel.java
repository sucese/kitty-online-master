package com.guoxiaoxing.specialitycore.model;

import com.guoxiaoxing.specialitycore.presenter.OnWwatherListener;

/**
 * Created by guoxiaoxing on 16-1-6.
 * 天气Model接口
 */
public interface WeatherModel {
    
    void loadWeather(String cityNO, OnWwatherListener listener);
}
