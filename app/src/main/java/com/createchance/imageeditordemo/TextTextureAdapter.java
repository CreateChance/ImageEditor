package com.createchance.imageeditordemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/10/31
 */
public class TextTextureAdapter extends RecyclerView.Adapter<TextTextureAdapter.ViewHolder> {

    private static final String TAG = "TextTextureAdapter";

    private Context mContext;

    private List<Texture> mTextureList;

    private OnTextureClickListener mListener;

    public TextTextureAdapter(Context context, List<Texture> textureList, OnTextureClickListener listener) {
        mContext = context;
        mTextureList = textureList;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_text_texture, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Texture texture = mTextureList.get(position);

        holder.texture.setImageResource(texture.resId);

        if (texture.selected) {
            holder.texture.setBackgroundResource(R.drawable.bg_white_square_corner);
        } else {
            holder.texture.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return mTextureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView texture;

        public ViewHolder(View itemView) {
            super(itemView);

            texture = itemView.findViewById(R.id.iv_text_texture);
            texture.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (index >= 0) {
                for (Texture texture : mTextureList) {
                    texture.selected = false;
                }
                mTextureList.get(index).selected = true;
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.onTextureSelected(index);
                }
            }
        }
    }

    public interface OnTextureClickListener {
        void onTextureSelected(int position);
    }

    public static class Texture {
        public int resId;
        public boolean selected;

        public Texture(int id) {
            resId = id;
        }
    }
}
