package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.ops.BaseImageOperator;
import com.createchance.imageeditordemo.Constants;
import com.createchance.imageeditordemo.R;

/**
 * Rotate edit panel.
 *
 * @author gaochao1-iri
 * @date 2018/11/5
 */
public class EditRotatePanel extends AbstractPanel implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "EditRotatePanel";

    private View mRotatePanel;

    private TextView mTvFlipX, mTvFlipY, mTvFlipZ, mTranslateX, mTranslateY, mTranslateZ;

    public EditRotatePanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_ROTATE);

        mRotatePanel = LayoutInflater.from(mContext).inflate(R.layout.edit_panel_rotate, mParent, false);
        mRotatePanel.findViewById(R.id.iv_cancel).setOnClickListener(this);
        mRotatePanel.findViewById(R.id.iv_apply).setOnClickListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_rotate_x)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_rotate_y)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_rotate_z)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_translate_x)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_translate_y)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_translate_z)).setOnSeekBarChangeListener(this);
        mTvFlipX = mRotatePanel.findViewById(R.id.tv_rotate_x);
        mTvFlipY = mRotatePanel.findViewById(R.id.tv_rotate_y);
        mTvFlipZ = mRotatePanel.findViewById(R.id.tv_rotate_z);
        mTranslateX = mRotatePanel.findViewById(R.id.tv_translate_x);
        mTranslateY = mRotatePanel.findViewById(R.id.tv_translate_y);
        mTranslateZ = mRotatePanel.findViewById(R.id.tv_translate_z);
        mTvFlipX.setText(String.format(mContext.getString(R.string.edit_rotate_x), 180));
        mTvFlipY.setText(String.format(mContext.getString(R.string.edit_rotate_y), 0));
        mTvFlipZ.setText(String.format(mContext.getString(R.string.edit_rotate_z), 0));
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        super.show(parent, surfaceWidth, surfaceHeight);

        mParent.addView(mRotatePanel);
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        BaseImageOperator baseImageOperator = (BaseImageOperator) Constants.mOpList.get(0);
        switch (seekBar.getId()) {
            case R.id.sb_rotate_x:
                mTvFlipX.setText(String.format(mContext.getString(R.string.edit_rotate_x), progress));
                baseImageOperator.setFlipX(progress);
                break;
            case R.id.sb_rotate_y:
                mTvFlipY.setText(String.format(mContext.getString(R.string.edit_rotate_y), progress));
                baseImageOperator.setFlipY(progress);
                break;
            case R.id.sb_rotate_z:
                mTvFlipZ.setText(String.format(mContext.getString(R.string.edit_rotate_z), progress));
                baseImageOperator.setFlipZ(progress);
                break;
            case R.id.sb_translate_x:
                mTranslateX.setText(String.format(mContext.getString(R.string.edit_translate_x), progress));
                baseImageOperator.setTranslateX(progress);
                break;
            case R.id.sb_translate_y:
                mTranslateY.setText(String.format(mContext.getString(R.string.edit_translate_y), progress));
                baseImageOperator.setTranslateY(progress);
                break;
            case R.id.sb_translate_z:
                float transZ = baseImageOperator.getNear() +
                        (progress * (baseImageOperator.getFar() - baseImageOperator.getNear()) / seekBar.getMax());
                mTranslateZ.setText(String.format(mContext.getString(R.string.edit_translate_z), transZ));
                baseImageOperator.setTranslateZ(transZ);
                break;
            default:
                break;
        }
        IEManager.getInstance().updateOperator(baseImageOperator);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
