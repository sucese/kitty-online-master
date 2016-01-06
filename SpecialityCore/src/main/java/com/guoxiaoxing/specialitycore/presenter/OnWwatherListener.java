package com.guoxiaoxing.specialitycore.presenter;

import com.guoxiaoxing.specialitycore.model.entity.Weather;

/**
 * Created by guoxiaoxing on 16-1-6.
 * OnWeatherListener，其在Presenter层实现，给Model层回调，更改View层的状态，确保
 * Model层不直接操作View层。如果没有这一接口在WeatherPresenterImpl实现的话，Weath
 * erPresenterImpl只 有View和Model的引用那么Model怎么把结果告诉View呢？当然这只是
 * 一种解决方案，在实际项目中可以使用Dagger、EventBus、 Otto等第三方框架结合进来达到
 * 更加松耦合的设计。
 */
public interface OnWwatherListener {

    void onSuccess(Weather weather);
    void onError();
}
