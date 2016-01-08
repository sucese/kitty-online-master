package net.oschina.app.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import net.oschina.app.AppContext;

/**
 * Typeface工具类 用于设置字体图标
 * <p/>
 * TypefaceUtil.java
 *
 * @author 火蚁(http://my.oschina.net/u/253900)
 * @data 2015-3-4 下午3:05:43
 */
public class TypefaceUtils {

    private static Typeface getTypeface() {
        Context context = AppContext.getInstance();

        Typeface font = Typeface.createFromAsset(context.getAssets(),
                "fontawesome-webfont.ttf");
        return font;
    }

    public static void setTypefaceWithColor(TextView tv, int textId,
                                            String colorStr) {
        try {
            int color = Color.parseColor(colorStr);
            tv.setTextColor(color);
        } catch (Exception e) {
        }
        setTypeface(tv, textId);
    }

    public static void setTypefaceWithColor(TextView tv, int textId, int colorId) {
        tv.setTextColor(AppContext.getInstance().getResources()
                .getColor(colorId));
        setTypeface(tv, textId);
    }

    public static void setTypefaceWithColor(TextView tv, int colorId) {
        tv.setTextColor(AppContext.getInstance().getResources()
                .getColor(colorId));
        setTypeface(tv);
    }

    public static void setTypefaceWithColor(TextView tv, String colorStr) {
        try {
            int color = Color.parseColor(colorStr);
            tv.setTextColor(color);
        } catch (Exception e) {
        }

        setTypeface(tv);
    }

    public static void setTypeface(TextView tv, int textId) {
        setTypeface(tv, AppContext.getInstance().getString(textId));
    }

    public static void setTypeface(TextView tv, String text) {
        if (StringUtils.isEmpty(text))
            return;
        tv.setText(text);
        setTypeface(tv);
    }

    public static void setTypeFaceWithText(TextView tv, int faRes, String text) {
        String lastText = AppContext.getInstance().getResources().getString(faRes) + " " + text;
        setTypeface(tv, lastText);
    }

    public static void setTypeface(TextView tv) {
        tv.setTypeface(getTypeface());
    }
}
