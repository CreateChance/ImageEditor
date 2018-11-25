package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.createchance.imageeditordemo.R;

/**
 * Scissor edit panel.
 *
 * @author createchance
 * @date 2018/11/24
 */
public class EditScissorPanel extends AbstractPanel implements View.OnClickListener {

    private static final String TAG = "EditScissorPanel";

    private View mScissorPanel;

    public EditScissorPanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_CUT);

        mScissorPanel = LayoutInflater.from(mContext).inflate(R.layout.edit_panel_scissor, mParent, false);
        mScissorPanel.findViewById(R.id.iv_cancel).setOnClickListener(this);
        mScissorPanel.findViewById(R.id.iv_apply).setOnClickListener(this);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        super.show(parent, surfaceWidth, surfaceHeight);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mParent.addView(mScissorPanel, params);
    }

    @Override
    public void close(boolean discard) {
        super.close(discard);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                close(true);
                break;
            case R.id.iv_apply:
                close(false);
                break;
            default:
                break;
        }
    }
}
