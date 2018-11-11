package com.createchance.imageeditordemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/2
 */
public class AdjustListAdapter extends RecyclerView.Adapter<AdjustListAdapter.ViewHolder> {

    private static final String TAG = "AdjustListAdapter";

    private Context mContext;

    private List<AdjustItem> mAdjustItemList;

    private OnAdjustSelectListener mListener;

    public AdjustListAdapter(Context context, OnAdjustSelectListener listener) {
        mContext = context;
        initAdjustList();
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_adjust_value, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdjustItem adjustItem = mAdjustItemList.get(position);
        holder.icon.setImageResource(adjustItem.mIconResId);
        holder.name.setText(adjustItem.mNameStrId);
        if (adjustItem.mIsSelected) {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.theme_red));
        } else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.theme_dark));
        }
    }

    @Override
    public int getItemCount() {
        return mAdjustItemList.size();
    }

    private void initAdjustList() {
        mAdjustItemList = new ArrayList<>();
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_BRIGHTNESS,
                R.drawable.icon_adjust_brightness,
                R.string.edit_adjust_brightness));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_CONTRAST,
                R.drawable.icon_adjust_contrast,
                R.string.edit_adjust_contrast));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_SATURATION,
                R.drawable.icon_adjust_saturation,
                R.string.edit_adjust_saturation));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_SHARPEN,
                R.drawable.icon_adjust_sharpen,
                R.string.edit_adjust_sharpen));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_DARK_CORNER,
                R.drawable.icon_adjust_dark_corner,
                R.string.edit_adjust_dark_corner));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_SHADOW,
                R.drawable.icon_adjust_shadow,
                R.string.edit_adjust_shadow));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_HIGHLIGHT,
                R.drawable.icon_adjust_highlight,
                R.string.edit_adjust_highlight));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_COLOR_TEMP,
                R.drawable.icon_adjust_color_temp,
                R.string.edit_adjust_color_temp));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_TONE,
                R.drawable.icon_adjust_tone,
                R.string.edit_adjust_tone));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_DENOISE,
                R.drawable.icon_adjust_denoise,
                R.string.edit_adjust_denoise));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_CURVE,
                R.drawable.icon_adjust_curve,
                R.string.edit_adjust_curve));
        mAdjustItemList.add(new AdjustItem(AdjustItem.TYPE_COLOR_BALANCE,
                R.drawable.icon_adjust_color_balance,
                R.string.edit_adjust_color_balance));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView icon;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            icon = itemView.findViewById(R.id.iv_adjust_icon);
            name = itemView.findViewById(R.id.tv_adjust_name);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (index >= 0) {
                for (AdjustItem item : mAdjustItemList) {
                    item.mIsSelected = false;
                }
                mAdjustItemList.get(index).mIsSelected = true;
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.onAdjustSelected(mAdjustItemList.get(index));
                }
            }
        }
    }

    public static class AdjustItem {
        public static final int TYPE_BRIGHTNESS = 0;
        public static final int TYPE_CONTRAST = 1;
        public static final int TYPE_SATURATION = 2;
        public static final int TYPE_SHARPEN = 3;
        public static final int TYPE_DARK_CORNER = 4;
        public static final int TYPE_SHADOW = 5;
        public static final int TYPE_HIGHLIGHT = 6;
        public static final int TYPE_COLOR_TEMP = 7;
        public static final int TYPE_TONE = 8;
        public static final int TYPE_DENOISE = 9;
        public static final int TYPE_CURVE = 10;
        public static final int TYPE_COLOR_BALANCE = 11;

        public int mType;
        public int mIconResId;
        public int mNameStrId;
        public boolean mIsSelected;

        public AdjustItem(int type, int icon, int name) {
            mType = type;
            mIconResId = icon;
            mNameStrId = name;
        }
    }

    public interface OnAdjustSelectListener {
        void onAdjustSelected(AdjustItem adjustItem);
    }
}
