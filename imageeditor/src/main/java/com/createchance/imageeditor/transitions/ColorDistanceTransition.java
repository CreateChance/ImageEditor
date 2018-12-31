package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.ColorDistanceTransDrawer;

/**
 * Color distance transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class ColorDistanceTransition extends AbstractTransition {

    private static final String TAG = "ColorDistanceTransition";

    private float mPower = 5.0f;

    public ColorDistanceTransition() {
        super(ColorDistanceTransition.class.getSimpleName(), TRANS_COLOR_DISTANCE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new ColorDistanceTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((ColorDistanceTransDrawer) mDrawer).setPower(mPower);
    }
}
