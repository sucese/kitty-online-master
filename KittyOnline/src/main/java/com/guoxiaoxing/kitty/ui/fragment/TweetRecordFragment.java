package com.guoxiaoxing.kitty.ui.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.bean.UserTalk;
import com.guoxiaoxing.kitty.service.ServerTaskUtils;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.TDevice;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.widget.RecordButton;
import com.guoxiaoxing.kitty.widget.RecordButton.OnFinishedRecordListener;
import com.guoxiaoxing.kitty.widget.RecordButtonUtil.OnPlayListener;

import org.kymjs.kjframe.utils.DensityUtils;

import java.io.File;

import butterknife.Bind;

/**
 * 语音动弹发布界面
 *
 * @author kymjs(kymjs123@gmail.com)
 */
public class TweetRecordFragment extends BaseFragment {

    @Bind(R.id.tweet_layout_record)
    RelativeLayout mLayout;
    @Bind(R.id.tweet_btn_record)
    RecordButton mBtnRecort;
    @Bind(R.id.tweet_time_record)
    TextView mTvTime;
    @Bind(R.id.tweet_text_record)
    TextView mTvInputLen;
    @Bind(R.id.tweet_edit_record)
    EditText mEtSpeech;
    @Bind(R.id.tweet_img_volume)
    ImageView mImgVolume;

    public static int MAX_LEN = 160;

    private AnimationDrawable drawable; // 录音播放时的动画背景

    private String strSpeech = "#语音动弹#";
    private int currentRecordTime = 0;

    @Override
    public void onClick(View v) {
        if (v == mLayout) {
            mBtnRecort.playRecord();
        }
    }

    @Override
    public void initView(View view) {
        RelativeLayout.LayoutParams params = (LayoutParams) mBtnRecort
                .getLayoutParams();
        params.width = DensityUtils.getScreenW(getActivity());
        params.height = (int) (DensityUtils.getScreenH(getActivity()) * 0.4);
        mBtnRecort.setLayoutParams(params);
        mLayout.setOnClickListener(this);

        mBtnRecort.setOnFinishedRecordListener(new OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int recordTime) {
                currentRecordTime = recordTime;
                mLayout.setVisibility(View.VISIBLE);
                if (recordTime < 10) {
                    mTvTime.setText("0" + recordTime + "\"");
                } else {
                    mTvTime.setText(recordTime + "\"");
                }
            }

            @Override
            public void onCancleRecord() {
                mLayout.setVisibility(View.GONE);
            }
        });

        drawable = (AnimationDrawable) mImgVolume.getBackground();
        mBtnRecort.getAudioUtil().setOnPlayListener(new OnPlayListener() {
            @Override
            public void stopPlay() {
                drawable.stop();
                mImgVolume.setBackgroundDrawable(drawable.getFrame(0));
            }

            @Override
            public void starPlay() {
                mImgVolume.setBackgroundDrawable(drawable);
                drawable.start();
            }
        });

        mEtSpeech.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > MAX_LEN) {
                    mTvInputLen.setText("已达到最大长度");
                } else {
                    mTvInputLen.setText("您还可以输入" + (MAX_LEN - s.length())
                            + "个字符");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_LEN) {
                    mEtSpeech.setText(s.subSequence(0, MAX_LEN));
                    CharSequence text = mEtSpeech.getText();
                    if (text instanceof Spannable)
                        Selection.setSelection((Spannable) text, MAX_LEN);
                }
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_tweet_pub_record;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pub_tweet_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_send:
                handleSubmit(mBtnRecort.getCurrentAudioPath());
                break;
        }
        return true;
    }

    /**
     * 发布动弹
     */
    private void handleSubmit(String audioPath) {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        if (StringUtils.isEmpty(audioPath)) {
            AppContext.showToastShort(R.string.record_sound_notfound);
            return;
        }
        File file = new File(audioPath);
        if (!file.exists()) {
            AppContext.showToastShort(R.string.record_sound_notfound);
            return;
        }

        String body = mEtSpeech.getText().toString();
        if (!StringUtils.isEmpty(body)) {
            strSpeech = body;
        }
        UserTalk userTalk = new UserTalk();
        userTalk.setAuthorid(AppContext.getInstance().getLoginUid());
        userTalk.setAudioPath(audioPath);
        userTalk.setBody(strSpeech);
        ServerTaskUtils.pubTweet(getActivity(), userTalk);
        getActivity().finish();
    }
}
