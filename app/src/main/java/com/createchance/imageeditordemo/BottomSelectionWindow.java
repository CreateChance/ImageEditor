package com.createchance.imageeditordemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Bottom selection window.
 *
 * @author createchance
 * @date 2019/1/5
 */
public class BottomSelectionWindow extends PopupWindow implements View.OnClickListener {

    private static final String TAG = "BottomSelectionWindow";

    private Context mContext;

    private View mRootView;

    private BottomSelectionListener mListener;

    public BottomSelectionWindow(Context context, BottomSelectionListener listener) {
        super(context);
        mContext = context;
        mListener = listener;

        mRootView = LayoutInflater.from(mContext).inflate(R.layout.main_bottom_selection, null, false);
        mRootView.findViewById(R.id.tv_bottom_preview).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_bottom_edit).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_bottom_share).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_bottom_delete).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_bottom_cancel).setOnClickListener(this);

        setContentView(mRootView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(R.style.BottomPopupWindow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bottom_preview:
                if (mListener != null) {
                    mListener.onPreview();
                }
                break;
            case R.id.tv_bottom_edit:
                if (mListener != null) {
                    mListener.onEdit();
                }
                break;
            case R.id.tv_bottom_share:
                if (mListener != null) {
                    mListener.onShare();
                }
                break;
            case R.id.tv_bottom_delete:
                if (mListener != null) {
                    mListener.onDelete();
                }
                break;
            case R.id.tv_bottom_cancel:
                if (mListener != null) {
                    mListener.onCancel();
                }
                break;
            default:
                break;
        }
    }

    public interface BottomSelectionListener {
        void onPreview();

        void onEdit();

        void onShare();

        void onDelete();

        void onCancel();
    }
}
