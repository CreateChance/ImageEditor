package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.BurnTransDrawer;

/**
 * Burn transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BurnTransition extends AbstractTransition {

    private static final String TAG = "BurnTransition";

    private float mRed = 0.9f, mGreen = 0.4f, mBlue = 0.2f;

    public BurnTransition() {
        super(BurnTransition.class.getSimpleName(), TRANS_BURN);
    }

    @Override
    public boolean checkRational() {
        return mRed >= 0 && mRed <= 1 && mGreen >= 0 && mGreen <= 1 && mBlue >= 0 && mBlue <= 1;
    }

    @Override
    protected void getDrawer() {
        mDrawer = new BurnTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();
        ((BurnTransDrawer) mDrawer).setColor(mRed, mGreen, mBlue);
    }
}
