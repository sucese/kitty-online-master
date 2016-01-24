package com.guoxiaoxing.kitty.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.guoxiaoxing.kitty.AppConfig;
import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.ui.dialog.ShareDialog;
import com.guoxiaoxing.kitty.ui.activity.SimpleBackActivity;
import com.guoxiaoxing.kitty.ui.base.BaseActivity;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;

import butterknife.Bind;

/**
 * 浏览器界面
 *
 * @author guoxiaoxing
 */
@SuppressLint("NewApi")
public class BrowserFragment extends BaseFragment {
    @Bind(R.id.webview)
    WebView mWebView;
    @Bind(R.id.browser_back)
    ImageView mImgBack;
    @Bind(R.id.browser_forward)
    ImageView mImgForward;
    @Bind(R.id.browser_refresh)
    ImageView mImgRefresh;
    @Bind(R.id.browser_system_browser)
    ImageView mImgSystemBrowser;
    @Bind(R.id.browser_bottom)
    LinearLayout mLayoutBottom;
    @Bind(R.id.progress)
    ProgressBar mProgress;

    public static final String BROWSER_KEY = "browser_url";
    public static final String DEFAULT = "http://www.oschina.net/";

    private int TAG = 1; // 双击事件需要
    private Activity aty;
    private String mCurrentUrl = DEFAULT;

    private Animation animBottomIn, animBottomOut;
    private GestureDetector mGestureDetector;
    private CookieManager cookie;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browser_back:
                mWebView.goBack();
                break;
            case R.id.browser_forward:
                mWebView.goForward();
                break;
            case R.id.browser_refresh:
                mWebView.loadUrl(mWebView.getUrl());
                break;
            case R.id.browser_system_browser:
                try {
                    // 启用外部浏览器
                    Uri uri = Uri.parse(mCurrentUrl);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    aty.startActivity(it);
                } catch (Exception e) {
                    AppContext.showToast("网页地址错误");
                }
                break;
        }
    }

    @Override
    public void initView(View view) {
        initWebView();
        initBarAnim();
        mImgBack.setOnClickListener(this);
        mImgForward.setOnClickListener(this);
        mImgRefresh.setOnClickListener(this);
        mImgSystemBrowser.setOnClickListener(this);

        mGestureDetector = new GestureDetector(aty, new MyGestureListener());
        mWebView.loadUrl(mCurrentUrl);
        mWebView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    @Override
    public void initData() {
        Bundle bundle = getActivity().getIntent().getBundleExtra(
                SimpleBackActivity.BUNDLE_KEY_ARGS);
        if (bundle != null) {
            mCurrentUrl = bundle.getString(BROWSER_KEY);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        aty = getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.browser_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_shared:
                showSharedDialog();
                break;
        }
        return true;
    }

    /**
     * 初始化上下栏的动画并设置结束监听事件
     */
    private void initBarAnim() {
        animBottomIn = AnimationUtils.loadAnimation(aty, R.anim.anim_bottom_in);
        animBottomOut = AnimationUtils.loadAnimation(aty,
                R.anim.anim_bottom_out);
        animBottomIn.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutBottom.setVisibility(View.VISIBLE);
            }
        });
        animBottomOut.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutBottom.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 打开分享dialog
     */
    private void showSharedDialog() {
        final ShareDialog dialog = new ShareDialog(getActivity());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);
        dialog.setShareInfo(getShareTitle(), getShareContent(), mCurrentUrl);
        dialog.show();
    }

    /**
     * 载入链接之前会被调用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlLoading(WebView view, String url) {
        mProgress.setVisibility(View.VISIBLE);
        cookie.setCookie(url,
                AppContext.getInstance().getProperty(AppConfig.CONF_COOKIE));
    }

    /**
     * 链接载入成功后会被调用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlFinished(WebView view, String url) {
        mCurrentUrl = url;
        mProgress.setVisibility(View.GONE);
    }

    /**
     * 当前WebView显示页面的标题
     *
     * @param view  WebView
     * @param title web页面标题
     */
    protected void onWebTitle(WebView view, String title) {
        if (aty != null && mWebView != null) { // 必须做判断，由于webview加载属于耗时操作，可能会本Activity已经关闭了才被调用
            ((BaseActivity) aty).setActionBarTitle(mWebView.getTitle());
        }
    }

    /**
     * 当前WebView显示页面的图标
     *
     * @param view WebView
     * @param icon web页面图标
     */
    protected void onWebIcon(WebView view, Bitmap icon) {
    }

    /**
     * 初始化浏览器设置信息
     */
    private void initWebView() {
        cookie = CookieManager.getInstance();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用支持javascript
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
        webSettings.setAllowFileAccess(true);// 可以访问文件
        webSettings.setBuiltInZoomControls(true);// 支持缩放
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            webSettings.setPluginState(PluginState.ON);
            webSettings.setDisplayZoomControls(false);// 支持缩放
        }
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            onWebTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            onWebIcon(view, icon);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            if (newProgress > 90) {
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            onUrlLoading(view, url);
            boolean flag = super.shouldOverrideUrlLoading(view, url);
            mCurrentUrl = url;
            return flag;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onUrlFinished(view, url);
        }
    }

    private class MyGestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {// webview的双击事件
            if (TAG % 2 == 0) {
                TAG++;
                mLayoutBottom.startAnimation(animBottomIn);
            } else {
                TAG++;
                mLayoutBottom.startAnimation(animBottomOut);
            }
            return super.onDoubleTap(e);
        }
    }

    private String getShareTitle() {
        return mWebView.getTitle();
    }

    private String getShareContent() {
        return mWebView.getTitle();
    }
}
