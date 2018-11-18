package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.ops.MosaicOperator;
import com.createchance.imageeditordemo.R;

/**
 * Mosaic edit panel.
 *
 * @author gaochao02
 * @date 2018/11/18
 */
public class EditMosaicPanel extends AbstractPanel implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "EditMosaicPanel";

    private MosaicOperator mMosaicOp;

    private View mMosaicPanel;

    private View mVwBrush, mVwErase;

    private SeekBar mSbSize, mSbStrength;

    private boolean mIsBrushMode = true;

    public EditMosaicPanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_MOSAIC);

        mMosaicPanel = LayoutInflater.from(mContext).inflate(R.layout.edit_panel_mosaic, mParent, false);
        mVwBrush = mMosaicPanel.findViewById(R.id.vw_mosaic_brush);
        mVwErase = mMosaicPanel.findViewById(R.id.vw_mosaic_erase);
        mVwBrush.setOnClickListener(this);
        mVwErase.setOnClickListener(this);
        mSbSize = mMosaicPanel.findViewById(R.id.sb_mosaic_size);
        mSbStrength = mMosaicPanel.findViewById(R.id.sb_mosaic_strength);
        mSbSize.setOnSeekBarChangeListener(this);
        mSbStrength.setOnSeekBarChangeListener(this);
        mMosaicPanel.findViewById(R.id.iv_cancel).setOnClickListener(this);
        mMosaicPanel.findViewById(R.id.iv_apply).setOnClickListener(this);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        if (mMosaicOp == null) {
            mMosaicOp = new MosaicOperator.Builder().strength(20).build();
            IEManager.getInstance().addOperator(mMosaicOp);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mIsBrushMode) {
                    mMosaicOp.addArea(event.getX(), event.getY());
                } else {
                    mMosaicOp.removeArea(event.getX(), event.getY());
                }
                break;
            default:
                break;
        }
        IEManager.getInstance().updateOperator(mMosaicOp);
    }

    @Override
    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        super.show(parent, surfaceWidth, surfaceHeight);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mParent.addView(mMosaicPanel, params);
    }

    @Override
    public void close(boolean discard) {
        super.close(discard);

        if (discard && mMosaicOp != null) {
            IEManager.getInstance().removeOperator(mMosaicOp);
            mMosaicOp.clearArea();
            mMosaicOp = null;
            mSbSize.setProgress(0);
            mSbStrength.setProgress(mSbStrength.getMax() / 2);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_apply:
                close(false);
                break;
            case R.id.iv_cancel:
                close(true);
                break;
            case R.id.vw_mosaic_brush:
                mIsBrushMode = true;
                mVwBrush.setBackgroundResource(R.color.theme_red);
                mVwErase.setBackgroundResource(R.color.theme_dark);
                break;
            case R.id.vw_mosaic_erase:
                mIsBrushMode = false;
                mVwBrush.setBackgroundResource(R.color.theme_dark);
                mVwErase.setBackgroundResource(R.color.theme_red);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        if (mMosaicOp == null) {
            mMosaicOp = new MosaicOperator.Builder().strength(20).build();
            IEManager.getInstance().addOperator(mMosaicOp);
        }
        switch (seekBar.getId()) {
            case R.id.sb_mosaic_size:
                mMosaicOp.setSize(1 + progress * 9.0f / seekBar.getMax());
                break;
            case R.id.sb_mosaic_strength:
                mMosaicOp.setStrength(progress * 40.0f / seekBar.getMax());
                break;
            default:
                break;
        }
        IEManager.getInstance().updateOperator(mMosaicOp);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
