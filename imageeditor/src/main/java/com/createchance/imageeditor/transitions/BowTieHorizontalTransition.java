package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.BowTieHorizontalTransDrawer;
import com.createchance.imageeditor.utils.Logger;

/**
 * Bow tie horizontal transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BowTieHorizontalTransition extends AbstractTransition {

    private static final String TAG = "BowTieHorizontalTransit";

    private BowTieHorizontalTransDrawer mDrawer;

    public BowTieHorizontalTransition() {
        super(BowTieHorizontalTransition.class.getSimpleName(), TRANS_BOW_TIE_HORIZONTAL);
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
                mDrawer = new BowTieHorizontalTransDrawer();
            }

            mDrawer.setProgress(mProgress);

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
