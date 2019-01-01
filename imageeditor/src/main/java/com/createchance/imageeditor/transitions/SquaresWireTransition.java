package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.SquaresWireTransDrawer;

/**
 * Squares wire transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SquaresWireTransition extends AbstractTransition {

    private static final String TAG = "SquaresWireTransition";

    private int mWidth = 10, mHeight = 10;
    private float mDirectionX = 1.0f, mDirectionY = -0.5f;
    private float mSmoothness = 1.6f;

    public SquaresWireTransition() {
        super(SquaresWireTransition.class.getSimpleName(), TRANS_SQUARES_WIRE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new SquaresWireTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((SquaresWireTransDrawer) mDrawer).setSquare(mWidth, mHeight);
        ((SquaresWireTransDrawer) mDrawer).setDirection(mDirectionX, mDirectionY);
        ((SquaresWireTransDrawer) mDrawer).setSmoothness(mSmoothness);
    }
}
