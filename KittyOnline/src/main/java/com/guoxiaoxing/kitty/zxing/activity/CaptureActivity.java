/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guoxiaoxing.kitty.zxing.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.AppException;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.model.BarCode;
import com.guoxiaoxing.kitty.model.ResultBean;
import com.guoxiaoxing.kitty.model.SingInResult;
import com.guoxiaoxing.kitty.ui.base.BaseActivity;
import com.guoxiaoxing.kitty.util.DialogHelp;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.guoxiaoxing.kitty.zxing.camera.CameraManager;
import com.guoxiaoxing.kitty.zxing.decode.DecodeThread;
import com.guoxiaoxing.kitty.zxing.utils.BeepManager;
import com.guoxiaoxing.kitty.zxing.utils.CaptureActivityHandler;
import com.guoxiaoxing.kitty.zxing.utils.InactivityTimer;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Field;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends BaseActivity implements
        SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    @Bind(R.id.tb_scan_activity)
    Toolbar mTbScanActivity;
    @Bind(R.id.sv_scan_preview)
    SurfaceView mSvScanPreview;
    @Bind(R.id.iv_scan_mask_top)
    ImageView mIvScanMaskTop;
    @Bind(R.id.iv_scan_line)
    ImageView mIvScanLine;
    @Bind(R.id.rl_scan_crop_view)
    RelativeLayout mRlScanCropView;
    @Bind(R.id.iv_scan_mask_bottom)
    ImageView mIvScanMaskBottom;
    @Bind(R.id.tv_tip)
    TextView mTvTip;
    @Bind(R.id.iv_scan_mask_left)
    ImageView mIvScanMaskLeft;
    @Bind(R.id.iv_scan_mask_right)
    ImageView mIvScanMaskRight;
    @Bind(R.id.rl_scan_container)
    RelativeLayout mRlScanContainer;
    @Bind(R.id.iv_scan_flash)
    ImageView mIvScanFlash;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

//    private SurfaceView mSvScanPreview = null;
//    private RelativeLayout mRlScanContainer;
//    private RelativeLayout mRlScanCropView;
//    private ImageView mIvScanLine;
//    private ImageView mIvScanFlash;

    private Rect mCropRect = null;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(mSvScanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            mSvScanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            mSvScanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG,
                    "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void setActionBar() {
        super.setActionBar();
        setSupportActionBar(mTbScanActivity);
    }

    @Override
    public void initView() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mIvScanFlash.setOnClickListener(this);
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        mIvScanLine.startAnimation(animation);
    }

    @Override
    public void initData() {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(final Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        // 通过这种方式可以获取到扫描的图片
//	bundle.putInt("width", mCropRect.width());
//	bundle.putInt("height", mCropRect.height());
//	bundle.putString("result", rawResult.getText());
//
//	startActivity(new Intent(CaptureActivity.this, ResultActivity.class)
//		.putExtras(bundle));

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                handleText(rawResult.getText());
            }
        }, 800);
    }

    private void handleText(String text) {

        if (StringUtils.isUrl(text)) {
            showUrlOption(text);
        } else {
            handleOtherText(text);
        }
    }

    private void showUrlOption(final String url) {
        if (url.contains("scan_login")) {
            showConfirmLogin(url);
            return;
        }
        if (url.contains("oschina.net")) {
            UIHelper.showUrlRedirect(CaptureActivity.this, url);
            finish();
            return;
        }
        DialogHelp.getConfirmDialog(this, "可能存在风险，是否打开链接?</br>" + url, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UIHelper.showUrlRedirect(CaptureActivity.this, url);
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }

    private void showConfirmLogin(final String url) {
        if (!AppContext.getInstance().isLogin()) {
            showLogin();
            return;
        }
        DialogHelp.getConfirmDialog(this, "扫描成功，是否进行网页登陆", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handleScanLogin(url);
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }

    private void handleScanLogin(final String url) {
        OSChinaApi.scanQrCodeLogin(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                ResultBean result = XmlUtils.toBean(ResultBean.class, arg2);
                if (result != null && result.getResult() != null
                        && result.getResult().OK()) {
                    AppContext.showToast(result.getResult().getErrorMessage());
                    finish();
                } else {
                    handler.sendEmptyMessage(R.id.restart_preview);
                    AppContext.showToast(result != null
                            && result.getResult() != null ? result.getResult()
                            .getErrorMessage() : "登陆失败");
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                handler.sendEmptyMessage(R.id.restart_preview);
                if (arg2 != null) {
                    AppContext.showToast(new String(arg2));
                } else {
                    AppContext.showToast("网页登陆失败");
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog("已扫描，正在登陆...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });
    }

    private void handleOtherText(final String text) {
        // 判断是否符合基本的json格式
        if (!text.matches("^\\{.*")) {
            showCopyTextOption(text);
        } else {
            try {
                BarCode barcode = BarCode.parse(text);
                int type = barcode.getType();
                switch (type) {
                    case BarCode.SIGN_IN:// 签到
                        handleSignIn(barcode);
                        break;
                    default:
                        break;
                }
            } catch (AppException e) {
                showCopyTextOption(text);
            }
        }
    }

    private void handleSignIn(BarCode barCode) {
        if (barCode.isRequireLogin() && !AppContext.getInstance().isLogin()) {
            showLogin();
            return;
        }
        showWaitDialog("正在签到...");
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    SingInResult res = SingInResult.parse(new String(arg2));
                    if (res.isOk()) {
                        DialogHelp.getMessageDialog(CaptureActivity.this, res.getMessage()).show();
                    } else {
                        DialogHelp.getMessageDialog(CaptureActivity.this, res.getErrorMes()).show();
                    }
                } catch (AppException e) {
                    e.printStackTrace();
                    onFailure(arg0, arg1, arg2, e);
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                hideWaitDialog();
                DialogHelp.getMessageDialog(CaptureActivity.this, arg3.getMessage()).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        };
        OSChinaApi.singnIn(barCode.getUrl(), handler);
    }

    private void showLogin() {
        DialogHelp.getConfirmDialog(this, "请先登录，再进行", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UIHelper.showLoginActivity(CaptureActivity.this);
            }
        }).show();
    }

    private void showCopyTextOption(final String text) {
        DialogHelp.getConfirmDialog(this, text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cbm.setText(text);
                AppContext.showToast("复制成功");
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager,
                        DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        mRlScanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = mRlScanCropView.getWidth();
        int cropHeight = mRlScanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = mRlScanContainer.getWidth();
        int containerHeight = mRlScanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_scan_flash:
                light();
                break;

            default:
                break;
        }
    }

    private boolean flag;

    protected void light() {
        if (flag == true) {
            flag = false;
            // 开闪光灯
            cameraManager.openLight();
            mIvScanFlash.setBackgroundResource(R.drawable.flash_open);
        } else {
            flag = true;
            // 关闪光灯
            cameraManager.offLight();
            mIvScanFlash.setBackgroundResource(R.drawable.flash_default);
        }
    }


}