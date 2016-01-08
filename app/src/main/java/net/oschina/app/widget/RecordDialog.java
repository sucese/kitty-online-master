package net.oschina.app.widget;

import net.oschina.app.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

/**
 * 弹出的指示音量大小的dialog
 * 
 * @author kymjs(kymjs123@gmail.com)
 * 
 */
public class RecordDialog extends Dialog {

    private ImageView mImgVolume;

    public RecordDialog(Context context) {
        super(context);
        init();
    }

    public RecordDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected RecordDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = View
                .inflate(getContext(), R.layout.dialog_record, null);
        mImgVolume = (ImageView) rootView
                .findViewById(R.id.dialog_img_record_volume);
        setContentView(rootView);
    }

    public void setImageResource(int resId) {
        mImgVolume.setImageResource(resId);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mImgVolume.setImageBitmap(bitmap);
    }

    public void setImageDrawable(Drawable drawable) {
        mImgVolume.setImageDrawable(drawable);
    }

}
