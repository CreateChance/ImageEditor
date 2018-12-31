package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.CircleTransDrawer;

/**
 * Circle transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CircleTransition extends AbstractTransition {

    private static final String TAG = "CircleTransition";

    private float mCenterX = 0.5f, mCenterY = 0.5f;
    private float mBackRed = 0.1f, mBackGreen = 0.1f, mBackBlue = 0.1f;

    public CircleTransition() {
        super(CircleTransition.class.getSimpleName(), TRANS_CIRCLE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new CircleTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((CircleTransDrawer) mDrawer).setCenter(mCenterX, mCenterY);
        ((CircleTransDrawer) mDrawer).setBackColor(mBackRed, mBackGreen, mBackBlue);
    }
}
