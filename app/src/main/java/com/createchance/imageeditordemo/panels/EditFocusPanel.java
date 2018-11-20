package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.createchance.imageeditordemo.R;

/**
 * Image focus edit panel.
 *
 * @author createchance
 * @date 2018/11/20
 */
public class EditFocusPanel extends AbstractPanel implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "EditFocusPanel";

    private View mVwFocusPanel;
    private SeekBar mSbFocusValue;

    public EditFocusPanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_FOCUS);

        mVwFocusPanel = LayoutInflater.from(mContext).inflate(R.layout.edit_panel_focus, mParent, false);
        mSbFocusValue = mVwFocusPanel.findViewById(R.id.sb_focus);
        mSbFocusValue.setOnSeekBarChangeListener(this);
        mVwFocusPanel.findViewById(R.id.iv_cancel).setOnClickListener(this);
        mVwFocusPanel.findViewById(R.id.iv_apply).setOnClickListener(this);
    }

    @Override
    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        super.show(parent, surfaceWidth, surfaceHeight);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mParent.addView(mVwFocusPanel, params);
    }

    @Override
    public void close(boolean discard) {
        super.close(discard);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int process, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        switch (seekBar.getId()) {
            case R.id.sb_focus:

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
}
