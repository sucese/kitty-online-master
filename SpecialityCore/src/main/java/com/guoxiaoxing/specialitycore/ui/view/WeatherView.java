package com.guoxiaoxing.specialitycore.ui.view;

import com.guoxiaoxing.specialitycore.model.entity.Weather;

/**
 * Created by guoxiaoxing on 16-1-6.
 */
public interface WeatherView {

    void showLoading();
    void hideLoading();
    void showError();
    void setWeatherBean(Weather weather);
}
