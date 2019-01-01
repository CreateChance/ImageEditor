package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.RadialTransDrawer;

/**
 * Radial transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RadialTransition extends AbstractTransition {

    private static final String TAG = "RadialTransition";

    private float mSmoothness = 1.0f;

    public RadialTransition() {
        super(RadialTransition.class.getSimpleName(), TRANS_RADIAL);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new RadialTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((RadialTransDrawer) mDrawer).setSmoothness(mSmoothness);
    }
}
