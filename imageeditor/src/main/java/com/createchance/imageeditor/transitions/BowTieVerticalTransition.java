package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.BowTieVerticalTransDrawer;
import com.createchance.imageeditor.utils.Logger;

/**
 * Bow tie vertical transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BowTieVerticalTransition extends AbstractTransition {

    private static final String TAG = "BowTieVerticalTransitio";

    private BowTieVerticalTransDrawer mDrawer;

    public BowTieVerticalTransition() {
        super(BowTieVerticalTransition.class.getSimpleName(), TRANS_BOW_TIE_VERTICAL);
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
                mDrawer = new BowTieVerticalTransDrawer();
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
