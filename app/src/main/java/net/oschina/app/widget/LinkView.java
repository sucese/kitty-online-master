package net.oschina.app.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.oschina.app.util.URLsUtils;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * @author HuangWenwei
 * 
 * @date 2014年10月10日
 */
public class LinkView extends TextView {
	private OnLinkClickListener mLinkClickListener = new LinkView.OnLinkClickListener() {
		@Override
		public void onLinkClick() {

		}
	};

	public LinkView(Context context) {
		super(context);
	}

	public LinkView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LinkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public OnLinkClickListener getLinkClickListener() {
		return mLinkClickListener;
	}

	public void setLinkClickListener(OnLinkClickListener linkClickListener) {
		this.mLinkClickListener = linkClickListener;
	}

	public final static Pattern WECHAT_REG = Pattern
			.compile("(<{1}img[\\s]+class=\"wechat-emoji\"[\\s]+src=\"[^<]+\"[\\s]+alt=\"([^<\\s\"]+)\"[\\s]+[^<]*[>]{1})");

	private static String filterWechat(String linktxt) {
		System.out.println(linktxt);
		if (null == linktxt)
			return "";
		try {
			Matcher match = WECHAT_REG.matcher(linktxt);
			if (null == match)
				return linktxt;
			while (match.find()) {
				String target = match.group(1);
				String rp = match.group(2);
				linktxt = linktxt.replace(target, "[" + rp + "]");
			}
		} catch (Exception e) {
			return linktxt;
		}
		return linktxt;
	}

	public void setLinkText(String linktxt) {
		linktxt = filterWechat(linktxt);
		Spanned span = Html.fromHtml(linktxt);
		setText(span);
		setMovementMethod(LinkMovementMethod.getInstance());
		parseLinkText(span);
	}

	public void parseLinkText(Spanned spanhtml) {
		CharSequence text = getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);

			URLSpan[] htmlurls = spanhtml != null ? spanhtml.getSpans(0, end,
					URLSpan.class) : new URLSpan[] {};

			if (urls.length == 0 && htmlurls.length == 0)
				return;

			SpannableStringBuilder style = new SpannableStringBuilder(text);
			// style.clearSpans();// 这里会清除之前所有的样式
			for (URLSpan url : urls) {
				if (!isNormalUrl(url)) {
					style.removeSpan(url);// 只需要移除之前的URL样式，再重新设置
					NoLinkSpan span = new NoLinkSpan(url.getURL());
					style.setSpan(span, sp.getSpanStart(url),
							sp.getSpanEnd(url),
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
					continue;
				}
				style.removeSpan(url);// 只需要移除之前的URL样式，再重新设置
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url),
						sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			for (URLSpan url : htmlurls) {
				style.removeSpan(url);// 只需要移除之前的URL样式，再重新设置
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan, spanhtml.getSpanStart(url),
						spanhtml.getSpanEnd(url),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			setText(style);
		}
	}

	public void parseLinkText() {
		parseLinkText(null);
	}

	public class MyURLSpan extends ClickableSpan {
		private String mUrl;

		public MyURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {
			mLinkClickListener.onLinkClick();
			URLsUtils urls = URLsUtils.parseURL(mUrl);
			if (urls != null) {
				//UIHelper.showLinkRedirect(widget.getContext(),
					//	urls.getObjType(), urls.getObjId(), urls.getObjKey());
			} else {
			//	UIHelper.openBrowser(widget.getContext(), mUrl);
			}
		}
	}

	public class NoLinkSpan extends ClickableSpan {
		private String text;
		public NoLinkSpan(String text) {
			super();
			this.text = text;
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(Color.BLACK);
			ds.setUnderlineText(false); // 去掉下划线
		}

		@Override
		public void onClick(View widget) {
		}

	}

	public interface OnLinkClickListener {
		void onLinkClick();
	}

	/**
	 * 过滤掉一些不正常的链接
	 * @param url
	 * @return
	 */
	public boolean isNormalUrl(URLSpan url) {
		String urlStr = url.getURL();
		if (urlStr.endsWith(".sh")) {
			return false;
		}
		return true;
	}
}
