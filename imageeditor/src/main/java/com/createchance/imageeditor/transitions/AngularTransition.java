package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.AngularTransDrawer;
import com.createchance.imageeditor.utils.Logger;

/**
 * Angular transition.
 *
 * @author createchance
 * @date 2018/12/30
 */
public class AngularTransition extends AbstractTransition {

    private static final String TAG = "AngularTransition";

    private AngularTransDrawer mDrawer;

    private float mStartAngle = 90.0f;

    public AngularTransition() {
        super(AngularTransition.class.getSimpleName(), TRANS_ANGULAR);
    }

    @Override
    public boolean checkRational() {
        return mStartAngle >= 0 && mStartAngle <= 360;
    }

    @Override
    public void exec() {
        int texture2 = mContext.getToTextureId();
        if (texture2 != -1) {
            mContext.attachOffScreenTexture(mContext.getOutputTextureId());
            if (mDrawer == null) {
                mDrawer = new AngularTransDrawer();
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
