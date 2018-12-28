package com.createchance.imageeditordemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * OutputDialog
 *
 * @author createchance
 * @date 2018/12/19
 */
public class OutputDialog extends Dialog {

    private static final String TAG = "OutputDialog";

    public static OutputDialog start(Context context) {
        OutputDialog dialog = new OutputDialog(context);
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
    }
}
