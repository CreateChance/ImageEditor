package com.createchance.imageeditordemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.Locale;

/**
 * OutputDialog
 *
 * @author createchance
 * @date 2018/12/19
 */
public class OutputDialog extends Dialog {

    private static final String TAG = "OutputDialog";

    private TextView mTvProgress;

    public static OutputDialog start(Context context) {
        OutputDialog dialog = new OutputDialog(context);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    private OutputDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_output);
        mTvProgress = findViewById(R.id.tv_progress);
    }

    public void setProgress(float progress) {
        mTvProgress.setText(String.format(Locale.getDefault(), "%.1f%%", progress * 100));
    }
}
