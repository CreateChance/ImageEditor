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
import com.createchance.imageeditordemo.FilterListAdapter;
import com.createchance.imageeditordemo.R;
import com.createchance.imageeditordemo.model.Filter;
import com.createchance.imageeditordemo.utils.AssetsUtil;

import java.util.List;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/11/2
 */
public class EditFilterPanel extends AbstractPanel implements
        FilterListAdapter.OnFilterSelectListener,
        SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {

    private static final String TAG = "EditFilterPanel";

    private View mFilterPanel;

    private List<Filter> mFilterList;

    private Filter mCurFilter;
    private FilterOperator mCurFilterOp;
    private boolean mOpAdded;

    private SeekBar mFilterAdjustBar;

    private TextView mFilterName;
    private TextView mAdjustValue;

    private FilterListAdapter mFilterListAdapter;

    public EditFilterPanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_EFFECT);

        mFilterPanel = LayoutInflater.from(mContext).
                inflate(R.layout.edit_panel_filter, mParent, false);
        RecyclerView filterListView = mFilterPanel.findViewById(R.id.rcv_filter_list);
        filterListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        initFilterList();
        mFilterListAdapter = new FilterListAdapter(mContext, mFilterList, this);
        filterListView.setAdapter(mFilterListAdapter);
        mFilterAdjustBar = mFilterPanel.findViewById(R.id.sb_adjust_filter);
        mFilterAdjustBar.setOnSeekBarChangeListener(this);

        mFilterPanel.findViewById(R.id.iv_apply).setOnClickListener(this);
        mFilterPanel.findViewById(R.id.iv_cancel).setOnClickListener(this);
        mFilterName = mFilterPanel.findViewById(R.id.tv_filter_name);
        if (mCurFilter == null) {
            mCurFilter = mFilterList.get(0);
            mFilterAdjustBar.setEnabled(false);
        }
        mFilterName.setText(mCurFilter.mName);

        mAdjustValue = mFilterPanel.findViewById(R.id.tv_adjust_value);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        super.show(parent, surfaceWidth, surfaceHeight);
        mParent.addView(mFilterPanel);
    }

    @Override
    public void close(boolean discard) {
        super.close(discard);

        if (discard && mCurFilterOp != null) {
            mOpAdded = false;
            IEManager.getInstance().removeOperator(mCurFilterOp);
            mFilterListAdapter.resetSelect();
        }
    }

    @Override
    public void onFilterSelected(Filter filter) {
        if (mCurFilter == filter) {
            return;
        }

        // init gpu image filter.
        filter.get(mContext);
        if (filter.canAdjust()) {
            mFilterAdjustBar.setEnabled(true);
        } else {
            mFilterAdjustBar.setEnabled(false);
        }

        mCurFilter = filter;
        mFilterName.setText(mCurFilter.mName);
        if (mCurFilterOp == null) {
            mCurFilterOp = new FilterOperator.Builder()
                    .filter(mCurFilter.get(mContext))
                    .build();
        }

        if (mOpAdded) {
            mCurFilterOp.setFilter(mCurFilter.get(mContext));
            IEManager.getInstance().updateOperator(mCurFilterOp);
        } else {
            mOpAdded = true;
            IEManager.getInstance().addOperator(mCurFilterOp);
        }
    }

    private void initFilterList() {
        mFilterList = AssetsUtil.parseJsonToList(mContext, "filters/filters.json", Filter.class);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && mCurFilter != null) {
            mCurFilter.adjust(progress);
            mCurFilterOp.setFilter(mCurFilter.get(mContext));
            IEManager.getInstance().updateOperator(mCurFilterOp);
            mAdjustValue.setText(String.valueOf(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_apply:
                close(false);
                break;
            case R.id.iv_cancel:
                close(true);
                break;
            default:
                break;
        }
    }
}
