package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.ColorPhaseTransDrawer;

/**
 * Color phase transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class ColorPhaseTransition extends AbstractTransition {

    private static final String TAG = "ColorPhaseTransition";

    private float mFromRed, mFromGreen = 0.2f, mFromBlue = 0.4f, mFromAlpha;
    private float mToRed = 0.6f, mToGreen = 0.8f, mToBlue = 1.0f, mToAlpha = 1.0f;

    public ColorPhaseTransition() {
        super(ColorPhaseTransition.class.getSimpleName(), TRANS_COLOR_PHASE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new ColorPhaseTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((ColorPhaseTransDrawer) mDrawer).setFromStep(mFromRed, mFromGreen, mFromBlue, mFromAlpha);
        ((ColorPhaseTransDrawer) mDrawer).setToStep(mToRed, mToGreen, mToBlue, mToAlpha);
    }
}
