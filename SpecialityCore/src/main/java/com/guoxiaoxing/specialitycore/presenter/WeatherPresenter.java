package com.guoxiaoxing.specialitycore.presenter;

/**
 * Created by guoxiaoxing on 16-1-6.
 * 天气Presenter接口
 */
public interface WeatherPresenter {

    /**
     * 获取天气逻辑
     * @param cityNO
     */
    void getWeather(String cityNO);
}
