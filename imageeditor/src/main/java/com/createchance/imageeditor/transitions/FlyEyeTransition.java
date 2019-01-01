package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.FlyEyeTransDrawer;

/**
 * Fly eye transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class FlyEyeTransition extends AbstractTransition {

    private static final String TAG = "FlyEyeTransition";

    private float mSize = 0.04f;
    private float mZoom = 50;
    private float mColorSeparation = 0.3f;

    public FlyEyeTransition() {
        super(FlyEyeTransition.class.getSimpleName(), TRANS_FLY_EYE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new FlyEyeTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((FlyEyeTransDrawer) mDrawer).setSize(mSize);
        ((FlyEyeTransDrawer) mDrawer).setZoom(mZoom);
        ((FlyEyeTransDrawer) mDrawer).setColorSeparation(mColorSeparation);
    }
}
