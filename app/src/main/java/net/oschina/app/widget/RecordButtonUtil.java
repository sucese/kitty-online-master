package net.oschina.app.widget;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.util.StringUtils;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.TextView;

/**
 * {@link #RecordButton}需要的工具类
 * 
 * @author kymjs(kymjs123@gmail.com)
 * 
 */
public class RecordButtonUtil {
    private final static String TAG = "AudioUtil";

    public static final String AUDOI_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/oschina/audio"; // 录音音频保存根路径

    private String mAudioPath; // 要播放的声音的路径
    private boolean mIsRecording;// 是否正在录音
    private boolean mIsPlaying;// 是否正在播放

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private OnPlayListener listener;

    public boolean isPlaying() {
        return mIsPlaying;
    }

    /**
     * 设置要播放的声音的路径
     * 
     * @param path
     */
    public void setAudioPath(String path) {
        this.mAudioPath = path;
    }

    /**
     * 播放声音结束时调用
     * 
     * @param l
     */
    public void setOnPlayListener(OnPlayListener l) {
        this.listener = l;
    }

    // 初始化 录音器
    private void initRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mAudioPath);
        mIsRecording = true;
    }

    /**
     * 开始录音，并保存到文件中
     */
    public void recordAudio() {
        initRecorder();
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            AppContext.showToast("小屁孩不听你说话了,请返回重试");
        }
    }

    /**
     * 获取音量值，只是针对录音音量
     * 
     * @return
     */
    public int getVolumn() {
        int volumn = 0;
        // 录音
        if (mRecorder != null && mIsRecording) {
            volumn = mRecorder.getMaxAmplitude();
            if (volumn != 0)
                volumn = (int) (10 * Math.log(volumn) / Math.log(10)) / 5;
        }
        return volumn;
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            mIsRecording = false;
        }
    }

    public void stopPlay() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mIsPlaying = false;
            if (listener != null) {
                listener.stopPlay();
            }
        }
    }

    public void startPlay(String audioPath, TextView timeView) {
        if (!mIsPlaying) {
            if (!StringUtils.isEmpty(audioPath)) {
                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(audioPath);
                    mPlayer.prepare();
                    if (timeView != null) {
                        int len = (mPlayer.getDuration() + 500) / 1000;
                        timeView.setText(len + "s");
                    }
                    mPlayer.start();
                    if (listener != null) {
                        listener.starPlay();
                    }
                    mIsPlaying = true;
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlay();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AppContext.showToastShort(R.string.record_sound_notfound);
            }
        } else {
            stopPlay();
        } // end playing
    }

    /**
     * 开始播放
     */
    public void startPlay() {
        startPlay(mAudioPath, null);
    }

    public interface OnPlayListener {
        /**
         * 播放声音结束时调用
         */
        void stopPlay();

        /**
         * 播放声音开始时调用
         */
        void starPlay();
    }
}
