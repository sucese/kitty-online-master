package net.oschina.app.widget;

import java.lang.ref.WeakReference;

import net.oschina.app.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.view.View;

public abstract class am extends ReplacementSpan {

	private WeakReference a;
	private String b;
	private int c;
	private int d;
	private int e;
	private int f;
	protected Context g;
	protected Drawable h;
	protected int i;
	protected int j;
	private int k;
	private Drawable l[];
	private int m;
	private int n;
	private Paint o;
	private int p;
	private float q;

	public am(Context context, String s, Drawable drawable) {
		l = new Drawable[2];
		o = new Paint(1);
		g = context.getApplicationContext();
		b = s;
		h = drawable;
		o = new Paint(1);
		d();
	}

	private void a(Canvas canvas) {
		l[k].draw(canvas);
		canvas.save();
		int i1 = (int) Math.ceil((float) (f - j) / 2.0F);
		canvas.translate(m, i1);
		h.draw(canvas);
		canvas.restore();
		canvas.drawText(b, p, q, o);
	}

	private void d() {
		if (TextUtils.isEmpty(b))
			b = "";
		if (b.length() > 7)
			b = (new StringBuilder()).append(b.substring(0, 6)).append("...")
					.toString();
		// a a1 = com.sina.weibo.k.a.a(g);
		Resources resources = g.getResources();
		m = resources
				.getDimensionPixelSize(R.dimen.timeline_small_card_icon_margin_left);
		i = resources
				.getDimensionPixelSize(R.dimen.timeline_small_card_icon_width);
		j = resources
				.getDimensionPixelSize(R.dimen.timeline_small_card_icon_height);
		int i1 = resources
				.getDimensionPixelSize(R.dimen.timeline_small_card_triangle_margin_right);
		int j1 = resources
				.getDimensionPixelSize(R.dimen.timeline_small_card_triangle_margin_left);
		n = resources
				.getDimensionPixelSize(R.dimen.timeline_small_card_title_margin_left);
		int k1 = resources
				.getDimensionPixelSize(R.dimen.timeline_small_card_title_text_size);
		f = resources.getDimensionPixelSize(R.dimen.timeline_small_card_height);
		int l1 = resources.getColor(R.color.main_link_text_color);
		// a1.a(R.color.main_link_text_color);
		int i2 = resources
				.getDimensionPixelSize(R.dimen.timeline_small_card_min_width);
		o.setStyle(Paint.Style.FILL);
		o.setTextSize(k1);
		o.setColor(l1);
		e = j1 + (i1 + ((int) o.measureText(b, 0, b.length()) + (m + i))) + n;
		if (e < i2) {
			int j2 = i2 - e;
			m = m + j2 / 2;
			e = i2;
		}
		Drawable drawable = resources
				.getDrawable(R.drawable.timeline_card_small_button);
		// a1.b(R.drawable.timeline_card_small_button);
		drawable.setBounds(0, 0, e, f);
		l[0] = drawable;
		Drawable drawable1 = resources
				.getDrawable(R.drawable.timeline_card_small_button_highlighted);
		// a1.b(R.drawable.timeline_card_small_button_highlighted);
		drawable1.setBounds(0, 0, e, f);
		l[1] = drawable1;
		if (h == null) {
			h = resources
					.getDrawable(R.drawable.timeline_card_small_placeholder);
			// h = a1.b(R.drawable.timeline_card_small_placeholder);
		}
		h.setBounds(0, 0, i, j);
		p = m + i + n;
		FontMetrics fontmetrics = o.getFontMetrics();
		float f1 = fontmetrics.bottom - (fontmetrics.top + fontmetrics.ascent)
				/ 2.0F;
		q = (float) f - ((float) f - f1) / 2.0F - fontmetrics.bottom;
	}

	private void e(View view) {
		if (a != null)
			a.clear();
		k = 1;
		if (view != null)
			view.invalidate();
	}

	private void f(View view) {
		if (a != null)
			a.clear();
		k = 0;
		if (view != null)
			view.invalidate();
	}

	protected int a() {
		return 0;
	}

	public abstract void a(View view);

	public boolean a(int i1, int j1) {
		Rect rect = c();
		boolean flag;
		if (rect != null)
			flag = rect.intersect(i1, j1, i1, j1);
		else
			flag = false;
		return flag;
	}

	protected int b() {
		return g.getResources().getDimensionPixelSize(
				R.dimen.timeline_small_card_padding_right);
	}

	public void b(View view) {
		f(view);
		a(view);
	}

	public Rect c() {
		Rect rect;
		if (e <= 0 || f <= 0) {
			rect = null;
		} else {
			rect = new Rect();
			rect.left = c;
			rect.top = d;
			rect.right = rect.left + e;
			rect.bottom = rect.top + f;
		}
		return rect;
	}

	public void c(View view) {
		e(view);
	}

	public void d(View view) {
		f(view);
	}

	public void draw(Canvas canvas, CharSequence charsequence, int i1, int j1,
			float f1, int k1, int l1, int i2, Paint paint) {
		canvas.save();
		int j2 = k1 + (i2 - k1 - f) / 2;
		c = (int) f1 + a();
		d = j2 - paint.getFontMetricsInt().descent;
		canvas.translate(c, j2);
		a(canvas);
		canvas.restore();
	}

	public int getSize(Paint paint, CharSequence charsequence, int i1, int j1,
			Paint.FontMetricsInt fontmetricsint) {
		if (fontmetricsint != null) {
			fontmetricsint.ascent = -f;
			fontmetricsint.descent = 0;
			fontmetricsint.top = fontmetricsint.ascent;
			fontmetricsint.bottom = 0;
		}
		return e + a() + b();
	}
}
