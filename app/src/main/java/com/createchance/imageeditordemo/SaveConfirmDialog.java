package com.createchance.imageeditordemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.createchance.imageeditor.IEManager;

import java.util.Locale;

/**
 * Save confirm dialog.
 *
 * @author createchance
 * @date 2019/1/23
 */
public class SaveConfirmDialog extends Dialog implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "SaveConfirmDialog";

    private int mSaveFormat = IEManager.IMG_FORMAT_JPEG;
    private int mSaveQuality = 100;

    private TextView mTvSaveQuality;
    private SeekBar mSbSaveQuality;

    private SaveInfoListener mListener;

    public static SaveConfirmDialog start(Context context, SaveInfoListener listener) {
        SaveConfirmDialog dialog = new SaveConfirmDialog(context, listener);
        dialog.show();
        return dialog;
    }

    private SaveConfirmDialog(@NonNull Context context, SaveInfoListener listener) {
        super(context);

        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_save_confirm);

        mTvSaveQuality = findViewById(R.id.tv_save_quality);
        mSbSaveQuality = findViewById(R.id.sb_save_quality);
        mSbSaveQuality.setOnSeekBarChangeListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        ((RadioGroup) findViewById(R.id.rg_save_format)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_save_as_jpeg:
                        mSaveFormat = IEManager.IMG_FORMAT_JPEG;
                        mSbSaveQuality.setEnabled(true);
                        break;
                    case R.id.rb_save_as_png:
                        mSaveFormat = IEManager.IMG_FORMAT_PNG;
                        mSbSaveQuality.setEnabled(false);
                        break;
                    case R.id.rb_save_as_webp:
                        mSaveFormat = IEManager.IMG_FORMAT_WEBP;
                        mSbSaveQuality.setEnabled(false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                if (mListener != null) {
                    mListener.onConfirm(mSaveFormat, mSaveQuality);
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

        switch (seekBar.getId()) {
            case R.id.sb_save_quality:
                mSaveQuality = progress;
                mTvSaveQuality.setText(String.format(Locale.getDefault(), "保存质量: %d", mSaveQuality));
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface SaveInfoListener {
        void onConfirm(int saveFormat, int quality);
    }
}
