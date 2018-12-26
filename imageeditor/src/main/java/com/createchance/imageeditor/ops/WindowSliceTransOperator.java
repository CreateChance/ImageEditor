package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.WindowSliceTransDrawer;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/12/23
 */
public class WindowSliceTransOperator extends AbstractOperator {

    private static final String TAG = "WindowSliceTransOperato";

    private WindowSliceTransDrawer mDrawer;

    private float mProgress;
    private float mCount = 10.0f;
    private float mSmoothness = 0.5f;

    public WindowSliceTransOperator() {
        super(WindowSliceTransOperator.class.getSimpleName(), OP_TRANS_WINDOW_SLICE);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new WindowSliceTransDrawer();
        }

        mDrawer.setProgress(mProgress);
        mDrawer.setCount(mCount);
        mDrawer.setSmoothness(mSmoothness);

        mDrawer.draw(mContext.getInputTextureId(),
                mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }
}
