package com.guoxiaoxing.kitty;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;

import com.guoxiaoxing.kitty.service.LogUploadService;
import com.guoxiaoxing.kitty.ui.MainActivity;
import com.guoxiaoxing.kitty.ui.welcome.base.ParentViewPager;
import com.guoxiaoxing.kitty.ui.welcome.outlayer.AdvertisementFragment;
import com.guoxiaoxing.kitty.ui.welcome.outlayer.WelcomAnimFragment;
import com.guoxiaoxing.kitty.util.DisplayUtil;
import com.guoxiaoxing.kitty.util.TDevice;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import org.kymjs.kjframe.http.KJAsyncTask;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.PreferenceHelper;

import java.io.File;

/**
 * 应用启动界面
 *
 * @author guoxiaoxing
 */
public class AppStart extends FragmentActivity {


    private final int FRAGMENT_WELCOMEANIM = 0;
    private final int FRAGMENT_LOGINANIM = 1;

    private ImageView mIvLogo;
    private ParentViewPager mVpParent;

    private float mLogoY;
    private AnimatorSet mAnimatorSet;

    private WelcomAnimFragment mWelcomAnimFragment;
    private AdvertisementFragment mAdvertisementFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 防止第三方跳转时出现双实例
        Activity aty = AppManager.getActivity(MainActivity.class);
        if (aty != null && !aty.isFinishing()) {
            finish();
        }
        // SystemTool.gc(this); //针对性能好的手机使用，加快应用相应速度

        setContentView(R.layout.app_start);
        switch (checkAppStart()) {
            //第一次启动
            case FIRST_TIME:
                playUserGuide();
                break;
            //新版本发布
            case FIRST_TIME_VERSION:
                playNewFesture();
                break;
            //正常启动
            case NORMAL:
//                playUserGuide();
                startApp();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int cacheVersion = PreferenceHelper.readInt(this, "first_install",
                "first_install", -1);
        int currentVersion = TDevice.getVersionCode();
        if (cacheVersion < currentVersion) {
            PreferenceHelper.write(this, "first_install", "first_install",
                    currentVersion);
            cleanImageCache();
        }
    }

    private void cleanImageCache() {
        final File folder = FileUtils.getSaveFolder("OSChina/imagecache");
        KJAsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (File file : folder.listFiles()) {
                    file.delete();
                }
            }
        });
    }

    /**
     * 正常启动
     */
    private void startApp() {
        Intent uploadLog = new Intent(this, LogUploadService.class);
        startService(uploadLog);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 显示新版本特性
     */
    private void playNewFesture() {

    }

    /**
     * 第一次启动
     */
    private void playUserGuide() {
        mIvLogo = (ImageView) findViewById(R.id.iv_logo);
        mVpParent = (ParentViewPager) findViewById(R.id.vp_parent);
        mVpParent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case FRAGMENT_WELCOMEANIM:
                        break;
                    case FRAGMENT_LOGINANIM:
                        ParentViewPager.mLoginPageLock = true;
//                        mIvLogo.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (mLogoY == 0) {
//                                    mLogoY = ViewHelper.getY(mIvLogo);
//                                }
//                                playLogoInAnim();
//                            }
//                        }, 500);
                        mVpParent.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mAdvertisementFragment.playInAnim();
//                                startApp();
                            }
                        }, 100);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });


        ParentFragmentStatePagerAdapter adapter = new ParentFragmentStatePagerAdapter(getSupportFragmentManager());
        mVpParent.setAdapter(adapter);
    }

    private void playLogoInAnim() {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(mIvLogo, "scaleX", 1.0f, 0.5f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(mIvLogo, "scaleY", 1.0f, 0.5f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(mIvLogo, "y", mLogoY, DisplayUtil.dip2px(AppStart.this, 15));

        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(anim1).with(anim2);
        mAnimatorSet.play(anim2).with(anim3);
        mAnimatorSet.setDuration(1000);
        mAnimatorSet.start();
    }

    public class ParentFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        public ParentFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case FRAGMENT_WELCOMEANIM:
                    mWelcomAnimFragment = new WelcomAnimFragment();
                    mVpParent.setWelcomAnimFragment(mWelcomAnimFragment);
                    mWelcomAnimFragment.setWelcomAnimFragmentInterface(new WelcomAnimFragment.WelcomAnimFragmentInterface() {
                        @Override
                        public void onSkip() {
                            mVpParent.setCurrentItem(1);
                        }
                    });
                    return mWelcomAnimFragment;
                case FRAGMENT_LOGINANIM:
                    mAdvertisementFragment = new AdvertisementFragment();
                    return mAdvertisementFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    /**
     * 点击返回键退出程序
     */
//    private static Boolean isExit = false;
//    private Handler mHandler = new Handler();

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (isExit == false) {
//                isExit = true;
//                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isExit = false;
//                    }
//                }, 2000);
//            } else {
//                finish();
//                System.exit(0);
//            }
//        }
//        return false;
//    }

    public enum AppStartMode {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL;
    }

    //上一次app启动时的version code
    private static final String LAST_APP_VERSION = "last_app_version";

    public AppStartMode checkAppStart() {

        PackageInfo pInfo;
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        AppStartMode appStartMode = AppStartMode.NORMAL;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int lastVersionCode = sharedPreferences
                    .getInt(LAST_APP_VERSION, -1);
            int currentVersionCode = pInfo.versionCode;
            appStartMode = checkAppStart(currentVersionCode, lastVersionCode);
            // Update version in preferences
            sharedPreferences.edit()
                    .putInt(LAST_APP_VERSION, currentVersionCode).commit();
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("AppContext",
                    "Unable to determine current app version from pacakge manager. Defenisvely assuming normal app start.");
        }
        return appStartMode;
    }

    public AppStartMode checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStartMode.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStartMode.FIRST_TIME_VERSION;
        } else if (lastVersionCode > currentVersionCode) {
            Log.w("AppContext", "Current version code (" + currentVersionCode
                    + ") is less then the one recognized on last startup ("
                    + lastVersionCode
                    + "). Defenisvely assuming normal app start.");
            return AppStartMode.NORMAL;
        } else {
            return AppStartMode.NORMAL;
        }
    }
}
