package net.oschina.app.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import net.oschina.app.R;

/**
 * 分享界面dialog
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
public class ImageMenuDialog extends Dialog implements
        android.view.View.OnClickListener {

    public interface OnMenuClickListener {
        void onClick(TextView menuItem);
    }

    private OnMenuClickListener mListener;

    private ImageMenuDialog(Context context, boolean flag,
            OnCancelListener listener) {
        super(context, flag, listener);
    }

    @SuppressLint("InflateParams")
    private ImageMenuDialog(Context context, int defStyle) {
        super(context, defStyle);
        View view = getLayoutInflater().inflate(R.layout.dialog_image_menu,
                null);
        view.findViewById(R.id.menu1).setOnClickListener(this);
        view.findViewById(R.id.menu2).setOnClickListener(this);
        view.findViewById(R.id.menu3).setOnClickListener(this);
        super.setContentView(view);
    }

    public ImageMenuDialog(Context context) {
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

    public void setOnMenuClickListener(OnMenuClickListener lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClick((TextView) v);
        }
    }
}
