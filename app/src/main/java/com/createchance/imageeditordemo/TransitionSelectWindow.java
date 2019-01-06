package com.createchance.imageeditordemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.createchance.imageeditordemo.model.Transition;

import java.util.List;

/**
 * Transition selection window.
 *
 * @author createchance
 * @date 2019/1/6
 */
public class TransitionSelectWindow extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "TransitionSelectWindow";

    private Context mContext;

    private View mRootView;

    private TransitionSelectListener mListener;

    private RecyclerView mRcvTransList;
    private TransitionListAdapter mTransListAdapter;
    private List<Transition> mTransitionList;

    public TransitionSelectWindow(Context context, List<Transition> transitionItemList, TransitionSelectListener listener) {
        mContext = context;
        mTransitionList = transitionItemList;
        mListener = listener;

        mRootView = LayoutInflater.from(mContext).inflate(R.layout.video_generate_bottom_trans, null, false);

        mRootView.findViewById(R.id.tv_delete_transition).setOnClickListener(this);
        mRcvTransList = mRootView.findViewById(R.id.rcv_trans_list);
        mTransListAdapter = new TransitionListAdapter();
        mRcvTransList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRcvTransList.setAdapter(mTransListAdapter);

        setContentView(mRootView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(R.style.BottomPopupWindow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete_transition:
                if (mListener != null) {
                    mListener.onTransitionDeleted();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface TransitionSelectListener {
        void onTransitionSelected(Transition transition);

        void onTransitionDeleted();
    }

    private class TransitionListAdapter extends RecyclerView.Adapter<TransitionListAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(mContext).inflate(R.layout.item_transition, parent, false);
            return new ViewHolder(root);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Transition transition = mTransitionList.get(position);
            holder.transitionName.setText(transition.mName);
        }

        @Override
        public int getItemCount() {
            return mTransitionList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView transitionName;

            public ViewHolder(View itemView) {
                super(itemView);

                transitionName = itemView.findViewById(R.id.tv_trans_name);
                transitionName.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int index = getAdapterPosition();
                if (index >= 0) {
                    if (mListener != null) {
                        mListener.onTransitionSelected(mTransitionList.get(index));
                    }
                }
                dismiss();
            }
        }
    }
}
