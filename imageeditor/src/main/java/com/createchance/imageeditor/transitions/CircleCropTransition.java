package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.CircleCropTransDrawer;

/**
 * Circle corp transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CircleCropTransition extends AbstractTransition {

    private static final String TAG = "CircleCropTransition";

    private float mBackRed = 0.0f, mBackGreen, mBackBlue, mBackAlpha = 1.0f;

    public CircleCropTransition() {
        super(CircleCropTransition.class.getSimpleName(), TRANS_CIRCLE_CORP);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new CircleCropTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((CircleCropTransDrawer) mDrawer).setBackColor(mBackRed, mBackGreen, mBackBlue, mBackAlpha);
    }
}
