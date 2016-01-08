package net.oschina.app.util;

import android.view.View;
import android.widget.TextView;

import net.oschina.app.AppContext;
import net.oschina.app.R;

/**
 * Created by 火蚁 on 15/5/21.
 */
public class PlatfromUtil {
    public final static int CLIENT_MOBILE = 2;
    public final static int CLIENT_ANDROID = 3;
    public final static int CLIENT_IPHONE = 4;
    public final static int CLIENT_WINDOWS_PHONE = 5;
    public final static int CLIENT_WECHAT = 6;

    public static void setPlatFromString(TextView tv, int platfrom) {
        int resId = R.string.from_mobile;
        tv.setVisibility(View.VISIBLE);
        switch (platfrom) {
            case CLIENT_MOBILE:
                resId = R.string.from_mobile;
                break;
            case CLIENT_ANDROID:
                resId = R.string.from_android;
                break;
            case CLIENT_IPHONE:
                resId = R.string.from_iphone;
                break;
            case CLIENT_WINDOWS_PHONE:
                resId = R.string.from_windows_phone;
                break;
            case CLIENT_WECHAT:
                resId = R.string.from_wechat;
                break;
            default:
                tv.setVisibility(View.GONE);
        }
        String platFromStr = AppContext.getInstance().getResources().getString(resId);
        TypefaceUtils.setTypeFaceWithText(tv, R.string.fa_mobile, platFromStr);
    }
}
