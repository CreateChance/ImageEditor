package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.ops.FilterOperator;
import com.createchance.imageeditordemo.AdjustListAdapter;
import com.createchance.imageeditordemo.R;
import com.createchance.imageeditordemo.model.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/11/2
 */
public class EditAdjustPanel extends AbstractPanel implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        AdjustListAdapter.OnAdjustSelectListener {

    private static final String TAG = "EditAdjustPanel";

    private View mAdjustPanel;

    private int mCurType = -1;

    private GPUImageAdjuster mBrightAdj,
            mContrastAdj,
            mSaturationAdj,
            mSharpenAdj, mDarkCornerAdj;

    private TextView mAdjustName, mAdjustValue;

    private SeekBar mAdjustBar;

    public EditAdjustPanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_ADJUST);

        mAdjustPanel = LayoutInflater.from(mContext).inflate(R.layout.edit_panel_adjust, mParent, false);
        mAdjustName = mAdjustPanel.findViewById(R.id.tv_adjust_name);
        mAdjustValue = mAdjustPanel.findViewById(R.id.tv_adjust_value);
        mAdjustBar = mAdjustPanel.findViewById(R.id.sb_adjust_bar);
        mAdjustBar.setEnabled(false);
        mAdjustBar.setOnSeekBarChangeListener(this);
        RecyclerView adjustListView = mAdjustPanel.findViewById(R.id.rcv_adjust_list);
        adjustListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        AdjustListAdapter adjustListAdapter = new AdjustListAdapter(mContext, this);
        adjustListView.setAdapter(adjustListAdapter);
        mAdjustPanel.findViewById(R.id.iv_cancel).setOnClickListener(this);
        mAdjustPanel.findViewById(R.id.iv_apply).setOnClickListener(this);
    }

    @Override
    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        super.show(parent, surfaceWidth, surfaceHeight);

        mParent.addView(mAdjustPanel);
    }

    @Override
    public void close(boolean discard) {
        super.close(discard);

        if (discard) {
            List<AbstractOperator> operatorList = new ArrayList<>();
            if (mBrightAdj != null) {
                operatorList.add(mBrightAdj.operator);
            }
            if (mContrastAdj != null) {
                operatorList.add(mContrastAdj.operator);
            }
            if (mSaturationAdj != null) {
                operatorList.add(mSaturationAdj.operator);
            }
            if (mSharpenAdj != null) {
                operatorList.add(mSharpenAdj.operator);
            }
            if (mDarkCornerAdj != null) {
                operatorList.add(mDarkCornerAdj.operator);
            }
            IEManager.getInstance().removeOperator(operatorList);
            mBrightAdj = null;
            mContrastAdj = null;
            mSaturationAdj = null;
            mSharpenAdj = null;
            mDarkCornerAdj = null;
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        switch (mCurType) {
            case AdjustListAdapter.AdjustItem.TYPE_BRIGHTNESS:
                if (mBrightAdj == null) {
                    mBrightAdj = new GPUImageAdjuster();
                    mBrightAdj.filter = new Filter();
                    mBrightAdj.filter.mType = Filter.TYPE_GPU_IMAGE_BRIGHTNESS;
                    mBrightAdj.filter.mAdjust = new float[]{0.5f};
                    mBrightAdj.operator = new FilterOperator.Builder()
                            .filter(mBrightAdj.filter.get(mContext))
                            .build();
                    IEManager.getInstance().addOperator(mBrightAdj.operator);
                }

                mAdjustValue.setText(String.valueOf(progress));
                mBrightAdj.filter.adjust(progress);
                IEManager.getInstance().updateOperator(mBrightAdj.operator);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_CONTRAST:
                if (mContrastAdj == null) {
                    mContrastAdj = new GPUImageAdjuster();
                    mContrastAdj.filter = new Filter();
                    mContrastAdj.filter.mType = Filter.TYPE_GPU_IMAGE_CONTRAST;
                    mContrastAdj.filter.mAdjust = new float[]{2.0f};
                    mContrastAdj.operator = new FilterOperator.Builder()
                            .filter(mContrastAdj.filter.get(mContext))
                            .build();
                    IEManager.getInstance().addOperator(mContrastAdj.operator);
                }

                mAdjustValue.setText(String.valueOf(progress));
                mContrastAdj.filter.adjust(progress);
                IEManager.getInstance().updateOperator(mContrastAdj.operator);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SATURATION:
                if (mSaturationAdj == null) {
                    mSaturationAdj = new GPUImageAdjuster();
                    mSaturationAdj.filter = new Filter();
                    mSaturationAdj.filter.mType = Filter.TYPE_GPU_IMAGE_SATURATION;
                    mSaturationAdj.filter.mAdjust = new float[]{1.0f};
                    mSaturationAdj.operator = new FilterOperator.Builder()
                            .filter(mSaturationAdj.filter.get(mContext))
                            .build();
                    IEManager.getInstance().addOperator(mSaturationAdj.operator);
                }
                mAdjustValue.setText(String.valueOf(progress));
                mSaturationAdj.filter.adjust(progress);
                IEManager.getInstance().updateOperator(mSaturationAdj.operator);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHARPEN:
                if (mSharpenAdj == null) {
                    mSharpenAdj = new GPUImageAdjuster();
                    mSharpenAdj.filter = new Filter();
                    mSharpenAdj.filter.mType = Filter.TYPE_GPU_IMAGE_SHARPEN;
                    mSharpenAdj.filter.mAdjust = new float[]{2.0f};
                    mSharpenAdj.operator = new FilterOperator.Builder()
                            .filter(mSharpenAdj.filter.get(mContext))
                            .build();
                    IEManager.getInstance().addOperator(mSharpenAdj.operator);
                }
                mAdjustValue.setText(String.valueOf(progress));
                mSharpenAdj.filter.adjust(progress);
                IEManager.getInstance().updateOperator(mSharpenAdj.operator);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DARK_CORNER:
                if (mDarkCornerAdj == null) {
                    mDarkCornerAdj = new GPUImageAdjuster();
                    mDarkCornerAdj.filter = new Filter();
                    mDarkCornerAdj.filter.mType = Filter.TYPE_GPU_IMAGE_VIGNETTE;
                    mDarkCornerAdj.filter.mAdjust = new float[]{
                            0.5f,
                            0.5f,
                            0.0f,
                            0.0f,
                            0.0f,
                            0.3f,
                            0.75f};
                    mDarkCornerAdj.operator = new FilterOperator.Builder()
                            .filter(mDarkCornerAdj.filter.get(mContext))
                            .build();
                    IEManager.getInstance().addOperator(mDarkCornerAdj.operator);
                }
                mAdjustValue.setText(String.valueOf(progress));
                mDarkCornerAdj.filter.adjust(progress);
                IEManager.getInstance().updateOperator(mDarkCornerAdj.operator);
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

    @Override
    public void onAdjustSelected(AdjustListAdapter.AdjustItem adjustItem) {
        mCurType = adjustItem.mType;
        mAdjustName.setText(adjustItem.mNameStrId);
        mAdjustValue.setText("0");
        switch (mCurType) {
            case AdjustListAdapter.AdjustItem.TYPE_BRIGHTNESS:
                mAdjustBar.setEnabled(true);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_CONTRAST:
                mAdjustBar.setEnabled(true);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SATURATION:
                mAdjustBar.setEnabled(true);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHARPEN:
                mAdjustBar.setEnabled(true);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DARK_CORNER:
                mAdjustBar.setEnabled(true);
                break;
            default:
                mAdjustBar.setEnabled(false);
                break;
        }
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

    private class GPUImageAdjuster {
        public FilterOperator operator;
        public Filter filter;
    }
}
