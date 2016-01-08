package net.oschina.app.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseActivity;
import net.oschina.app.bean.LoginUserBean;
import net.oschina.app.bean.OpenIdCatalog;
import net.oschina.app.util.DialogHelp;
import net.oschina.app.util.XmlUtils;

import cz.msebera.android.httpclient.Header;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 账号绑定、账号注册
 * Created by zhangdeyi on 15/7/17.
 */
public class LoginBindActivityChooseActivity extends BaseActivity {

    public static final String BUNDLE_KEY_CATALOG = "bundle_key_catalog";
    public static final String BUNDLE_KEY_OPENIDINFO = "bundle_key_openid_info";

    private String catalog;
    private String openIdInfo;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_bind_choose;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            catalog = intent.getStringExtra(BUNDLE_KEY_CATALOG);
            openIdInfo = intent.getStringExtra(BUNDLE_KEY_OPENIDINFO);
            initCatalogText();
        }
    }

    @InjectView(R.id.tv_openid_tip)
    TextView tvOpenIdTip;
    private void initCatalogText() {
        if (catalog.equals(OpenIdCatalog.QQ)) {
            tvOpenIdTip.setText("你好，QQ用户");
        } else if (catalog.equals(OpenIdCatalog.WECHAT)) {
            tvOpenIdTip.setText("你好，微信用户");
        } else if (catalog.equals(OpenIdCatalog.WEIBO)) {
            tvOpenIdTip.setText("你好，新浪用户");
        } else if (catalog.equals(OpenIdCatalog.GITHUB)) {
            tvOpenIdTip.setText("你好，Github用户");
        }
    }

    @Override
    @OnClick({R.id.bt_bind, R.id.bt_reg})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_bind:
                toBind();
                break;
            case R.id.bt_reg:
                to_reg();
                break;
        }
    }

    private void toBind() {
        Intent intent = new Intent(this, LoginAccountBindOpenIdActivity.class);
        intent.putExtra(BUNDLE_KEY_CATALOG, catalog);
        intent.putExtra(BUNDLE_KEY_OPENIDINFO, openIdInfo);
        startActivityForResult(intent, LoginActivity.REQUEST_CODE_OPENID);
    }

    private void to_reg() {
        final ProgressDialog waitDialog = DialogHelp.getWaitDialog(this, "加载中...");
        OSChinaApi.openid_reg(catalog, openIdInfo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    LoginUserBean loginUserBean = XmlUtils.toBean(LoginUserBean.class, responseBody);
                    if (loginUserBean != null && loginUserBean.getResult().OK()) {
                        backLogin(loginUserBean);
                    } else {
                        if (loginUserBean.getResult() != null) {
                            DialogHelp.getMessageDialog(LoginBindActivityChooseActivity.this, loginUserBean.getResult().getErrorMessage()).show();
                        } else {
                            DialogHelp.getMessageDialog(LoginBindActivityChooseActivity.this, "使用第三方注册失败").show();
                        }
                    }
                } catch (Exception e) {
                    DialogHelp.getMessageDialog(LoginBindActivityChooseActivity.this, "使用第三方注册失败").show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                DialogHelp.getMessageDialog(LoginBindActivityChooseActivity.this, "网络出错" + statusCode).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LoginActivity.REQUEST_CODE_OPENID:
                if (data == null) {
                    return;
                }
                LoginUserBean loginUserBean = (LoginUserBean) data.getSerializableExtra(LoginActivity.BUNDLE_KEY_LOGINBEAN);
                backLogin(loginUserBean);
                break;
        }
    }

    private void backLogin(LoginUserBean loginUserBean) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LoginActivity.BUNDLE_KEY_LOGINBEAN, loginUserBean);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
    }
}
