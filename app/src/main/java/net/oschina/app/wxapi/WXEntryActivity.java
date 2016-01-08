package net.oschina.app.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import net.oschina.app.R;
import net.oschina.app.api.ApiHttpClient;
import net.oschina.app.bean.Constants;
import net.oschina.app.bean.OpenIdCatalog;
import net.oschina.app.ui.LoginBindActivityChooseActivity;
import net.oschina.app.util.DialogHelp;
import net.oschina.app.util.TLog;

import cz.msebera.android.httpclient.Header;
/**
 * 微信回调的activity
 * Created by zhangdeyi on 15/7/27.
 */
public class WXEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin_entry);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {

            //用户同意
            String code = resp.code;
            String state = resp.state;
            // 如果不是登录
            if (!state.equals("wechat_login")) {
                finish();
            }
            //上面的code就是接入指南里要拿到的code
            getAccessTokenAndOpenId(code);
        } else {
            finish();
        }
    }

    // 使用code获取微信的access_token和openid
    private void getAccessTokenAndOpenId(String code) {
        final ProgressDialog waitDialog = DialogHelp.getWaitDialog(this, "加载中...");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&grant_type=authorization_code&code=%s";
        String tokenUrl = String.format(url, Constants.WEICHAT_APPID, Constants.WEICHAT_SECRET, code);
        ApiHttpClient.getDirect(tokenUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                TLog.log("Test", new String(responseBody));
                String openid_info = new String(responseBody);
                Intent intent = new Intent(OpenIdCatalog.WECHAT);
                intent.putExtra(LoginBindActivityChooseActivity.BUNDLE_KEY_OPENIDINFO, openid_info);
                sendBroadcast(intent);
                finish();
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
}
