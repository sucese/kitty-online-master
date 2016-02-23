package com.guoxiaoxing.kitty.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.model.LoginUserBean;
import com.guoxiaoxing.kitty.ui.base.BaseActivity;
import com.guoxiaoxing.kitty.util.DialogHelp;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 第三方登陆账号绑定操作
 *
 * @author guoxiaoxing
 */
public class LoginAccountBindOpenIdActivity extends BaseActivity {

    @Bind(R.id.et_user_phone)
    EditText etUsername;
    @Bind(R.id.et_user_password)
    EditText etPassword;

    private String catalog;
    private String openIdInfo;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.login;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_bind_openid;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            catalog = intent.getStringExtra(LoginBindActivityChooseActivity.BUNDLE_KEY_CATALOG);
            openIdInfo = intent.getStringExtra(LoginBindActivityChooseActivity.BUNDLE_KEY_OPENIDINFO);
        }
    }

    @OnClick(R.id.bt_bind)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_bind:
                toBind();
                break;
        }
    }

    private void toBind() {
        if (checkInputInof()) {
            return;
        }

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        final ProgressDialog waitDialog = DialogHelp.getWaitDialog(this, "加载中...");
        OSChinaApi.bind_openid(catalog, openIdInfo, username, password, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                LoginUserBean loginUserBean = XmlUtils.toBean(LoginUserBean.class, responseBody);
                if (loginUserBean != null && loginUserBean.getResult().OK()) {
                    Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(LoginActivity.BUNDLE_KEY_LOGINBEAN, loginUserBean);
                    data.putExtras(bundle);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    DialogHelp.getMessageDialog(LoginAccountBindOpenIdActivity.this, loginUserBean.getResult().getErrorMessage()).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

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

    private boolean checkInputInof() {
        if (etUsername.length() == 0) {
            etUsername.setError("请输入用户名");
            etUsername.requestFocus();
            return true;
        }

        if (etPassword.length() == 0) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return true;
        }
        return false;
    }
}
