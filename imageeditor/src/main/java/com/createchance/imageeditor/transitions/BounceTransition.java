package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.BounceTransDrawer;

/**
 * Bounce transition.
 *
 * @author createchance
 * @date 2018/12/30
 */
public class BounceTransition extends AbstractTransition {

    private static final String TAG = "BounceTransition";

    private float mShadowRed, mShadowGreen, mShadowBlue, mShadowAlpha = 0.6f;
    private float mShadowHeight = 0.075f;
    private float mBounces = 3.0f;

    public BounceTransition() {
        super(BounceTransition.class.getSimpleName(), TRANS_BOUNCE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new BounceTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        ((BounceTransDrawer) mDrawer).setShadowColor(mShadowRed, mShadowGreen, mShadowBlue, mShadowAlpha);
        ((BounceTransDrawer) mDrawer).setShadowHeight(mShadowHeight);
        ((BounceTransDrawer) mDrawer).setBounces(mBounces);
    }
}
