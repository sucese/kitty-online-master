package com.guoxiaoxing.specialitycore.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guoxiaoxing.specialitycore.R;
import com.guoxiaoxing.specialitycore.model.entity.Weather;
import com.guoxiaoxing.specialitycore.model.entity.WeatherBean;
import com.guoxiaoxing.specialitycore.presenter.WeatherPresenter;
import com.guoxiaoxing.specialitycore.presenter.impl.WeatherPresenterImpl;
import com.guoxiaoxing.specialitycore.ui.view.WeatherView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity implements WeatherView {


    @Bind(R.id.et_city_no)
    EditText etCityNo;
    @Bind(R.id.btn_go)
    Button btnGo;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.tv_city_no)
    TextView tvCityNo;
    @Bind(R.id.tv_temp)
    TextView tvTemp;
    @Bind(R.id.tv_WD)
    TextView tvWD;
    @Bind(R.id.tv_WS)
    TextView tvWS;
    @Bind(R.id.tv_SD)
    TextView tvSD;
    @Bind(R.id.tv_WSE)
    TextView tvWSE;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_njd)
    TextView tvNjd;

    private WeatherPresenter mWeatherPresenter;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        mWeatherPresenter = new WeatherPresenterImpl(this);
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("加载天气中...");

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeatherPresenter.getWeather(etCityNo.getText().toString().trim());
            }
        });
    }

    @Override
    public void showLoading() {

        mDialog.show();

    }

    @Override
    public void hideLoading() {

        mDialog.dismiss();

    }

    @Override
    public void showError() {

        Toast.makeText(this, "错误", Toast.LENGTH_SHORT).show();
        ;

    }

    @Override
    public void setWeatherBean(Weather weather) {

        WeatherBean info = weather.getWeatherBean();
        tvCity.setText(info.getCity());
        tvCityNo.setText(info.getCityid());
        tvTemp.setText(info.getTemp());
        tvWD.setText(info.getWD());
        tvWS.setText(info.getWS());
        tvSD.setText(info.getSD());
        tvWSE.setText(info.getWS());
        tvTime.setText(info.getTemp());
        tvNjd.setText(info.getNjd());

    }
}
