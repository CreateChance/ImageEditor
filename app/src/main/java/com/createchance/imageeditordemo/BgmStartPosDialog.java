package com.createchance.imageeditordemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

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

    public static BgmStartPosDialog start(Context context, long startTime, long duration, BgmStartPosSelectedListener listener) {
        BgmStartPosDialog dialog = new BgmStartPosDialog(context, startTime, duration, listener);
        dialog.show();
        return dialog;
    }

    private BgmStartPosDialog(@NonNull Context context, long startTime, long duration, BgmStartPosSelectedListener listener) {
        super(context);

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

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface BgmStartPosSelectedListener {
        void onBgmStartSelected(long position);
    }
}
