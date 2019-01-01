package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.RandomSquaresTransDrawer;

/**
 * Random square transition
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RandomSquaresTransition extends AbstractTransition {

    private static final String TAG = "RandomSquaresTransition";

    private float mSmoothness = 0.5f;
    private int mWidth = 10, mHeight = 10;

    public RandomSquaresTransition() {
        super(RandomSquaresTransition.class.getSimpleName(), TRANS_RANDOM_SQUARES);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new RandomSquaresTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((RandomSquaresTransDrawer) mDrawer).setSize(mWidth, mHeight);
        ((RandomSquaresTransDrawer) mDrawer).setSmoothness(mSmoothness);
    }
}
