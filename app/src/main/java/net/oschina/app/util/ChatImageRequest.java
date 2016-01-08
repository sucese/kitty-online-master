package net.oschina.app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.kymjs.kjframe.bitmap.ImageRequest;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpHeaderParser;
import org.kymjs.kjframe.http.KJHttpException;
import org.kymjs.kjframe.http.NetworkResponse;
import org.kymjs.kjframe.http.Response;
import org.kymjs.kjframe.utils.KJLoger;

import java.util.Map;


/**
 * Created by fants on 15-9-17.
 * http://my.oschina.net/fants
 */
public class ChatImageRequest extends ImageRequest {

    private int mMaxWidth;
    private int mMaxHeight;
    private int mMinWidth;
    private int mMinHeight;
    // 用来保证当前对象只有一个线程在访问
    private static final Object sDecodeLock = new Object();

    public ChatImageRequest(String url, int maxWidth, int maxHeight, int minWidth, int minHeight, HttpCallBack callback) {
        super(url, maxWidth, maxHeight, callback);
        mMaxWidth = maxWidth;
        mMaxHeight = maxHeight;
        mMinWidth = minWidth;
        mMinHeight = minHeight;
    }

    public ChatImageRequest(String url, int maxWidth, int maxHeight, HttpCallBack callback) {
        super(url, maxWidth, maxHeight, callback);
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary,
                                           int actualPrimary, int actualSecondary) {
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    @Override
    public Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
        synchronized (sDecodeLock) {
            try {
                return doParse(response);
            } catch (OutOfMemoryError e) {
                KJLoger.debug("Caught OOM for %d byte image, url=%s",
                        response.data.length, getUrl());
                return Response.error(new KJHttpException(e));
            }
        }
    }

    private Response<Bitmap> doParse(NetworkResponse response) {
        byte[] data = response.data;
        BitmapFactory.Options option = new BitmapFactory.Options();
        Bitmap bitmap = null;
        if (mMaxWidth <= 0 && mMaxHeight <= 0) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, option);
        } else {
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, option);
            int outWidth = option.outWidth;
            int outHeight = option.outHeight;

            if (outWidth < mMaxWidth && outHeight < mMaxHeight) {
                option.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, option);
            } else {

                // 计算出图片应该显示的宽高
                int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                        outWidth, outHeight);
                int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                        outHeight, outWidth);

                //如果计算后的宽度小于要求的最小宽度
                //TLog.error("处理前的size：" + desiredWidth + " x " + desiredHeight);

                //处理长图情况，例如一张。400*8000的图，或8000*400的图。
                //这里 要么 宽小于预设的最小宽度，要么高小于预设的最小高度。不会出现 管和高都小于最小宽高度的情况。
                //宽度小于最小值
                if (desiredWidth < mMinWidth) {
                    float scale = (float) desiredWidth / mMinWidth;
                    desiredWidth = mMinWidth;
                    desiredHeight = (int) (desiredHeight / scale);
                } else
                //高度小于最小值
                if (desiredHeight < mMinHeight) {
                    float scale = (float) desiredHeight / mMinHeight;
                    desiredHeight = mMinHeight;
                    desiredWidth = (int) (desiredWidth / scale);
                }

                //TLog.error("处理后的size：" + desiredWidth + " x " + desiredHeight);
                option.inJustDecodeBounds = false;
                option.inSampleSize = findBestSampleSize(outWidth, outHeight, desiredWidth, desiredHeight);

                Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, option);

                // 做缩放
                if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                    bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                    tempBitmap.recycle();
                    //缩放完后如果还是大，剪裁.
                    //分两种情况，一种是高超标，一种是宽超标
                    if (bitmap.getWidth() > mMaxWidth || bitmap.getHeight() > mMaxHeight) {
                        tempBitmap = bitmap;
                        int width = bitmap.getWidth() > mMaxWidth ? mMaxWidth : bitmap.getWidth();
                        int height = bitmap.getHeight() > mMaxHeight ? mMaxHeight : bitmap.getHeight();
                        bitmap = cropImage(tempBitmap, width, height);
                        tempBitmap.recycle();
                    }
                } else {
                    bitmap = tempBitmap;
                }
            }
        }

        if (bitmap == null) {
            return Response.error(new KJHttpException(response));
        } else {
            Response<Bitmap> b = Response.success(bitmap, response.headers,
                    HttpHeaderParser.parseCacheHeaders(mConfig, response));
            return b;
        }
    }

    /**
     * 剪裁图片
     * @param bitmap 原图
     * @param width 剪裁的宽度
     * @param height 剪裁的高度
     * @return
     */
    private Bitmap cropImage(Bitmap bitmap, int width, int height) {
        return Bitmap.createBitmap(bitmap, 0, 0, width, height);
    }

    @Override
    protected void deliverResponse(Map<String, String> header, Bitmap response) {
        if (mCallback != null) {
            mCallback.onSuccess(response);
        }
    }

    static int findBestSampleSize(int actualWidth, int actualHeight,
                                  int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }
        return (int) n;
    }
}