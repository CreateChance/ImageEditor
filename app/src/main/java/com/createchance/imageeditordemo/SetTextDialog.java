package com.createchance.imageeditordemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Dialog to set text.
 *
 * @author createchance
 * @date 2018-10-10
 */
public class SetTextDialog extends Dialog implements View.OnClickListener {

    private OnClickListener mListener;

    private EditText mEtText;
    private Button mBtnConfirm, mBtnCancel;

    private SetTextDialog(@NonNull Context context, OnClickListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_set_text);
        mEtText = findViewById(R.id.et_text);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnCancel = findViewById(R.id.btn_cancel);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mEtText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        mEtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mBtnConfirm.setEnabled(false);
                } else {
                    mBtnConfirm.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (mListener != null) {
                    mListener.onConfirm(mEtText.getText().toString());
                }
                dismiss();
                break;
            case R.id.btn_cancel:
                if (mListener != null) {
                    mListener.onCancel();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public static void start(Context context, OnClickListener listener) {
        SetTextDialog dialog = new SetTextDialog(context, listener);
        dialog.show();
    }

    public interface OnClickListener {
        void onConfirm(String text);

        void onCancel();
    }
}
