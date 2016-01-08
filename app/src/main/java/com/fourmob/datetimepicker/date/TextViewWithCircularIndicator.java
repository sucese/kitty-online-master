package com.fourmob.datetimepicker.date;

import net.oschina.app.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 摘取自https://github.com/flavienlaurent/datetimepicker
 * 
 * @author kymjs
 * 
 */
public class TextViewWithCircularIndicator extends TextView {

    private final int mCircleColor;
    private final Paint mCirclePaint = new Paint();
    private boolean mDrawCircle;
    private final String mItemIsSelectedText;

    public TextViewWithCircularIndicator(Context context,
            AttributeSet attributeSet) {
        super(context, attributeSet);

        Resources res = context.getResources();
        mCircleColor = res.getColor(R.color.blue);
        mItemIsSelectedText = "已选择";

        init();
    }

    private void init() {
        mCirclePaint.setFakeBoldText(true);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setTextAlign(Paint.Align.CENTER);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAlpha(60);
    }

    public void drawIndicator(boolean drawIndicator) {
        mDrawCircle = drawIndicator;
    }

    @Override
    public CharSequence getContentDescription() {
        CharSequence text = getText();
        if (mDrawCircle) {
            text = String.format(mItemIsSelectedText, text);
        }
        return text;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawCircle) {
            int width = getWidth();
            int heigth = getHeight();
            int radius = Math.min(width, heigth) / 2;
            canvas.drawCircle(width / 2, heigth / 2, radius, mCirclePaint);
        }
    }
}