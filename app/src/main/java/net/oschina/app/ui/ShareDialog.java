package net.oschina.app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.bean.Constants;
import net.oschina.app.ui.dialog.CommonDialog;
import net.oschina.app.util.TDevice;

/**
 * 分享界面dialog
 *
 * @author kymjs
 *
 */
public class ShareDialog extends CommonDialog implements
        android.view.View.OnClickListener {

    private Context context;
    private String title;
    private String content;
    private String link;

    final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    private ShareDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
        this.context = context;
    }

    @SuppressLint("InflateParams")
    private ShareDialog(Context context, int defStyle) {
        super(context, defStyle);
        this.context = context;
        View shareView = getLayoutInflater().inflate(
                R.layout.dialog_cotent_share, null);
        shareView.findViewById(R.id.ly_share_qq).setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_copy_link)
                .setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_more_option).setOnClickListener(
                this);
        shareView.findViewById(R.id.ly_share_sina_weibo).setOnClickListener(
                this);
        shareView.findViewById(R.id.ly_share_weichat).setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_weichat_circle)
                .setOnClickListener(this);
        setContent(shareView, 0);
    }

    public ShareDialog(Context context) {
        this(context, R.style.dialog_bottom);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    // 设置需要分享的内容
    public void setShareInfo(String title, String content, String link) {
        this.title = title;
        this.content = content;
        this.link = link;
    }

    @Override
    public void onClick(View v) {
        if (!checkCanShare()) {
            AppContext.showToast("内容加载中，请稍候...");
            return;
        }
        switch (v.getId()) {
            case R.id.ly_share_weichat_circle:
                shareToWeiChatCircle();
                break;
            case R.id.ly_share_weichat:
                shareToWeiChat();
                break;
            case R.id.ly_share_sina_weibo:
                shareToSinaWeibo();
                break;
            case R.id.ly_share_qq:
                shareToQQ();
                break;
            case R.id.ly_share_copy_link:
                TDevice.copyTextToBoard(this.link);
                break;
            case R.id.ly_share_more_option:
                TDevice.showSystemShareOption((Activity)this.context,
                        this.content, this.title);
                break;
            default:
                break;
        }
        this.dismiss();
    }

    @SuppressWarnings("deprecation")
    private void shareToWeiChatCircle() {
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this.context,
                Constants.WEICHAT_APPID);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        // 设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(this.content);
        // 设置朋友圈title
        circleMedia.setTitle(this.title);
        circleMedia.setShareImage(getShareImg());
        circleMedia.setTargetUrl(this.link);
        mController.setShareMedia(circleMedia);
        mController.postShare(this.context, SHARE_MEDIA.WEIXIN_CIRCLE, null);
    }

    @SuppressWarnings("deprecation")
    private void shareToWeiChat() {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this.context,
                Constants.WEICHAT_APPID);
        wxHandler.addToSocialSDK();
        // 设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        // 设置分享文字
        weixinContent.setShareContent(this.content);
        // 设置title
        weixinContent.setTitle(this.title);
        // 设置分享内容跳转URL
        weixinContent.setTargetUrl(this.link);
        // 设置分享图片
        weixinContent.setShareImage(getShareImg());
        mController.setShareMedia(weixinContent);
        mController.postShare(this.context, SHARE_MEDIA.WEIXIN, null);
    }

    private void shareToSinaWeibo() {
        // 设置新浪微博SSO handler
        SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
        sinaSsoHandler.setTargetUrl(this.link);
        mController.setShareType(ShareType.SHAKE);
        mController.setShareContent(this.content + " " + this.link);
        mController.setShareImage(getShareImg());
        mController.getConfig().setSsoHandler(sinaSsoHandler);

        if (OauthHelper.isAuthenticated(this.context, SHARE_MEDIA.SINA)) {
            mController.directShare(this.context, SHARE_MEDIA.SINA, null);
        } else {
            mController.doOauthVerify(this.context, SHARE_MEDIA.SINA,
                    new SocializeListeners.UMAuthListener() {

                        @Override
                        public void onStart(SHARE_MEDIA arg0) {
                        }

                        @Override
                        public void onError(SocializeException arg0,
                                            SHARE_MEDIA arg1) {
                        }

                        @Override
                        public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
                            mController.directShare(ShareDialog.this.context, SHARE_MEDIA.SINA, null);
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA arg0) {}
                    });
        }
    }

    private void shareToQQ() {
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity)this.context,
                Constants.QQ_APPID, Constants.QQ_APPKEY);
        qqSsoHandler.setTargetUrl(this.link);
        qqSsoHandler.setTitle(this.title);
        qqSsoHandler.addToSocialSDK();
        mController.setShareContent(this.content);
        mController.setShareImage(getShareImg());
        mController.postShare(this.context, SHARE_MEDIA.QQ, null);
    }

    private UMImage getShareImg() {
        UMImage img = new UMImage(this.context, R.drawable.ic_share);
        return img;
    }

    private boolean checkCanShare() {
        boolean canShare = true;
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(link)) {
            canShare = false;
        }
        return canShare;
    }
}
