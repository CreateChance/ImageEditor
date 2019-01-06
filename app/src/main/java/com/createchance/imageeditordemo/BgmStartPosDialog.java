package com.createchance.imageeditordemo;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

/**
 * Set bgm start position dialog.
 *
 * @author createchance
 * @date 2019/1/6
 */
public class BgmStartPosDialog extends Dialog implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "BgmStartPosDialog";

    private BgmStartPosSelectedListener mListener;

    private final long mDuration;
    private long mCurStartPos;

    // views
    private TextView mTvStartPos, mTvDuration;

    private String mBgmFile;

    // media player
    private MediaPlayer mBgmPlayer;

    public static BgmStartPosDialog start(Context context,
                                          String bgmFile,
                                          long startTime,
                                          long duration,
                                          BgmStartPosSelectedListener listener) {
        BgmStartPosDialog dialog = new BgmStartPosDialog(context, bgmFile, startTime, duration, listener);
        dialog.show();
        return dialog;
    }

    private BgmStartPosDialog(@NonNull Context context,
                              String bgmFile,
                              long startTime,
                              long duration,
                              BgmStartPosSelectedListener listener) {
        super(context);

        mBgmFile = bgmFile;
        mCurStartPos = startTime;
        mDuration = duration;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_bgm_start_position_select);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        mTvStartPos = findViewById(R.id.tv_bgm_start_time);
        mTvDuration = findViewById(R.id.tv_bgm_duration);
        mTvStartPos.setText(String.format(Locale.getDefault(), "%02d:%02d", mCurStartPos / (60 * 1000), (mCurStartPos / 1000) % 60));
        mTvDuration.setText(String.format(Locale.getDefault(), "%02d:%02d", mDuration / (60 * 1000), (mDuration / 1000) % 60));
        ((SeekBar) findViewById(R.id.sb_bgm_start_pos)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.sb_bgm_start_pos)).setProgress((int) (mCurStartPos * 1.0f * 5000 / mDuration));

        // init mBgmPlayer.
        try {
            mBgmPlayer = new MediaPlayer();
            mBgmPlayer.setDataSource(mBgmFile);
            mBgmPlayer.prepare();
            mBgmPlayer.setLooping(false);
            mBgmPlayer.seekTo((int) mCurStartPos);
            mBgmPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // play it again
                    mBgmPlayer.seekTo((int) mCurStartPos);
                    mBgmPlayer.start();
                }
            });
            mBgmPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Music init failed!", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        // stop playing first.
        if (mBgmPlayer.isPlaying()) {
            mBgmPlayer.stop();
        }
        mBgmPlayer.release();

        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                if (mListener != null) {
                    mListener.onBgmStartSelected(mCurStartPos);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        mCurStartPos = (long) (progress * 1.0f * mDuration / seekBar.getMax());
        mTvStartPos.setText(String.format(Locale.getDefault(), "%02d:%02d", mCurStartPos / (60 * 1000), (mCurStartPos / 1000) % 60));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mBgmPlayer.isPlaying()) {
            mBgmPlayer.pause();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mBgmPlayer.seekTo((int) mCurStartPos);
        mBgmPlayer.start();
    }

    public interface BgmStartPosSelectedListener {
        void onBgmStartSelected(long position);
    }
}
