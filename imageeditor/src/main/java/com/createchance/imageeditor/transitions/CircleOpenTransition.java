package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.CircleOpenTransDrawer;

/**
 * Circle open transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CircleOpenTransition extends AbstractTransition {

    private static final String TAG = "CircleOpenTransition";

    private float mSmoothness = 0.3f;
    private boolean mOpening = true;

    public CircleOpenTransition() {
        super(CircleOpenTransition.class.getSimpleName(), TRANS_CIRCLE_OPEN);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new CircleOpenTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((CircleOpenTransDrawer) mDrawer).setSmoothness(mSmoothness);
        ((CircleOpenTransDrawer) mDrawer).setOpening(mOpening);
    }
}
