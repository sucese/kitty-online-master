package com.guoxiaoxing.kitty.ui.activity;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.bean.ShakeObject;
import com.guoxiaoxing.kitty.ui.base.BaseActivity;
import com.guoxiaoxing.kitty.util.KJAnimations;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.TypefaceUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.kymjs.kjframe.Core;

import java.io.ByteArrayInputStream;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 摇一摇界面
 *
 * @author guoxiaoxing
 */
public class ShakeActivity extends BaseActivity implements SensorEventListener {

    @Bind(R.id.shake_img)
    ImageView mImgShake;

    @Bind(R.id.progress)
    ProgressBar mProgress;
    @Bind(R.id.shake_bottom)
    LinearLayout mLayoutBottom;
    @Bind(R.id.iv_face)
    ImageView mImgHead;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_description)
    TextView mTvDetail;
    @Bind(R.id.tv_author)
    TextView mTvAuthor;
    @Bind(R.id.tv_comment_count)
    TextView mTvCommentCount;
    @Bind(R.id.tv_time)
    TextView mTvDate;

    private SensorManager sensorManager = null;
    private Sensor sensor;
    private Vibrator vibrator = null;

    private boolean isRequest = false;

    private float lastX;
    private float lastY;
    private float lastZ;
    private long lastUpdateTime;
    private static final int SPEED_SHRESHOLD = 45;// 这个值越大需要越大的力气来摇晃手机
    private static final int UPTATE_INTERVAL_TIME = 50;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shake;
    }

    @Override
    public void initView() {

    }

    /**
     * 摇动手机成功后调用
     */
    private void onShake() {
        isRequest = true;
        mProgress.setVisibility(View.VISIBLE);
        Animation anim = KJAnimations.shakeAnimation(mImgShake.getLeft());
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                OSChinaApi.shake(new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        isRequest = false;
                        final ShakeObject obj = XmlUtils.toBean(
                                ShakeObject.class, new ByteArrayInputStream(
                                        arg2));
                        if (obj != null) {
                            if (StringUtils.isEmpty(obj.getAuthor())
                                    && StringUtils.isEmpty(obj
                                    .getCommentCount())
                                    && StringUtils.isEmpty(obj.getPubDate())) {
                                jokeToast();
                            } else {
                                mLayoutBottom.setVisibility(View.VISIBLE);
                                mLayoutBottom
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                UIHelper.showUrlShake(ShakeActivity.this, obj);
                                            }
                                        });
                                Core.getKJBitmap().displayWithLoadBitmap(mImgHead,
                                        obj.getImage(), R.drawable.widget_dface);
                                mTvTitle.setText(obj.getTitle());
                                mTvDetail.setText(obj.getDetail());
                                mTvAuthor.setText(obj.getAuthor());
                                TypefaceUtils.setTypeface(mTvAuthor);
                                TypefaceUtils.setTypeFaceWithText(mTvCommentCount, R.string
                                        .fa_comment, obj.getCommentCount() + "");
                                TypefaceUtils.setTypeFaceWithText(mTvDate, R.string.fa_clock_o,
                                        StringUtils.friendly_time(obj.getPubDate()));
                            }
                        } else {
                            jokeToast();
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        isRequest = false;
                        jokeToast();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mProgress != null) {
                            mProgress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        mImgShake.startAnimation(anim);
    }

    private void jokeToast() {
        AppContext.showToast("红薯跟你开了个玩笑");
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (sensor != null) {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - lastUpdateTime;
        if (timeInterval < UPTATE_INTERVAL_TIME) {
            return;
        }
        lastUpdateTime = currentUpdateTime;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        double speed = (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ) / timeInterval) * 100;
        if (speed >= SPEED_SHRESHOLD && !isRequest) {
            mLayoutBottom.setVisibility(View.GONE);
            vibrator.vibrate(300);
            onShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void initData() {
        sensorManager = (SensorManager) this
                .getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View view) {

    }
}
