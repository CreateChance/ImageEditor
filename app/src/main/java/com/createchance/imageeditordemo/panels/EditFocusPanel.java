package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.ops.ThreeXThreeSampleOperator;
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

    private ThreeXThreeSampleOperator mFocusOp;

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

        if (discard && mFocusOp != null) {
            IEManager.getInstance().removeOperator(0, mFocusOp, true);
            mFocusOp = null;
        }
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
                if (mFocusOp == null) {
                    mFocusOp = new ThreeXThreeSampleOperator.Builder()
                            .sampleKernel(ThreeXThreeSampleOperator.SIGMA_1_5_GAUSSIAN_SAMPLE_KERNEL)
                            .build();
                    IEManager.getInstance().addOperator(0, mFocusOp, false);
                }
                mFocusOp.setRepeatTimes((int) (process * 100.0f / seekBar.getMax()));
                IEManager.getInstance().updateOperator(0, mFocusOp, true);
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
