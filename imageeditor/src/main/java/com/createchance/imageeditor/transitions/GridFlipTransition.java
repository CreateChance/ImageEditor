package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.GridFlipTransDrawer;

/**
 * Grid flip transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class GridFlipTransition extends AbstractTransition {

    private static final String TAG = "GridFlipTransition";

    private int mGridWidth = 4, mGridHeight = 4;
    private float mPause = 0.1f;
    private float mDividerWidth = 0.05f;
    private float mBackRed, mBackGreen, mBackBlue, mBackAlpha = 1;
    private float mRandomness = 0.1f;

    public GridFlipTransition() {
        super(GridFlipTransition.class.getSimpleName(), TRANS_GRID_FLIP);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new GridFlipTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((GridFlipTransDrawer) mDrawer).setSize(mGridWidth, mGridHeight);
        ((GridFlipTransDrawer) mDrawer).setPause(mPause);
        ((GridFlipTransDrawer) mDrawer).setDividerWidth(mDividerWidth);
        ((GridFlipTransDrawer) mDrawer).setBackColor(mBackRed, mBackGreen, mBackBlue, mBackAlpha);
        ((GridFlipTransDrawer) mDrawer).setRandomness(mRandomness);
    }
}
