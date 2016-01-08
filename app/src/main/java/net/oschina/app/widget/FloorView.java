package net.oschina.app.widget;

import java.util.List;

import net.oschina.app.R;
import net.oschina.app.bean.Comment.Refer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 用来显示PostView中盖楼的自定义控件
 * 
 * @author Aige
 * @since 2014/11/14
 * 
 * @update 去除隐藏楼层的特性
 */
public class FloorView extends LinearLayout {
	private Context context;// 上下文环境引用

	private Drawable drawable;// 背景Drawable

	public FloorView(Context context) {
		this(context, null);
	}

	public FloorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public FloorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;

		// 获取背景Drawable的资源文件
		drawable = context.getResources().getDrawable(
				R.drawable.comment_background);
	}

	/**
	 * 设置Comment数据
	 * 
	 * @param comments
	 *            Comment数据列表
	 */
	public void setComments(List<Refer> refers) {
		// 清除子View
		removeAllViews();

		// 获取评论数
		int count = refers.size();

		/*
		 * 如果评论条数小于9条则直接显示，否则我们只显示评论的头两条和最后一条（这里的最后一条是相对于PostView中已经显示的一条评论来说的）
		 */
		initViewWithAll(refers);
	}

	/**
	 * 初始化所有的View
	 * 
	 * @param comments
	 *            评论数据列表
	 */
	private void initViewWithAll(List<Refer> refers) {
		for (int i = 0; i < refers.size(); i++) {
			View commentView = getView(refers.get(i), i, refers.size(),
					false);
			addView(commentView);
		}
	}

	/**
	 * 初始化带有隐藏楼层的View
	 * 
	 * @param comments
	 *            评论数据列表
	 */
	private void initViewWithHide(final List<Refer> refers) {
		View commentView = null;

		// 初始化一楼
		commentView = getView(refers.get(0), 0, refers.size() - 1, false);
		addView(commentView);

		// 初始化二楼
		commentView = getView(refers.get(1), 1, refers.size() - 1, false);
		addView(commentView);

		// 初始化隐藏楼层标识
		commentView = getView(null, 2, refers.size() - 1, true);
		commentView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initViewWithAll(refers);
			}
		});
		addView(commentView);

		// 初始化倒数第二楼
		commentView = getView(refers.get(refers.size() - 2), 3,
				refers.size() - 1, false);
		addView(commentView);
	}

	/**
	 * 获取单个评论子视图
	 * 
	 * @param comment
	 *            评论对象
	 * @param index
	 *            第几个评论
	 * @param count
	 *            总共有几个评论
	 * @param isHide
	 *            是否是隐藏显示
	 * @return 一个评论子视图
	 */
	private View getView(Refer refer, int index, int count, boolean isHide) {
		// 获取根布局
		View commentView = LayoutInflater.from(context).inflate(
				R.layout.list_cell_reply_name_content, null, false);

		// 获取控件
		TextView tvUserName = (TextView) commentView
				.findViewById(R.id.tv_reply_name);
		TweetTextView tvContent = (TweetTextView) commentView
				.findViewById(R.id.tv_reply_content);
		

		/*
		 * 判断是否是隐藏楼层
		 */
		if (isHide) {
			/*
			 * 是则显示“点击显示隐藏楼层”控件而隐藏其他的不相干控件
			 */
			tvUserName.setVisibility(GONE);
			tvContent.setVisibility(GONE);
		} else {
			/*
			 * 否则隐藏“点击显示隐藏楼层”控件而显示其他的不相干控件
			 */
			tvUserName.setVisibility(VISIBLE);
			tvContent.setVisibility(VISIBLE);

			// 设置显示数据
			tvUserName.setText(refer.getRefertitle());
			
			tvContent.setMovementMethod(MyLinkMovementMethod.a());
			tvContent.setFocusable(false);
			tvContent.setDispatchToParent(true);
			tvContent.setLongClickable(false);
			Spanned rcontent = Html.fromHtml(refer.referbody);
			tvContent.setText(rcontent);
			MyURLSpan.parseLinkText(tvContent, rcontent);
			
		}

		// 设置布局参数
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		// 计算margin指数，这个指数的意义在于将第一个的margin值设置为最大的，然后依次递减体现层叠效果
		int marginIndex = count - index;
		int margin = marginIndex * 8;

		params.setMargins(margin, margin, margin, 0);
		commentView.setLayoutParams(params);

		return commentView;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		/*
		 * 在FloorView绘制子控件前先绘制层叠的背景图片
		 */
		for (int i = getChildCount() - 1; i >= 0; i--) {
			View view = getChildAt(i);
			drawable.setBounds(view.getLeft(), view.getLeft(), view.getRight(),
					view.getBottom());
			drawable.draw(canvas);
		}
		super.dispatchDraw(canvas);
	}
}
