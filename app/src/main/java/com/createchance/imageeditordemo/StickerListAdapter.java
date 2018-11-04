package com.createchance.imageeditordemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.createchance.imageeditordemo.model.Sticker;
import com.createchance.imageeditordemo.utils.DensityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Sticker list adapter.
 *
 * @author gaochao1-iri
 * @date 2018/11/4
 */
public class StickerListAdapter extends RecyclerView.Adapter<StickerListAdapter.ViewHolder> {

    private static final String TAG = "StickerListAdapter";

    private Context mContext;

    private List<StickerItem> mStickerList;

    private OnStickerSelectListener mListener;

    public StickerListAdapter(Context context,
                              List<Sticker> stickerList,
                              OnStickerSelectListener listener) {
        mContext = context;
        initStickerList(stickerList);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_sticker, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StickerItem stickerItem = mStickerList.get(position);
        Glide.with(mContext)
                .load(new File(mContext.getFilesDir(), stickerItem.sticker.mAsset))
                .into(holder.stickerIcon);
        if (stickerItem.selected) {
            holder.itemView.setBackgroundResource(R.color.theme_red);
        } else {
            holder.itemView.setBackgroundResource(R.color.theme_dark);
        }
    }

    @Override
    public int getItemCount() {
        return mStickerList.size();
    }

    private void initStickerList(List<Sticker> stickerList) {
        mStickerList = new ArrayList<>();
        for (Sticker sticker : stickerList) {
            mStickerList.add(new StickerItem(sticker));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView stickerIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            stickerIcon = itemView.findViewById(R.id.iv_sticker_icon);
            stickerIcon.setOnClickListener(this);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) stickerIcon.getLayoutParams();
            params.leftMargin = (Constants.mScreenWidth - DensityUtil.dip2px(mContext, 20 + 60 * 4)) / 8;
            params.rightMargin = (Constants.mScreenWidth - DensityUtil.dip2px(mContext, 20 + 60 * 4)) / 8;
            stickerIcon.setLayoutParams(params);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (index >= 0) {
                for (StickerItem stickerItem : mStickerList) {
                    stickerItem.selected = false;
                }
                mStickerList.get(index).selected = true;
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.onStickerSelected(mStickerList.get(index).sticker);
                }
            }
        }
    }

    private class StickerItem {
        public Sticker sticker;
        public boolean selected;

        public StickerItem(Sticker sticker) {
            this.sticker = sticker;
        }
    }

    public interface OnStickerSelectListener {
        void onStickerSelected(Sticker sticker);
    }
}
