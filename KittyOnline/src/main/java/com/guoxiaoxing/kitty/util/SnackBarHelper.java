package com.guoxiaoxing.kitty.util;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * SnackBar帮助类
 *
 * @author guoxiaoxing
 */
public class SnackBarHelper {


    private static final String PINK_PRIMARY = "#E91E63";

    public static void showSnackBar(View container, String message) {

        Snackbar snackbar = Snackbar.make(container, message, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        view.setBackgroundColor(Color.parseColor(PINK_PRIMARY));
        snackbar.show();

    }

    public static void showCustomSnackBar() {

    }
}
