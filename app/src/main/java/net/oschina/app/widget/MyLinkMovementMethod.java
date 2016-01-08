package net.oschina.app.widget;

import net.oschina.app.R;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

public class MyLinkMovementMethod extends LinkMovementMethod {

	private static MyLinkMovementMethod d;
	private am a;
	private ClickableSpan b;
	private int c;

	public MyLinkMovementMethod() {
		c = 90;
	}

	public static MovementMethod a() {
		if (d == null)
			d = new MyLinkMovementMethod();
		return d;
	}

	private void a(TextView textview, Spannable span, ClickableSpan clickSpan) {
		if (clickSpan != null && a(span, clickSpan)) {
			span.setSpan(new BackgroundColorSpan(0),
					span.getSpanStart(clickSpan), span.getSpanEnd(clickSpan),
					33);
			Selection.removeSelection(span);
		}
	}

	private boolean a(Spannable spannable, ClickableSpan clickablespan) {
		boolean flag = false;
		int i = spannable.getSpanStart(clickablespan);
		int j = spannable.getSpanEnd(clickablespan);
		if (i >= 0 && j > i)
			flag = true;
		return flag;
	}

	public void a(TextView textview) {
		if (a != null)
			a.d(textview);
	}

	public boolean onTouchEvent(TextView textview, Spannable spannable,
			MotionEvent event) {
		int i = event.getAction();
		int j = (int) event.getX();
		int k = (int) event.getY();
		int l = j - textview.getTotalPaddingLeft();
		int i1 = k - textview.getTotalPaddingTop();
		int j1 = l + textview.getScrollX();
		int k1 = i1 + textview.getScrollY();
		boolean flag = false;
		boolean flag1;

		if (i == 0) {
			Layout layout2 = textview.getLayout();
			int k2 = layout2.getLineForVertical(k1);
			int l2 = layout2.getOffsetForHorizontal(k2, j1);
			int i3 = layout2.getOffsetForHorizontal(k2, j1 + c);
			am aam1[] = spannable.getSpans(l2, l2, am.class);
			ClickableSpan aclickablespan1[];
			ClickableSpan clickablespan1;
			if (aam1 != null && aam1.length != 0) {
				am am2 = aam1[0];
				if (am2 != null && am2.a(j1, k1)) {
					a = aam1[0];
					a.c(textview);
				} else {
					am2.d(textview);
				}
				flag = true;
			}
			if (l2 != i3) {
				aclickablespan1 = (ClickableSpan[]) spannable.getSpans(l2, l2,
						ClickableSpan.class);
				if (aclickablespan1 != null && aclickablespan1.length != 0) {
					clickablespan1 = aclickablespan1[0];
					if (clickablespan1 != null && a(spannable, clickablespan1)) {
						b = clickablespan1;
						Selection.removeSelection(spannable);
						spannable
								.setSpan(
										new BackgroundColorSpan(
												textview.getContext()
														.getResources()
														.getColor(
																R.color.timeline_clickable_text_highlighted_background)),
										spannable.getSpanStart(clickablespan1),
										spannable.getSpanEnd(clickablespan1),
										33);
					}
				}
			} else {
				flag = true;
			}
		} else if (i == 2) {
			if (a == null) {
				Layout layout1 = textview.getLayout();
				int j2 = layout1.getOffsetForHorizontal(
						layout1.getLineForVertical(k1), j1);
				am aam[] = (am[]) spannable.getSpans(j2, j2, am.class);
				if (aam != null && aam.length != 0) {
					am am1 = aam[0];
					if (am1 != null && am1.a(j1, k1)) {
						a = aam[0];
						a.c(textview);
					} else {
						am1.d(textview);
					}
					flag = true;
				}
			}
			if (a != null && !a.a(j1, k1)) {
				a.d(textview);
				a = null;
				flag = true;
			}
			if (b == null) {
				ClickableSpan aclickablespan[];
				ClickableSpan clickablespan;

				Layout layout = textview.getLayout();
				int l1 = layout.getLineForVertical(k1);
				int i2 = layout.getOffsetForHorizontal(l1, j1);
				if (i2 != layout.getOffsetForHorizontal(l1, j1 + c)) {
					aclickablespan = (ClickableSpan[]) spannable.getSpans(i2,
							i2, ClickableSpan.class);
					if (aclickablespan != null && aclickablespan.length != 0) {
						clickablespan = aclickablespan[0];
						if (clickablespan != null
								&& a(spannable, clickablespan)) {
							b = clickablespan;
							Selection.removeSelection(spannable);
							spannable
									.setSpan(
											new BackgroundColorSpan(
													textview.getContext()
															.getResources()
															.getColor(
																	R.color.timeline_clickable_text_highlighted_background)),
											spannable
													.getSpanStart(clickablespan),
											spannable.getSpanEnd(clickablespan),
											33);
						}
					}
				} else {
					flag = true;
				}
			}
		} else if (i == 1) {
			if (a != null) {
				if (a.a(j1, k1))
					a.b(textview);
				else
					a.d(textview);
				a = null;
				flag = true;
			}
			a(textview, spannable, b);
		} else if (i == 3) {
			if (a != null) {
				a.d(textview);
				a = null;
				flag = true;
			}
			a(textview, spannable, b);
		}
		if (flag) {
			Selection.removeSelection(spannable);
			flag1 = true;
		} else {
			flag1 = super.onTouchEvent(textview, spannable, event);
		}
		return flag1;
	}
}
