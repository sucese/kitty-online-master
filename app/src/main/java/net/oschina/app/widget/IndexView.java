package net.oschina.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * <p>Created 15/8/27 上午9:35.</p>
 * <p><a href="mailto:730395591@qq.com">Email:730395591@qq.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author 李文龙(LeonLee)
 *
 * 索引界面
 */
public class IndexView extends View {

    private static final String DEFAULT_INDEX = "☆ABCDEFGHIJKLMNOPQRSTUVWXYZ#";

    private static final int[] ATTRS = new int[] {
            android.R.attr.textSize,
            android.R.attr.textColor,
    };

    private char[] indexLetters = DEFAULT_INDEX.toCharArray();

    private Paint paint;
    private int lastIndex = -1;
    private int itemHeight;
    private int offsetY;

    //是否初始化高度
    private boolean isInitItemHeight = false;

    private OnIndexTouchListener indexTouchListener;

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(a.getDimensionPixelSize(0, 12));
        paint.setColor(a.getColor(1, 0xff000000));

        a.recycle();

        setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        isInitItemHeight = false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(indexLetters == null || indexLetters.length == 0) {
            return;
        }

        final int width = getWidth();
        if(!isInitItemHeight) {
            final int height = getHeight() - getPaddingTop() - getPaddingBottom();
            itemHeight = height / indexLetters.length;
            final Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            offsetY = (int) ((itemHeight - (fontMetrics.bottom - fontMetrics.top)) * 0.5
                    - fontMetrics.top + getPaddingTop());
            isInitItemHeight = true;
        }

        int x;
        for(int i = 0; i < indexLetters.length; i ++) {
            String indexStr = String.valueOf(indexLetters[i]);
            x = (int) ((width - paint.measureText(indexStr)) * 0.5);
            canvas.drawText(indexStr, x, offsetY + itemHeight * i, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(itemHeight == 0) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(0x50000000);
            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();
                final int newIndex = (int) (y / itemHeight);
                if (lastIndex != newIndex && 0 <= newIndex && newIndex < indexLetters.length) {
                    lastIndex = newIndex;
                    if (indexTouchListener != null) {
                        indexTouchListener.onIndexTouchMove(indexLetters[newIndex]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setBackgroundColor(0);
                if (indexTouchListener != null) {
                    indexTouchListener.onIndexTouchUp();
                }
                break;
        }
        return true;
    }

    public void setOnIndexTouchListener(OnIndexTouchListener listener) {
        indexTouchListener = listener;
    }

    public interface OnIndexTouchListener {
        public void onIndexTouchMove(char indexLeter);
        public void onIndexTouchUp();
    }
}
