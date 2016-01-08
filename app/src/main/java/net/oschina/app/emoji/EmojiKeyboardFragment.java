package net.oschina.app.emoji;

import net.oschina.app.R;
import net.oschina.app.emoji.SoftKeyboardStateHelper.SoftKeyboardStateListener;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class EmojiKeyboardFragment extends Fragment implements
        SoftKeyboardStateListener {

    private LinearLayout mEmojiContent;
    private RadioGroup mEmojiBottom;
    private View[] mEmojiTabs;
    private ViewPager mEmojiPager;

    private EmojiPagerAdapter adapter;

    private LinearLayout mRootView;
    private OnEmojiClickListener listener;
    public static int EMOJI_TAB_CONTENT;

    private SoftKeyboardStateHelper mKeyboardHelper;

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = (LinearLayout) inflater.inflate(R.layout.frag_keyboard,
                container, false);
        initWidget(mRootView);
        return mRootView;
    }

    private void initWidget(View rootView) {
        // bottom
        mEmojiBottom = (RadioGroup) rootView.findViewById(R.id.emoji_bottom);
        mEmojiBottom.setVisibility(View.VISIBLE);
        EMOJI_TAB_CONTENT = mEmojiBottom.getChildCount() - 1; // 减一是因为有一个删除按钮
        mEmojiTabs = new View[EMOJI_TAB_CONTENT];
        if (EMOJI_TAB_CONTENT <= 1) { // 只有一个分类的时候就不显示了
            mEmojiBottom.setVisibility(View.GONE);
        }
        for (int i = 0; i < EMOJI_TAB_CONTENT; i++) {
            mEmojiTabs[i] = mEmojiBottom.getChildAt(i);
            mEmojiTabs[i].setOnClickListener(getBottomBarClickListener(i));
        }
        mEmojiBottom.findViewById(R.id.emoji_bottom_del).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onDeleteButtonClick(v);
                        }
                    }
                });

        // content必须放在bottom下面初始化
        mEmojiContent = (LinearLayout) rootView
                .findViewById(R.id.emoji_content);
        mEmojiPager = (ViewPager) mEmojiContent.findViewById(R.id.emoji_pager);
        adapter = new EmojiPagerAdapter(getFragmentManager(),
                EMOJI_TAB_CONTENT, listener);
        mEmojiPager.setAdapter(adapter);
        mEmojiContent.setVisibility(View.VISIBLE);

        mKeyboardHelper = new SoftKeyboardStateHelper(getActivity().getWindow()
                .getDecorView());
        mKeyboardHelper.addSoftKeyboardStateListener(this);
    }

    /**
     * 底部栏点击事件监听器
     * 
     * @param indexfff
     * @return
     */
    private OnClickListener getBottomBarClickListener(final int index) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmojiPager.setCurrentItem(index);
            }
        };
    }

    public void setOnEmojiClickListener(OnEmojiClickListener l) {
        this.listener = l;
    }

    public void hideAllKeyBoard() {
        hideEmojiKeyBoard();
        hideSoftKeyboard();
    }

    public boolean isShow() {
        return mEmojiContent.getVisibility() == View.VISIBLE;
    }

    /**
     * 隐藏Emoji并显示软键盘
     */
    public void hideEmojiKeyBoard() {
        mEmojiBottom.setVisibility(View.GONE);
        mEmojiContent.setVisibility(View.GONE);
    }

    /**
     * 显示Emoji并隐藏软键盘
     */
    public void showEmojiKeyBoard() {
        mEmojiContent.setVisibility(View.VISIBLE);
        if (EMOJI_TAB_CONTENT > 1) {
            mEmojiBottom.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mEmojiBottom.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyboard(EditText et) {
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(et,
                InputMethodManager.SHOW_FORCED);
    }

    /**
     * 当软键盘显示时回调
     */
    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        mEmojiBottom.setVisibility(View.GONE);
        mEmojiContent.setVisibility(View.GONE);
    }

    @Override
    public void onSoftKeyboardClosed() {}

    @Override
    public void onStop() {
        super.onStop();
        hideSoftKeyboard();
    }
}
