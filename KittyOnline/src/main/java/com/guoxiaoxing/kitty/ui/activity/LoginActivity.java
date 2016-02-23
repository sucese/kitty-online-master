package com.guoxiaoxing.kitty.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.guoxiaoxing.kitty.AppConfig;
import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.api.ApiHttpClient;
import com.guoxiaoxing.kitty.model.Constants;
import com.guoxiaoxing.kitty.model.LoginUserBean;
import com.guoxiaoxing.kitty.ui.base.BaseActivity;
import com.guoxiaoxing.kitty.ui.fragment.LoginFragment;
import com.guoxiaoxing.kitty.ui.fragment.SigninFragment;
import com.guoxiaoxing.kitty.util.TLog;
import com.loopj.android.http.AsyncHttpClient;

import org.kymjs.kjframe.http.HttpConfig;

import butterknife.Bind;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * 用户登录界面
 *
 * @author guoxiaoxing
 */
public class LoginActivity extends BaseActivity implements LoginFragment.OnFragmentInteractionListener,
        SigninFragment.OnFragmentInteractionListener {


    public static final int REQUEST_CODE_INIT = 0;
    public static final String BUNDLE_KEY_REQUEST_CODE = "BUNDLE_KEY_REQUEST_CODE";
    public static final String TAG = LoginActivity.class.getSimpleName();
    public final int requestCode = REQUEST_CODE_INIT;

    public static final int REQUEST_CODE_OPENID = 1000;
    // 登陆实体类
    public static final String BUNDLE_KEY_LOGINBEAN = "bundle_key_loginbean";

    @Bind(R.id.tv_tb_title)
    TextView mTvTitle;
    @Bind(R.id.btn_signin)
    TextView mBtnSignin;
    @Bind(R.id.tb_login_activity)
    Toolbar mToolbar;

    private LoginFragment mLoginFragment;
    private SigninFragment mSigninFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

        if (findViewById(R.id.fl_login_activity_container) != null) {

            mLoginFragment = LoginFragment.newInstance("", "");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(R.id.fl_login_activity_container,
                    mLoginFragment).commit();
        }

        mBtnSignin.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void setActionBar() {
        mTvTitle.setText(R.string.login);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_OPENID:
                if (data == null) {
                    return;
                }
                LoginUserBean loginUserBean = (LoginUserBean) data.getSerializableExtra(BUNDLE_KEY_LOGINBEAN);
                if (loginUserBean != null) {
                    handleLoginBean(loginUserBean);
                }
                break;
            default:

                break;
        }
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {

            case R.id.btn_signin:
                handleSignin();
                break;
            case R.id.tv_forget_password:

            default:
                break;
        }

    }

    private void handleSignin() {
        mSigninFragment = SigninFragment.newInstance("", "");
        mBtnSignin.setVisibility(View.GONE);
        mTvTitle.setText(R.string.signin);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fl_login_activity_container, mSigninFragment);
        transaction.commit();
    }

    private void handleLoginSuccess() {
        Intent data = new Intent();
        data.putExtra(BUNDLE_KEY_REQUEST_CODE, requestCode);
        setResult(RESULT_OK, data);
        this.sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
        finish();
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
//            loginUserBean.getUser().setAccount(mUserName);
//            loginUserBean.getUser().setPwd(mPassword);
//            loginUserBean.getUser().setRememberMe(true);
            AppContext.getInstance().saveUserInfo(loginUserBean.getUser());
            hideWaitDialog();
            handleLoginSuccess();

        } else {
            AppContext.getInstance().cleanLoginInfo();
            AppContext.showToast(loginUserBean.getResult().getErrorMessage());
        }
    }

    @Override
    public void onLoginFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSigninFragmentInteraction(Uri uri) {

        mTvTitle.setText(R.string.login);
        mBtnSignin.setVisibility(View.VISIBLE);

    }
}
