package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.AngularTransDrawer;

/**
 * Angular transition.
 *
 * @author createchance
 * @date 2018/12/30
 */
public class AngularTransition extends AbstractTransition {

    private static final String TAG = "AngularTransition";

    private float mStartAngle = 90.0f;

    public AngularTransition() {
        super(AngularTransition.class.getSimpleName(), TRANS_ANGULAR);
    }

    @Override
    public boolean checkRational() {
        return mStartAngle >= 0 && mStartAngle <= 360;
    }

    @Override
    protected void getDrawer() {
        mDrawer = new AngularTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        ((AngularTransDrawer) mDrawer).setStartAngular(mStartAngle);
    }
}
