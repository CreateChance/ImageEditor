package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.ops.StickerOperator;
import com.createchance.imageeditordemo.R;
import com.createchance.imageeditordemo.StickerListAdapter;
import com.createchance.imageeditordemo.model.Sticker;
import com.createchance.imageeditordemo.utils.AssetsUtil;

import java.io.File;
import java.util.List;

/**
 * Sticker edit panel
 *
 * @author createchance
 * @date 2018/11/4
 */
public class EditStickerPanel extends AbstractPanel implements
        StickerListAdapter.OnStickerSelectListener,
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "EditStickerPanel";

    private View mStickerPanel;
    private ViewGroup mSubPanelContainer;
    private View mStickerAssetPanel, mStickerAdjustPanel;

    private RecyclerView mStickerListView;
    private StickerListAdapter mStickerListAdapter;
    private List<Sticker> mStickerList;

    private StickerOperator mCurOp;
    private Sticker mCurSticker;

    private int mLastX, mLastY;

    public EditStickerPanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_STICKER);

        initStickerList();

        initMainPanel();
        initAssetPanel();
        initAdjustPanel();

        mSubPanelContainer.removeAllViews();
        mSubPanelContainer.addView(mStickerAssetPanel);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        if (mCurOp == null) {
            return;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int curX = (int) (mCurOp.getPosX() + (event.getX() - mLastX));
                int curY = (int) (mCurOp.getPosY() - (event.getY() - mLastY));
                if (curX < IEManager.getInstance().getClip(0).getScissorX()) {
                    curX = IEManager.getInstance().getClip(0).getScissorX();
                } else if (curX > IEManager.getInstance().getClip(0).getScissorX() +
                        IEManager.getInstance().getClip(0).getScissorWidth() - mCurOp.getWidth() * mCurOp.getScaleFactor()) {
                    curX = (int) (IEManager.getInstance().getClip(0).getScissorX() +
                            IEManager.getInstance().getClip(0).getScissorWidth() - mCurOp.getWidth() * mCurOp.getScaleFactor());
                }

                if (curY < IEManager.getInstance().getClip(0).getScissorY()) {
                    curY = IEManager.getInstance().getClip(0).getScissorY();
                } else if (curY > IEManager.getInstance().getClip(0).getScissorY() +
                        IEManager.getInstance().getClip(0).getScissorHeight() - mCurOp.getHeight() * mCurOp.getScaleFactor()) {
                    curY = (int) (IEManager.getInstance().getClip(0).getScissorY() +
                            IEManager.getInstance().getClip(0).getScissorHeight() - mCurOp.getHeight() * mCurOp.getScaleFactor());
                }

                mCurOp.setPosX(curX);
                mCurOp.setPosY(curY);
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                IEManager.getInstance().renderClip(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        super.show(parent, surfaceWidth, surfaceHeight);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mParent.addView(mStickerPanel, params);
    }

    @Override
    public void close(boolean discard) {
        super.close(discard);

        if (discard && mCurOp != null) {
            IEManager.getInstance().removeOperator(0, mCurOp);
            mCurOp = null;
            mCurSticker = null;
        }
    }

    private void initStickerList() {
        mStickerList = AssetsUtil.parseJsonToList(mContext, "stickers/stickers.json", Sticker.class);
    }

    @Override
    public void onStickerSelected(Sticker sticker) {
        if (mCurSticker == sticker) {
            Log.w(TAG, "onStickerSelected, sticker not changed, so skip.");
            return;
        }
        mCurSticker = sticker;
        if (mCurOp == null) {
            Bitmap stickerImg = BitmapFactory.decodeFile(new File(mContext.getFilesDir(), sticker.mAsset).getAbsolutePath());
            mCurOp = new StickerOperator.Builder()
                    .sticker(stickerImg)
                    .scaleFactor(0.5f)
                    .position((IEManager.getInstance().getClip(0).getSurfaceWidth() - (int) (stickerImg.getWidth() * 0.5f)) / 2,
                            (IEManager.getInstance().getClip(0).getSurfaceHeight() - (int) (stickerImg.getHeight() * 0.5f)) / 2)

                    .build();
            IEManager.getInstance().addOperator(0, mCurOp);
        } else {
            mCurOp.setSticker(BitmapFactory.decodeFile(new File(mContext.getFilesDir(), sticker.mAsset).getAbsolutePath()));
            IEManager.getInstance().renderClip(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sticker_asset:
                mStickerPanel.findViewById(R.id.tv_sticker_asset).setBackgroundResource(R.color.theme_dark);
                mStickerPanel.findViewById(R.id.tv_sticker_adjust).setBackgroundResource(R.color.theme_dark);
                v.setBackgroundResource(R.color.theme_red);
                mSubPanelContainer.removeAllViews();
                mSubPanelContainer.addView(mStickerAssetPanel);
                break;
            case R.id.tv_sticker_adjust:
                mStickerPanel.findViewById(R.id.tv_sticker_asset).setBackgroundResource(R.color.theme_dark);
                mStickerPanel.findViewById(R.id.tv_sticker_adjust).setBackgroundResource(R.color.theme_dark);
                v.setBackgroundResource(R.color.theme_red);
                mSubPanelContainer.removeAllViews();
                mSubPanelContainer.addView(mStickerAdjustPanel);
                break;
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mCurOp == null) {
            return;
        }
        switch (seekBar.getId()) {
            case R.id.sb_sticker_alpha:
                mCurOp.setAlphaFactor(progress * 1.0f / seekBar.getMax());
                break;
            case R.id.sb_sticker_size:
                mCurOp.setScaleFactor(progress * 1.0f / seekBar.getMax());
                break;
            case R.id.sb_sticker_red:
                mCurOp.setRed(progress * 1.0f / seekBar.getMax());
                break;
            case R.id.sb_sticker_green:
                mCurOp.setGreen(progress * 1.0f / seekBar.getMax());
                break;
            case R.id.sb_sticker_blue:
                mCurOp.setBlue(progress * 1.0f / seekBar.getMax());
                break;
            case R.id.sb_sticker_rotation:
                mCurOp.setRotationZ(progress);
                break;
            default:
                break;
        }

        IEManager.getInstance().renderClip(0);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void initMainPanel() {
        mStickerPanel = LayoutInflater.from(mContext).inflate(R.layout.edit_panel_stickers, mParent, false);
        mSubPanelContainer = mStickerPanel.findViewById(R.id.vw_sticker_sub_panel_container);
        mStickerPanel.findViewById(R.id.tv_sticker_asset).setOnClickListener(this);
        mStickerPanel.findViewById(R.id.tv_sticker_adjust).setOnClickListener(this);
        mStickerPanel.findViewById(R.id.iv_cancel).setOnClickListener(this);
        mStickerPanel.findViewById(R.id.iv_apply).setOnClickListener(this);
    }

    private void initAssetPanel() {
        mStickerAssetPanel = LayoutInflater.from(mContext)
                .inflate(R.layout.edit_panel_stickers_asset, mSubPanelContainer, false);
        mStickerListView = mStickerAssetPanel.findViewById(R.id.rcv_sticker_list);
        mStickerListView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mStickerListAdapter = new StickerListAdapter(mContext, mStickerList, this);
        mStickerListView.setAdapter(mStickerListAdapter);
    }

    private void initAdjustPanel() {
        mStickerAdjustPanel = LayoutInflater.from(mContext)
                .inflate(R.layout.edit_panel_stickers_adjust, mSubPanelContainer, false);
        ((SeekBar) mStickerAdjustPanel.findViewById(R.id.sb_sticker_alpha)).setOnSeekBarChangeListener(this);
        ((SeekBar) mStickerAdjustPanel.findViewById(R.id.sb_sticker_size)).setOnSeekBarChangeListener(this);
        ((SeekBar) mStickerAdjustPanel.findViewById(R.id.sb_sticker_red)).setOnSeekBarChangeListener(this);
        ((SeekBar) mStickerAdjustPanel.findViewById(R.id.sb_sticker_green)).setOnSeekBarChangeListener(this);
        ((SeekBar) mStickerAdjustPanel.findViewById(R.id.sb_sticker_blue)).setOnSeekBarChangeListener(this);
        ((SeekBar) mStickerAdjustPanel.findViewById(R.id.sb_sticker_rotation)).setOnSeekBarChangeListener(this);
    }
}
