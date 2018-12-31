package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.CrossHatchTransDrawer;

/**
 * Cross hatch transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrossHatchTransition extends AbstractTransition {

    private static final String TAG = "CrossHatchTransition";

    private float mCenterX = 0.5f, mCenterY = 0.5f;
    private float mThreshold = 3.0f;
    private float mFadeEdge = 0.1f;

    public CrossHatchTransition() {
        super(CrossHatchTransition.class.getSimpleName(), TRANS_CROSS_HATCH);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new CrossHatchTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((CrossHatchTransDrawer) mDrawer).setCenter(mCenterX, mCenterY);
        ((CrossHatchTransDrawer) mDrawer).setThreshold(mThreshold);
        ((CrossHatchTransDrawer) mDrawer).setFadeEdge(mFadeEdge);
    }
}
