package com.createchance.imageeditordemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.createchance.imageeditordemo.panels.AbstractPanel;
import com.createchance.imageeditordemo.panels.EditAdjustPanel;
import com.createchance.imageeditordemo.panels.EditFilterPanel;
import com.createchance.imageeditordemo.panels.EditMosaicPanel;
import com.createchance.imageeditordemo.panels.EditStickerPanel;
import com.createchance.imageeditordemo.panels.EditTextPanel;
import com.createchance.imageeditordemo.panels.EditTransformPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/10/30
 */
public class EditListAdapter extends RecyclerView.Adapter<EditListAdapter.ViewHolder> {

    private static final String TAG = "EditListAdapter";

    private ItemClickListener mListener;

    private Context mContext;

    private List<EditItem> mEditList;

    public EditListAdapter(Context context,
                           ItemClickListener itemClickListener,
                           AbstractPanel.PanelListener panelListener) {
        mContext = context;
        mListener = itemClickListener;

        mEditList = new ArrayList<>();

        // init edit items
        mEditList.add(new EditItem(R.drawable.icon_effects, R.string.edit_effect, AbstractPanel.TYPE_EFFECT,
                new EditFilterPanel(mContext, panelListener)));
        mEditList.add(new EditItem(R.drawable.icon_adjust, R.string.edit_adjust, AbstractPanel.TYPE_ADJUST,
                new EditAdjustPanel(mContext, panelListener)));
        mEditList.add(new EditItem(R.drawable.icon_cut, R.string.edit_cut, AbstractPanel.TYPE_CUT, null));
        mEditList.add(new EditItem(R.drawable.icon_rotate, R.string.edit_transform, AbstractPanel.TYPE_ROTATE,
                new EditTransformPanel(mContext, panelListener)));
        mEditList.add(new EditItem(R.drawable.icon_text, R.string.edit_text, AbstractPanel.TYPE_TEXT,
                new EditTextPanel(mContext, panelListener)));
        mEditList.add(new EditItem(R.drawable.icon_focus, R.string.edit_focus, AbstractPanel.TYPE_FOCUS, null));
        mEditList.add(new EditItem(R.drawable.icon_sticker, R.string.edit_sticker, AbstractPanel.TYPE_STICKER,
                new EditStickerPanel(mContext, panelListener)));
        mEditList.add(new EditItem(R.drawable.icon_mosaic, R.string.edit_mosaic, AbstractPanel.TYPE_MOSAIC,
                new EditMosaicPanel(mContext, panelListener)));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_edit, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EditItem editItem = mEditList.get(position);

        holder.icon.setImageResource(editItem.iconResId);
        holder.text.setText(editItem.textResId);
        if (editItem.selected) {
            holder.text.setBackgroundColor(mContext.getResources().getColor(R.color.theme_red));
        } else {
            holder.text.setBackgroundColor(mContext.getResources().getColor(R.color.theme_dark));
        }
    }

    @Override
    public int getItemCount() {
        return mEditList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView icon;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.iv_icon);
            text = itemView.findViewById(R.id.tv_text);

            text.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (index >= 0) {
                for (EditItem editItem : mEditList) {
                    editItem.selected = false;
                }
                mEditList.get(getAdapterPosition()).selected = true;
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.onItemClicked(mEditList.get(index));
                }
            } else {
                Log.e(TAG, "onClick error, refreshing try later!");
            }
        }
    }

    public static class EditItem {
        public int iconResId;
        public int textResId;
        public boolean selected;
        public int editType;
        public AbstractPanel editPanel;

        public EditItem(int icon, int text, int type, AbstractPanel panel) {
            iconResId = icon;
            textResId = text;
            editType = type;
            editPanel = panel;
        }
    }

    public interface ItemClickListener {
        void onItemClicked(EditItem item);
    }
}
