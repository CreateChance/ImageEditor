package com.createchance.imageeditordemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.createchance.imageeditordemo.model.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/11/2
 */
public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.ViewHolder> {

    private Context mContext;

    private List<FilterItem> mFilterList;

    private OnFilterSelectListener mListener;

    public FilterListAdapter(Context context,
                             List<Filter> filterList,
                             OnFilterSelectListener listener) {
        mContext = context;
        initFilterItemList(filterList);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_filter, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilterItem filterItem = mFilterList.get(position);

        holder.filterCode.setText(filterItem.filter.mCode);
        if (filterItem.selected) {
            holder.filterCode.setBackgroundResource(R.drawable.bg_red_square_corner);
        } else {
            holder.filterCode.setBackgroundResource(R.drawable.bg_white_square_corner);
        }
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    private void initFilterItemList(List<Filter> filterList) {
        mFilterList = new ArrayList<>();
        for (Filter filter : filterList) {
            mFilterList.add(new FilterItem(filter));
        }
        mFilterList.get(0).selected = true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView filterCode;

        public ViewHolder(View itemView) {
            super(itemView);

            filterCode = itemView.findViewById(R.id.tv_filter_code);
            filterCode.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (index >= 0) {
                for (FilterItem item : mFilterList) {
                    item.selected = false;
                }
                mFilterList.get(index).selected = true;
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.onFilterSelected(mFilterList.get(index).filter);
                }
            }
        }
    }

    private class FilterItem {
        public Filter filter;
        public boolean selected;

        public FilterItem(Filter f) {
            filter = f;
        }
    }

    public interface OnFilterSelectListener {
        void onFilterSelected(Filter filter);
    }
}
