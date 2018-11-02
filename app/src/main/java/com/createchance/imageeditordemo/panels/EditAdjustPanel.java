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
import com.createchance.imageeditor.ops.FilterOperator;
import com.createchance.imageeditordemo.AdjustListAdapter;
import com.createchance.imageeditordemo.R;
import com.createchance.imageeditordemo.model.Filter;

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

    private GPUImageAdjuster mBrightAdj;

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
            default:
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
