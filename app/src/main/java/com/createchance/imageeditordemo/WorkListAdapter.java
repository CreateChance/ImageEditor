package com.createchance.imageeditordemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/3
 */
public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.ViewHolder> {

    private static final String TAG = "WorkListAdapter";

    private Context mContext;

    private List<WorkItem> mWorkList;

    private OnWorkSelectListener mListener;

    public WorkListAdapter(Context context, List<WorkItem> workItemList, OnWorkSelectListener listener) {
        mContext = context;
        mWorkList = workItemList;
        mListener = listener;

        Collections.sort(mWorkList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_work, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkItem workItem = mWorkList.get(position);
        Glide.with(mContext).load(workItem.mImage).into(holder.thumbnail);
        holder.timestamp.setText(getFormatTime(workItem.mTimeStamp));
        holder.resolution.setText(
                String.format(mContext.getString(R.string.main_page_work_resolution_format), workItem.mWidth, workItem.mHeight));
        if (workItem.mSize < 1024) {
            holder.size.setText(
                    String.format(mContext.getString(R.string.main_page_work_size_format_byte), workItem.mSize)
            );
        } else if (workItem.mSize < 1024 * 1024) {
            holder.size.setText(
                    String.format(mContext.getString(R.string.main_page_work_size_format_k_byte), workItem.mSize * 1.0f / 1024)
            );
        } else {
            holder.size.setText(
                    String.format(mContext.getString(R.string.main_page_work_size_format_m_byte),
                            workItem.mSize * 1.0f / (1024 * 1024))
            );
        }
    }

    @Override
    public int getItemCount() {
        return mWorkList.size();
    }

    public void refresh(List<WorkItem> workItemList) {
        mWorkList.clear();
        Collections.sort(workItemList);
        mWorkList.addAll(workItemList);
        notifyDataSetChanged();
    }

    private String getFormatTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date(time));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnail;
        TextView timestamp;
        TextView resolution;
        TextView size;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.iv_work_thumbnail);
            timestamp = itemView.findViewById(R.id.tv_work_timestamp_value);
            resolution = itemView.findViewById(R.id.tv_work_resolution_value);
            size = itemView.findViewById(R.id.tv_work_size_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (index >= 0) {
                if (mListener != null) {
                    mListener.onWorkSelected(mWorkList.get(index));
                }
            }
        }
    }

    public static class WorkItem implements Comparable<WorkItem> {
        public File mImage;
        public long mTimeStamp;
        public int mWidth, mHeight;
        public long mSize;


        @Override
        public int compareTo(@NonNull WorkItem o) {
            if (o.mTimeStamp > mTimeStamp) {
                return 1;
            } else if (o.mTimeStamp < mTimeStamp) {
                return -1;
            }

            return 0;
        }
    }

    public interface OnWorkSelectListener {
        void onWorkSelected(WorkItem workItem);
    }
}
