package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.UndulatingBurnOutTransDrawer;

/**
 * Undulating burnout transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class UndulatingBurnOutTransition extends AbstractTransition {

    private static final String TAG = "UndulatingBurnOutTransi";

    private float mCenterX = 0.5f, mCenterY = 0.5f;
    private float mSmoothness = 0.03f;
    private float mRed, mGreen, mBlue;

    public UndulatingBurnOutTransition() {
        super(UndulatingBurnOutTransition.class.getSimpleName(), TRANS_UNDULATING_BURN_OUT);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new UndulatingBurnOutTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((UndulatingBurnOutTransDrawer) mDrawer).setCenter(mCenterX, mCenterY);
        ((UndulatingBurnOutTransDrawer) mDrawer).setSmoothness(mSmoothness);
        ((UndulatingBurnOutTransDrawer) mDrawer).setColor(mRed, mGreen, mBlue);
    }
}
