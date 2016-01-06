package com.guoxiaoxing.specialitycore.model.impl;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.guoxiaoxing.specialitycore.model.WeatherModel;
import com.guoxiaoxing.specialitycore.model.entity.Weather;
import com.guoxiaoxing.specialitycore.presenter.OnWwatherListener;
import com.guoxiaoxing.specialitycore.util.VolleyRequest;

/**
 * Created by guoxiaoxing on 16-1-6.
 * 天气Model实现
 */
public class WeatherModelImpl implements WeatherModel {
    @Override
    public void loadWeather(String cityNO, final OnWwatherListener listener) {

        //数据层操作
        VolleyRequest.newInstance().newGsonRequest("http://www.weather.com.cn/data/sk/" + cityNO +
                ".html", Weather.class, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather weather) {

                if(weather != null){
                    listener.onSuccess(weather);
                }else{
                    listener.onError();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                listener.onError();

            }
        });

    }
}
