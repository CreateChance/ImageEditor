package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.PinWheelTransDrawer;

/**
 * Pin wheel transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PinWheelTransition extends AbstractTransition {

    private static final String TAG = "PinWheelTransition";

    private float mSpeed = 2.0f;

    public PinWheelTransition() {
        super(PinWheelTransition.class.getSimpleName(), TRANS_PIN_WHEEL);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new PinWheelTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((PinWheelTransDrawer) mDrawer).setSpeed(mSpeed);
    }
}
