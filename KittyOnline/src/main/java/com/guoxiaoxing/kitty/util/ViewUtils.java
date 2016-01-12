package com.guoxiaoxing.kitty.util;

import android.widget.TextView;

/**
 * View工具类
 * 
 * ViewUtils.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-3-11 上午11:43:58
 */
public class ViewUtils {
    
    /***
     * 设置TextView的划线状态
     * @author 火蚁
     * 2015-3-11 上午11:46:10
     *
     * @return void
     * @param tv
     * @param flag
     */
    public static void setTextViewLineFlag(TextView tv, int flags) {
	tv.getPaint().setFlags(flags);
	tv.invalidate();
    }
}

