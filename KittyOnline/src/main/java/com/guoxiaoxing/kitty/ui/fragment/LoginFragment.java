package com.guoxiaoxing.kitty.ui.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.guoxiaoxing.kitty.AppConfig;
import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.api.ApiHttpClient;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.bean.Constants;
import com.guoxiaoxing.kitty.bean.LoginUserBean;
import com.guoxiaoxing.kitty.bean.OpenIdCatalog;
import com.guoxiaoxing.kitty.ui.activity.LoginBindActivityChooseActivity;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.util.CyptoUtils;
import com.guoxiaoxing.kitty.util.DialogHelp;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.TDevice;
import com.guoxiaoxing.kitty.util.TLog;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;

import org.kymjs.kjframe.http.HttpConfig;

import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * 登录Fragment
 *
 * @author guoxiaoxing
 */
public class LoginFragment extends BaseFragment implements IUiListener, TextWatcher {

    public static final int REQUEST_CODE_OPENID = 1000;

    private static final String TAG = "LoginFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.cl_container)
    CoordinatorLayout mClContainer;
    @Bind(R.id.ll_root)
    LinearLayout mLlRoot;

    private String mParam1;
    private String mParam2;

    @Bind(R.id.til_user_phone)
    TextInputLayout mTilUserName;
    @Bind(R.id.til_user_password)
    TextInputLayout mTilPassword;
    @Bind(R.id.et_user_phone)
    AppCompatEditText mEtUserName;
    @Bind(R.id.et_user_password)
    AppCompatEditText mEtPassword;

    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.tv_forget_password)
    TextView mTvForgetPassword;
    @Bind(R.id.iv_qq_login)
    ImageView mIvQqLogin;
    @Bind(R.id.iv_wx_login)
    ImageView mIvWxLogin;
    @Bind(R.id.iv_sina_login)
    ImageView mIvSinaLogin;

    private Context mContext;
    private String mUserName;
    private String mPassword;


    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void initView(View view) {
        mContext = getActivity();
        mBtnLogin.setOnClickListener(this);
        mTvForgetPassword.setOnClickListener(this);
        mIvQqLogin.setOnClickListener(this);
        mIvWxLogin.setOnClickListener(this);
        mIvSinaLogin.setOnClickListener(this);
        mEtUserName.addTextChangedListener(this);
        mEtPassword.addTextChangedListener(this);
    }

    @Override
    public void initData() {
        mEtUserName.setText(AppContext.getInstance()
                .getProperty("user.account"));
        mEtPassword.setText(CyptoUtils.decode("oschinaApp", AppContext
                .getInstance().getProperty("user.pwd")));
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                handleLogin();
                break;
            case R.id.btn_signin:
                handleSignin();
                break;
            case R.id.tv_forget_password:
                handleForgetPassword();
                break;
            case R.id.iv_qq_login:
                qqLogin();
                break;
            case R.id.iv_wx_login:
                wxLogin();
                break;
            case R.id.iv_sina_login:
                sinaLogin();
                break;
            default:
                break;
        }
    }


    // 获取到QQ授权登陆的信息
    @Override
    public void onComplete(Object o) {
        openIdLogin(OpenIdCatalog.QQ, o.toString());
    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        getLoginInfo();
        if (!StringUtils.isEmpty(mUserName) && !StringUtils.isEmpty(mPassword)) {
            mBtnLogin.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rip_btn_pink));
        } else {
            mBtnLogin.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray_300));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onInteraction(Uri uri) {
        if (mListener != null) {
            mListener.onLoginFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLoginFragmentInteraction(Uri uri);
    }

    private void handleLogin() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return;
        }
        if (mEtUserName.length() == 0) {
            mTilUserName.setError("请输入手机号");
            mEtUserName.requestFocus();
            return;
        } else {
            mTilUserName.setError("");
        }

        if (mEtPassword.length() == 0) {
            mTilPassword.setError("请输入密码");
            mEtPassword.requestFocus();
            return;
        } else {
            mTilPassword.setError("");
        }

        // if the data has ready
        getLoginInfo();

        showWaitDialog(R.string.progress_login);
        AVUser.logInInBackground(mUserName, mPassword, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    //登录成功


                } else {
                    //登录失败

                }
            }
        });
    }

    private void getLoginInfo() {

        if (mEtUserName == null || mEtPassword == null) {
            return;
        }
        mUserName = mEtUserName.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
    }


    private void handleSignin() {


    }

    private void handleForgetPassword() {

        AVUser.requestPasswordResetInBackground("myemail@example.com", new RequestPasswordResetCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 已发送一份重置密码的指令到用户的邮箱
                } else {
                    // 重置密码出错。
                }
            }
        });
    }

    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            LoginUserBean loginUserBean = XmlUtils.toBean(LoginUserBean.class, arg2);
            if (loginUserBean != null) {
                handleLoginBean(loginUserBean);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            AppContext.showToast("网络出错" + arg0);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };


    private void handleLoginSuccess() {

    }

    private boolean prepareForLogin() {


        return true;
    }

    /**
     * QQ登陆
     */
    private void qqLogin() {
        Tencent mTencent = Tencent.createInstance(AppConfig.APP_QQ_KEY,
                mContext);
        mTencent.login(this, "all", this);
    }

    BroadcastReceiver receiver;

    /**
     * 微信登陆
     */
    private void wxLogin() {
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, Constants.WEICHAT_APPID, false);
        api.registerApp(Constants.WEICHAT_APPID);

        if (!api.isWXAppInstalled()) {
            AppContext.showToast("手机中没有安装微信客户端");
            return;
        }
        // 唤起微信登录授权
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_login";
        api.sendReq(req);
        // 注册一个广播，监听微信的获取openid返回（类：WXEntryActivity中）
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OpenIdCatalog.WECHAT);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String openid_info = intent.getStringExtra(LoginBindActivityChooseActivity.BUNDLE_KEY_OPENIDINFO);
                    openIdLogin(OpenIdCatalog.WECHAT, openid_info);
                    // 注销这个监听广播
                    if (receiver != null) {
                        mContext.unregisterReceiver(receiver);
                    }
                }
            }
        };

        mContext.registerReceiver(receiver, intentFilter);
    }

    /**
     * 新浪登录
     */
    private void sinaLogin() {
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
        mController.getConfig().setSsoHandler(sinaSsoHandler);
        mController.doOauthVerify(mContext, SHARE_MEDIA.SINA,
                new SocializeListeners.UMAuthListener() {

                    @Override
                    public void onStart(SHARE_MEDIA arg0) {
                    }

                    @Override
                    public void onError(SocializeException arg0,
                                        SHARE_MEDIA arg1) {
                        AppContext.showToast("新浪授权失败");
                    }

                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA arg1) {
                        if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                            // 获取平台信息
                            mController.getPlatformInfo(mContext, SHARE_MEDIA.SINA, new SocializeListeners.UMDataListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onComplete(int i, Map<String, Object> map) {
                                    if (i == 200 && map != null) {
                                        StringBuilder sb = new StringBuilder("{");
                                        Set<String> keys = map.keySet();
                                        int index = 0;
                                        for (String key : keys) {
                                            index++;
                                            String jsonKey = key;
                                            if (jsonKey.equals("uid")) {
                                                jsonKey = "openid";
                                            }
                                            sb.append(String.format("\"%s\":\"%s\"", jsonKey, map.get(key).toString()));
                                            if (index != map.size()) {
                                                sb.append(",");
                                            }
                                        }
                                        sb.append("}");
                                        openIdLogin(OpenIdCatalog.WEIBO, sb.toString());
                                    } else {
                                        AppContext.showToast("发生错误：" + i);
                                    }
                                }
                            });
                        } else {
                            AppContext.showToast("授权失败");
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA arg0) {
                        AppContext.showToast("已取消新浪登陆");
                    }
                });
    }


    /**
     * @param catalog    第三方登录的类别
     * @param openIdInfo 第三方的信息
     */
    private void openIdLogin(final String catalog, final String openIdInfo) {
        final ProgressDialog waitDialog = DialogHelp.getWaitDialog(mContext, "登陆中...");
        OSChinaApi.open_login(catalog, openIdInfo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                LoginUserBean loginUserBean = XmlUtils.toBean(LoginUserBean.class, responseBody);
                if (loginUserBean.getResult().OK()) {
                    handleLoginBean(loginUserBean);
                } else {
                    // 前往绑定或者注册操作
                    Intent intent = new Intent(mContext, LoginBindActivityChooseActivity.class);
                    intent.putExtra(LoginBindActivityChooseActivity.BUNDLE_KEY_CATALOG, catalog);
                    intent.putExtra(LoginBindActivityChooseActivity.BUNDLE_KEY_OPENIDINFO, openIdInfo);
                    startActivityForResult(intent, REQUEST_CODE_OPENID);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                AppContext.showToast("网络出错" + statusCode);
            }

            @Override
            public void onStart() {
                super.onStart();
                waitDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                waitDialog.dismiss();
            }
        });
    }

    // 处理loginBean
    private void handleLoginBean(LoginUserBean loginUserBean) {
        if (loginUserBean.getResult().OK()) {
            AsyncHttpClient client = ApiHttpClient.getHttpClient();
            HttpContext httpContext = client.getHttpContext();
            CookieStore cookies = (CookieStore) httpContext
                    .getAttribute(ClientContext.COOKIE_STORE);
            if (cookies != null) {
                String tmpcookies = "";
                for (Cookie c : cookies.getCookies()) {
                    TLog.log(TAG,
                            "cookie:" + c.getName() + " " + c.getValue());
                    tmpcookies += (c.getName() + "=" + c.getValue()) + ";";
                }
                TLog.log(TAG, "cookies:" + tmpcookies);
                AppContext.getInstance().setProperty(AppConfig.CONF_COOKIE,
                        tmpcookies);
                ApiHttpClient.setCookie(ApiHttpClient.getCookie(AppContext
                        .getInstance()));
                HttpConfig.sCookie = tmpcookies;
            }
            // 保存登录信息
            loginUserBean.getUser().setAccount(mUserName);
            loginUserBean.getUser().setPwd(mPassword);
            loginUserBean.getUser().setRememberMe(true);
            AppContext.getInstance().saveUserInfo(loginUserBean.getUser());
            hideWaitDialog();
            handleLoginSuccess();

        } else {
            AppContext.getInstance().cleanLoginInfo();
            AppContext.showToast(loginUserBean.getResult().getErrorMessage());
        }
    }
}
