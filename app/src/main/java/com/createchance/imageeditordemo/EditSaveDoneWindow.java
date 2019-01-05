package com.createchance.imageeditordemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Edit save done window.
 *
 * @author createchance
 * @date 2019/1/5
 */
public class EditSaveDoneWindow extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "EditSaveDoneWindow";

    private Context mContext;

    private View mRootView;

    private EditSaveDoneListener mListener;

    public EditSaveDoneWindow(Context context, EditSaveDoneListener listener) {
        mContext = context;
        mListener = listener;

        mRootView = LayoutInflater.from(mContext).inflate(R.layout.edit_bottom_save_done, null, false);
        // set click listener.
        mRootView.findViewById(R.id.tv_exit).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_continue).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_share).setOnClickListener(this);

        setContentView(mRootView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(false);
        setAnimationStyle(R.style.BottomPopupWindow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share:
                if (mListener != null) {
                    mListener.onShare();
                }
                break;
            case R.id.tv_exit:
                if (mListener != null) {
                    mListener.onExit();
                }
                dismiss();
                break;
            case R.id.tv_continue:
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface EditSaveDoneListener {
        void onShare();

        void onExit();
    }
}
