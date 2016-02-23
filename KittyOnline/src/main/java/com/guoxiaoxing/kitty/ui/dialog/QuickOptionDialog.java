package com.guoxiaoxing.kitty.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.SimpleBackPage;
import com.guoxiaoxing.kitty.team.fragment.NoteEditFragment;
import com.guoxiaoxing.kitty.ui.fragment.TalkPubFragment;
import com.guoxiaoxing.kitty.util.UIHelper;


/**
 * 快速选择Dialog
 *
 * @author guoxiaoxing
 */
public class QuickOptionDialog extends Dialog implements
        View.OnClickListener {

    LinearLayout mLyQuickOptionText;
    LinearLayout mLyQuickOptionAlbum;
    LinearLayout mLyQuickOptionPhoto;
    LinearLayout mLyQuickOptionScan;
    LinearLayout mLyQuickOptionNote;
    LinearLayout mLyQuickOptionVoice;
    LinearLayout mLlOptionContainer;
    ImageView mIvClose;
    LinearLayout mLlFoot;
    LinearLayout mSetPop;

    public interface OnQuickOptionformClick {
        void onQuickOptionClick(int id);
    }

    private OnQuickOptionformClick mListener;

    private QuickOptionDialog(Context context, boolean flag,
                              OnCancelListener listener) {
        super(context, flag, listener);
    }

    @SuppressLint("InflateParams")
    private QuickOptionDialog(Context context, int defStyle) {

        super(context, defStyle);
        View contentView = getLayoutInflater().inflate(
                R.layout.dialog_quick_option, null);

        mLyQuickOptionText = (LinearLayout) contentView.findViewById(R.id.ly_quick_option_text);
        mLyQuickOptionAlbum = (LinearLayout) contentView.findViewById(R.id.ly_quick_option_album);
        mLyQuickOptionPhoto = (LinearLayout) contentView.findViewById(R.id.ly_quick_option_photo);
        mLyQuickOptionScan = (LinearLayout) contentView.findViewById(R.id.ly_quick_option_scan);
        mLyQuickOptionNote = (LinearLayout) contentView.findViewById(R.id.ly_quick_option_note);
        mLyQuickOptionVoice = (LinearLayout) contentView.findViewById(R.id.ly_quick_option_voice);
        mIvClose = (ImageView) contentView.findViewById(R.id.iv_close);


        mLyQuickOptionText.setOnClickListener(this);
        mLyQuickOptionAlbum.setOnClickListener(this);
        mLyQuickOptionPhoto.setOnClickListener(this);
        mLyQuickOptionScan.setOnClickListener(this);
        mLyQuickOptionNote.setOnClickListener(this);
        mLyQuickOptionVoice.setOnClickListener(this);
        mIvClose.setOnClickListener(this);

//        contentView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                QuickOptionDialog.this.dismiss();
//                return true;
//            }
//        });
        super.setContentView(contentView);

    }


    public QuickOptionDialog(Context context) {
        this(context, R.style.quick_option_dialog);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.CENTER);

        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = display.getWidth();
//        params.height = display.getHeight();
        getWindow().setAttributes(params);

        initView();
    }

    public void setOnQuickOptionformClickListener(OnQuickOptionformClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.ly_quick_option_text:
                onClickTalkPub(R.id.ly_quick_option_text);
                break;
            case R.id.ly_quick_option_album:
                onClickTalkPub(R.id.ly_quick_option_album);
                break;
            case R.id.ly_quick_option_photo:
                onClickTalkPub(R.id.ly_quick_option_photo);
                break;
            case R.id.ly_quick_option_voice:
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.RECORD);
                break;
            case R.id.ly_quick_option_scan:
                UIHelper.showScanActivity(getContext());
                break;
            case R.id.ly_quick_option_note:
                // UIHelper.showSimpleBack(getContext(), SimpleBackPage.FIND_USER);
                onClickNote();
                //UIHelper.showSimpleBack(getContext(), SimpleBackPage.FIND_USER);
                // onClickNote();
                break;
            default:
                break;
        }
        if (mListener != null) {
            mListener.onQuickOptionClick(id);
        }
        dismiss();
    }

    private void onClickTalkPub(int id) {
        Bundle bundle = new Bundle();
        int type = -1;
        switch (id) {
            case R.id.ly_quick_option_album:
                type = TalkPubFragment.TALK_TYPE_ALBUM;
                break;
            case R.id.ly_quick_option_photo:
                type = TalkPubFragment.TALK_TYPE_PHOTO;
                break;
            default:
                break;
        }
        bundle.putInt(TalkPubFragment.TALK_TYPE, type);
        UIHelper.showTalkActivity(getContext(), SimpleBackPage.TALK_PUB,
                bundle);
    }

    private void onClickNote() {
        Bundle bundle = new Bundle();
        bundle.putInt(NoteEditFragment.NOTE_FROMWHERE_KEY,
                NoteEditFragment.QUICK_DIALOG);
        UIHelper.showSimpleBack(getContext(), SimpleBackPage.NOTE_EDIT, bundle);
    }

    private void initView() {

        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(),
                R.anim.quick_option_close);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        mIvClose.startAnimation(operatingAnim);

        Animation iconAnimationslow = AnimationUtils.loadAnimation(getContext(), R.anim.quick_option_icon_in_slow);
        mLyQuickOptionText.startAnimation(iconAnimationslow);
        mLyQuickOptionVoice.startAnimation(iconAnimationslow);
        mLyQuickOptionPhoto.startAnimation(iconAnimationslow);
        mLyQuickOptionNote.startAnimation(iconAnimationslow);

        Animation iconAnimationFast = AnimationUtils.loadAnimation(getContext(), R.anim.quick_option_icon_in_fast);
        mLyQuickOptionAlbum.setAnimation(iconAnimationFast);
        mLyQuickOptionScan.setAnimation(iconAnimationFast);

    }
}