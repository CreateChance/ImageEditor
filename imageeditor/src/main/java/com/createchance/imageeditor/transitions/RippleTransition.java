package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.RippleTransDrawer;

/**
 * Ripple transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RippleTransition extends AbstractTransition {

    private static final String TAG = "RippleTransition";

    private float mAmplitude = 100;
    private float mSpeed = 50;

    public RippleTransition() {
        super(RippleTransition.class.getSimpleName(), TRANS_RIPPLE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new RippleTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((RippleTransDrawer) mDrawer).setAmplitude(mAmplitude);
        ((RippleTransDrawer) mDrawer).setSpeed(mSpeed);
    }
}
