package com.createchance.imageeditordemo;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/10/31
 */
public class TextFontAdapter extends RecyclerView.Adapter<TextFontAdapter.ViewHolder> {

    private static final String TAG = "TextFontAdapter";

    private Context mContext;

    private List<FontItem> mFontList;

    private OnFontSelectListener mListener;

    public TextFontAdapter(Context context, List<FontItem> fontItemList, OnFontSelectListener listener) {
        mContext = context;
        mFontList = fontItemList;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_text_font, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FontItem fontItem = mFontList.get(position);

        Typeface typeface = Typeface.createFromFile(new File(mContext.getFilesDir(), fontItem.fontPath));
        holder.fontShow.setTypeface(typeface);
        if (fontItem.isChinese) {
            holder.fontShow.setText("汉");
        } else {
            holder.fontShow.setText("abc");
        }
        holder.fontName.setText(fontItem.fontName);

        if (fontItem.selected) {
            holder.fontShow.setTextColor(mContext.getResources().getColor(R.color.font_red));
            holder.fontShow.setBackgroundResource(R.drawable.bg_red_round_corner);
            holder.fontName.setTextColor(mContext.getResources().getColor(R.color.font_red));
        } else {
            holder.fontShow.setTextColor(mContext.getResources().getColor(R.color.font_white));
            holder.fontShow.setBackgroundResource(R.drawable.bg_white_corner);
            holder.fontName.setTextColor(mContext.getResources().getColor(R.color.font_white));
        }
    }

    @Override
    public int getItemCount() {
        return mFontList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fontShow;
        TextView fontName;

        public ViewHolder(View itemView) {
            super(itemView);

            fontShow = itemView.findViewById(R.id.tv_font_show);
            fontName = itemView.findViewById(R.id.tv_font_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (index >= 0) {
                for (FontItem fontItem : mFontList) {
                    fontItem.selected = false;
                }
                mFontList.get(index).selected = true;
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.fontSelected(index);
                }
            }
        }
    }

    public static class FontItem {
        @SerializedName("path")
        public String fontPath;
        @SerializedName("name")
        public String fontName;
        @SerializedName("chinese")
        public boolean isChinese;
        public boolean selected;

        public FontItem(String path, String name, boolean chinese) {
            fontPath = path;
            fontName = name;
            isChinese = chinese;
        }
    }

    public interface OnFontSelectListener {
        void fontSelected(int position);
    }
}
