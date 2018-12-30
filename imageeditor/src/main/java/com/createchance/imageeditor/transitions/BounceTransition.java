package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.BounceTransDrawer;
import com.createchance.imageeditor.utils.Logger;

/**
 * Bounce transition.
 *
 * @author createchance
 * @date 2018/12/30
 */
public class BounceTransition extends AbstractTransition {

    private static final String TAG = "BounceTransition";

    private BounceTransDrawer mDrawer;

    private float mShadowRed, mShadowGreen, mShadowBlue, mShadowAlpha = 0.6f;
    private float mShadowHeight = 0.075f;
    private float mBounces = 3.0f;

    public BounceTransition() {
        super(BounceTransition.class.getSimpleName(), TRANS_BOUNCE);
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
                mDrawer = new BounceTransDrawer();
            }

            mDrawer.setProgress(mProgress);
            mDrawer.setShadowColor(mShadowRed, mShadowGreen, mShadowBlue, mShadowAlpha);
            mDrawer.setShadowHeight(mShadowHeight);
            mDrawer.setBounces(mBounces);

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
