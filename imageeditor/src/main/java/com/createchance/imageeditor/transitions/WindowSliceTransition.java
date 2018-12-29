package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.WindowSliceTransDrawer;
import com.createchance.imageeditor.utils.Logger;

/**
 * Window slice trans.
 *
 * @author createchance
 * @date 2018/12/23
 */
public class WindowSliceTransition extends AbstractTransition {

    private static final String TAG = "WindowSliceTransition";

    private WindowSliceTransDrawer mDrawer;

    private float mCount = 10.0f;
    private float mSmoothness = 0.5f;

    public WindowSliceTransition() {
        super(WindowSliceTransition.class.getSimpleName(), TRANS_WINDOW_SLICE);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        int texture2 = mContext.getToTextureId();
        if (texture2 != -1) {
            mContext.attachOffScreenTexture(mContext.getOutputTextureId());
            if (mDrawer == null) {
                mDrawer = new WindowSliceTransDrawer();
            }

            mDrawer.setProgress(mProgress);
            mDrawer.setCount(mCount);
            mDrawer.setSmoothness(mSmoothness);

            mDrawer.draw(mContext.getFromTextureId(),
                    texture2,
                    mContext.getRenderLeft(),
                    mContext.getRenderBottom(),
                    mContext.getRenderWidth(),
                    mContext.getRenderHeight());
            mContext.swapTexture();
        } else {
            Logger.e(TAG, "Can not get texture 2 id.");
        }
    }
}
