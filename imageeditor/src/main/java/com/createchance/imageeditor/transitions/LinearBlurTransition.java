package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.LinearBlurTransDrawer;

/**
 * Linear blur transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class LinearBlurTransition extends AbstractTransition {

    private static final String TAG = "LinearBlurTransition";

    private float mIntensity = 0.1f;
    private int mPasses = 6;

    public LinearBlurTransition() {
        super(LinearBlurTransition.class.getSimpleName(), TRANS_LINEAR_BLUR);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new LinearBlurTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((LinearBlurTransDrawer) mDrawer).setIntensity(mIntensity);
        ((LinearBlurTransDrawer) mDrawer).setPasses(mPasses);
    }
}
