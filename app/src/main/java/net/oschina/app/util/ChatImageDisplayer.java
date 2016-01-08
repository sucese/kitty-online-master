package net.oschina.app.util;

import android.graphics.Bitmap;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.bitmap.BitmapConfig;
import org.kymjs.kjframe.bitmap.ImageDisplayer;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.KJHttpException;
import org.kymjs.kjframe.http.Request;

/**
 * Created by fants on 15-9-17.
 * http://my.oschina.net/fants
 */
public class ChatImageDisplayer extends ImageDisplayer {

    private int mMaxWidth;
    private int mMaxHeight;
    private int mMinWidth;
    private int mMinHeight;

    public ChatImageDisplayer(BitmapConfig bitmapConfig) {
        super(Core.getKJHttp(), bitmapConfig);
    }

    public void setImageSize(int maxWidth, int maxHeight, int minWidth, int minHeight) {
        this.mMaxWidth = maxWidth;
        this.mMaxHeight = maxHeight;
        this.mMinWidth = minWidth;
        this.mMinHeight = minHeight;
    }

    protected Request<Bitmap> makeImageRequest(final String requestUrl, int maxWidth, int
            maxHeight) {
        //原本的ImageRequest 换成 ChatImageRequest
        return new ChatImageRequest(requestUrl, mMaxWidth, mMaxHeight, mMinWidth, mMinHeight,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(Bitmap t) {
                        super.onSuccess(t);
                        onGetImageSuccess(requestUrl, t);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        onGetImageError(requestUrl, new KJHttpException(strMsg));
                    }
                });
    }
}
