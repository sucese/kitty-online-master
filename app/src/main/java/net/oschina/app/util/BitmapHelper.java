package net.oschina.app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

/**
 * @author kymjs (http://www.kymjs.com/) on 1/4/16.
 */
public class BitmapHelper {
    public BitmapHelper() {
    }

    public static BitmapFactory.Options calculateInSampleSize(BitmapFactory.Options options, int 
            reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Bitmap imageZoom(Bitmap bitmap, double maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] b = baos.toByteArray();
        double mid = (double) (b.length / 1024);
        double i = mid / maxSize;
        if (i > 1.0D) {
            bitmap = scaleWithWH(bitmap, (double) bitmap.getWidth() / Math.sqrt(i), (double) 
                    bitmap.getHeight() / Math.sqrt(i));
        }

        return bitmap;
    }

    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w != 0.0D && h != 0.0D && src != null) {
            int width = src.getWidth();
            int height = src.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidth = (float) (w / (double) width);
            float scaleHeight = (float) (h / (double) height);
            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        } else {
            return src;
        }
    }

    public static Bitmap scaleWithMatrix(Bitmap src, Matrix scaleMatrix) {
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), scaleMatrix, true);
    }

    public static Bitmap scaleWithXY(Bitmap src, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap scaleWithXY(Bitmap src, float scaleXY) {
        return scaleWithXY(src, scaleXY, scaleXY);
    }

    public static Bitmap rotate(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, 
                true);
    }

    public static void doRecycledIfNot(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

    }
}

