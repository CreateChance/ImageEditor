package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.PolarFunctionTransDrawer;

/**
 * Polar function transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PolarFunctionTransition extends AbstractTransition {

    private static final String TAG = "PolarFunctionTransition";

    private int mSegments = 5;

    public PolarFunctionTransition() {
        super(PolarFunctionTransition.class.getSimpleName(), TRANS_POLAR_FUNCTION);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new PolarFunctionTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((PolarFunctionTransDrawer) mDrawer).setSegments(mSegments);
    }
}
