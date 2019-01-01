package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.RotateScaleFadeTransDrawer;

/**
 * Rotate scale fade transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RotateScaleFadeTransition extends AbstractTransition {

    private static final String TAG = "RotateScaleFadeTransiti";

    private float mCenterX = 0.5f, mCenterY = 0.5f;
    private float mRotations = 1;
    private float mScale = 8;
    private float mBackRed = 0.15f, mBackGreen = 0.15f, mBackBlue = 0.15f, mBackAlpha = 1.0f;

    public RotateScaleFadeTransition() {
        super(RotateScaleFadeTransition.class.getSimpleName(), TRANS_ROTATE_SCALE_FADE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new RotateScaleFadeTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((RotateScaleFadeTransDrawer) mDrawer).setCenter(mCenterX, mCenterY);
        ((RotateScaleFadeTransDrawer) mDrawer).setRotations(mRotations);
        ((RotateScaleFadeTransDrawer) mDrawer).setScale(mScale);
        ((RotateScaleFadeTransDrawer) mDrawer).setBackColor(mBackRed, mBackGreen, mBackBlue, mBackAlpha);
    }
}
