package com.guoxiaoxing.specialitycore.presenter.impl;

import com.guoxiaoxing.specialitycore.model.WeatherModel;
import com.guoxiaoxing.specialitycore.model.entity.Weather;
import com.guoxiaoxing.specialitycore.model.impl.WeatherModelImpl;
import com.guoxiaoxing.specialitycore.presenter.OnWwatherListener;
import com.guoxiaoxing.specialitycore.presenter.WeatherPresenter;
import com.guoxiaoxing.specialitycore.ui.view.WeatherView;

/**
 * Created by guoxiaoxing on 16-1-6.
 * 天气Presenter实现
 */
public class WeatherPresenterImpl implements WeatherPresenter, OnWwatherListener {

    private WeatherView mWeatherView;
    private WeatherModel mWeatherModel;

    public WeatherPresenterImpl(WeatherView weatherView) {
        mWeatherView = weatherView;
        mWeatherModel = new WeatherModelImpl();
    }

    @Override
    public void onSuccess(Weather weather) {

        mWeatherView.hideLoading();
        mWeatherView.setWeatherBean(weather);

    }

    @Override
    public void onError() {
        mWeatherView.hideLoading();
        mWeatherView.showError();

    }

    @Override
    public void getWeather(String cityNO) {

        mWeatherView.showLoading();
        mWeatherModel.loadWeather(cityNO, this);

    }
}
