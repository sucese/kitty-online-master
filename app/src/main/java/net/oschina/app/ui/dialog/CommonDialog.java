package net.oschina.app.ui.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.util.TDevice;

public class CommonDialog extends Dialog {
    public DialogInterface.OnClickListener listener;
    protected View barDivider;
    protected View buttonDivider;
    protected FrameLayout container;
    protected View content;
    private final int contentPadding;

    protected DialogTitleView headerVw;
    protected Button negativeBt;
    protected Button positiveBt;
    protected DialogInterface.OnClickListener dismissClick = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    public CommonDialog(Context context) {
        this(context, R.style.dialog_common);
    }

    public CommonDialog(Context context, int defStyle) {
        super(context, defStyle);
        contentPadding = (int) getContext().getResources().getDimension(
                R.dimen.global_dialog_padding);
        init(context);
    }

    protected CommonDialog(Context context, boolean flag,
                           DialogInterface.OnCancelListener listener) {
        super(context, flag, listener);
        contentPadding = (int) getContext().getResources().getDimension(
                R.dimen.global_dialog_padding);
        init(context);
    }

    @SuppressLint("InflateParams")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void init(final Context context) {
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        content = LayoutInflater.from(context).inflate(
                R.layout.dialog_common, null);
        headerVw = (DialogTitleView) content.findViewById(R.id.dialog_header);
        container = (FrameLayout) content.findViewById(R.id.content_container);
        barDivider = content.findViewById(R.id.button_bar_divider);
        buttonDivider = content.findViewById(R.id.button_divder);
        positiveBt = (Button) content.findViewById(R.id.positive_bt);
        negativeBt = (Button) content.findViewById(R.id.negative_bt);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // TODO Check content view height and change height

        } else {
//			content.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//
//				@Override
//				public void onLayoutChange(View v, int left, int top,
//						int right, int bottom, int oldLeft, int oldTop,
//						int oldRight, int oldBottom) {
//					int height = v.getHeight();
//					int contentHeight = container.getHeight();
//					int winHeight = BaseApplication.getDisplaySize()[1];
//					int needHeight = height - winHeight * 8 / 10;
//					if (needHeight > 0) {
//						container
//								.setLayoutParams(new LinearLayout.LayoutParams(
//										LayoutParams.MATCH_PARENT,
//										contentHeight - needHeight));
//					}
//				}
//			});
        }
        super.setContentView(content);
    }

    public TextView getTitleTextView() {
        return headerVw.titleTv;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (TDevice.isTablet()) {
            int maxWidth = (int) TDevice.dpToPixel(360f);
            if (maxWidth < TDevice.getScreenWidth()) {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.width = maxWidth;
                getWindow().setAttributes(params);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }

    public void setContent(View view) {
        setContent(view, contentPadding);
    }

    public void setContent(View view, int padding) {
        container.removeAllViews();
        container.setPadding(padding, padding, padding, padding);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        container.addView(view, lp);
    }

    @Override
    public void setContentView(int i) {
        setContent(null);
    }

    @Override
    public void setContentView(View view) {
        setContentView(null, null);
    }

    @Override
    public void setContentView(View view,
                               android.view.ViewGroup.LayoutParams layoutparams) {
        throw new Error("Dialog: User setContent (View view) instead!");
    }

    public void setItems(BaseAdapter adapter,
                         AdapterView.OnItemClickListener onItemClickListener) {
        ListView listview = new ListView(content.getContext());
        listview.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        listview.setDivider(null);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onItemClickListener);
        setContent(listview, 0);
    }

    public void setItems(CharSequence[] items,
                         AdapterView.OnItemClickListener onItemClickListener) {
        ListView listview = new ListView(content.getContext());
        listview.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        listview.setAdapter(new DialogAdapter(items));
        listview.setDivider(null);
        listview.setOnItemClickListener(onItemClickListener);
        setContent(listview, 0);
    }

    public void setItemsWithoutChk(CharSequence[] items,
                                   AdapterView.OnItemClickListener onItemClickListener) {
        ListView listview = new ListView(content.getContext());
        listview.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        DialogAdapter adapter = new DialogAdapter(items);
        adapter.setShowChk(false);
        listview.setDivider(null);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onItemClickListener);
        setContent(listview, 0);
    }

    public void setItems(CharSequence[] items, int index,
                         AdapterView.OnItemClickListener onItemClickListener) {
        ListView listview = new ListView(content.getContext());
        listview.setCacheColorHint(0);
        listview.setDivider(null);
        listview.setLayoutParams(new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        listview.setAdapter(new DialogAdapter(items, index));
        listview.setOnItemClickListener(onItemClickListener);
        setContent(listview, 0);
    }

    public void setMessage(int resId) {
        setMessage(getContext().getResources().getString(resId));
    }

    public void setMessage(Spanned spanned) {
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        TextView tvMessage = new TextView(getContext(), null,
                R.style.dialog_pinterest_text);
        tvMessage.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        tvMessage.setPadding(contentPadding, contentPadding, contentPadding,
                contentPadding);
        tvMessage.setLineSpacing(0.0F, 1.3F);
        tvMessage.setText(spanned);
        tvMessage.setTextColor(getContext().getResources().getColor(
                R.color.black));

        ScrollView.LayoutParams lp = new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT);
        scrollView.addView(tvMessage, lp);
        setContent(scrollView, 0);
    }

    public void setMessage(String message) {
        setMessage(Html.fromHtml(message));
    }

    public void setNegativeButton(int negative,
                                  DialogInterface.OnClickListener listener) {
        setNegativeButton(getContext().getString(negative), listener);
    }

    public void setNegativeButton(String text,
                                  final DialogInterface.OnClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            negativeBt.setText(text);
            negativeBt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onClick(CommonDialog.this, 0);
                    else
                        dismissClick.onClick(CommonDialog.this, 0);
                }
            });
            negativeBt.setVisibility(View.VISIBLE);
            if (positiveBt.getVisibility() == View.VISIBLE)
                buttonDivider.setVisibility(View.VISIBLE);
        } else {
            negativeBt.setVisibility(View.GONE);
            buttonDivider.setVisibility(View.GONE);
        }
        if (positiveBt.getVisibility() == View.VISIBLE
                || negativeBt.getVisibility() == View.VISIBLE)
            barDivider.setVisibility(View.VISIBLE);
        else
            barDivider.setVisibility(View.GONE);
    }

    public void setPositiveButton(int positive,
                                  DialogInterface.OnClickListener listener) {
        setPositiveButton(getContext().getString(positive), listener);
    }

    public void setPositiveButton(String positive,
                                  final DialogInterface.OnClickListener listener) {
        if (!TextUtils.isEmpty(positive)) {
            positiveBt.setText(positive);
            positiveBt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onClick(CommonDialog.this, 0);
                    else
                        dismissClick.onClick(CommonDialog.this, 0);
                }
            });
            positiveBt.setVisibility(View.VISIBLE);
            if (negativeBt.getVisibility() == View.VISIBLE)
                buttonDivider.setVisibility(View.VISIBLE);
        } else {
            positiveBt.setVisibility(View.GONE);
            buttonDivider.setVisibility(View.GONE);
        }
        if (positiveBt.getVisibility() == View.VISIBLE
                || negativeBt.getVisibility() == View.VISIBLE)
            barDivider.setVisibility(View.VISIBLE);
        else
            barDivider.setVisibility(View.GONE);
    }

    public void setSubTitle(int i) {
        setSubTitle((getContext().getResources().getString(i)));
    }

    public void setSubTitle(CharSequence subtitle) {
        if (subtitle != null && subtitle.length() > 0) {
            headerVw.subTitleTv.setText(subtitle);
            headerVw.subTitleTv.setVisibility(View.VISIBLE);
        } else {
            headerVw.subTitleTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTitle(int title) {
        setTitle((getContext().getResources().getString(title)));
    }

    @Override
    public void setTitle(CharSequence title) {
        if (title != null && title.length() > 0) {
            headerVw.titleTv.setText(title);
            headerVw.setVisibility(View.VISIBLE);
        } else {
            headerVw.setVisibility(View.GONE);
        }
    }
}
