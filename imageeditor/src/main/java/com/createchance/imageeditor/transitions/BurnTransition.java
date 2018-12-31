package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.BurnTransDrawer;
import com.createchance.imageeditor.utils.Logger;

/**
 * Burn transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BurnTransition extends AbstractTransition {

    private static final String TAG = "BurnTransition";

    private BurnTransDrawer mDrawer;

    private float mRed = 0.9f, mGreen = 0.4f, mBlue = 0.2f;

    public BurnTransition() {
        super(BurnTransition.class.getSimpleName(), TRANS_BURN);
    }

    @Override
    public boolean checkRational() {
        return mRed >= 0 && mRed <= 1 && mGreen >= 0 && mGreen <= 1 && mBlue >= 0 && mBlue <= 1;
    }

    @Override
    public void exec() {
        int texture2 = mContext.getToTextureId();
        if (texture2 != -1) {
            mContext.attachOffScreenTexture(mContext.getOutputTextureId());
            if (mDrawer == null) {
                mDrawer = new BurnTransDrawer();
            }

            mDrawer.setProgress(mProgress);
            mDrawer.setColor(mRed, mGreen, mBlue);

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
