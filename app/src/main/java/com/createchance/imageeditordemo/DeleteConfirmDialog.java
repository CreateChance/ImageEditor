package com.createchance.imageeditordemo;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Delete confirm dialog.
 *
 * @author createchance
 * @date 2019/1/5
 */
public class DeleteConfirmDialog extends Dialog implements View.OnClickListener {

    private DeleteConformListener mListener;

    public static DeleteConfirmDialog start(Context context, DeleteConformListener listener) {
        DeleteConfirmDialog dialog = new DeleteConfirmDialog(context, listener);
        dialog.show();
        return dialog;
    }

    private DeleteConfirmDialog(@NonNull Context context, DeleteConformListener listener) {
        super(context);
        mListener = listener;

        setContentView(R.layout.dialog_delete_confirm);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (mListener != null) {
                    mListener.onDeleteFile();
                }
                break;
            default:
                break;
        }
        dismiss();
    }

    public interface DeleteConformListener {
        void onDeleteFile();
    }
}
