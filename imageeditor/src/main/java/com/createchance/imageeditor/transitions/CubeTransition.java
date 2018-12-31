package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.CubeTransDrawer;

/**
 * Cube transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CubeTransition extends AbstractTransition {

    private static final String TAG = "CubeTransition";

    private float mPerspective = 0.7f;
    private float mUnzoom = 0.3f;
    private float mReflection = 0.4f;
    private float mFloating = 3.0f;

    public CubeTransition() {
        super(CubeTransition.class.getSimpleName(), TRANS_CUBE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new CubeTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((CubeTransDrawer) mDrawer).setPerspective(mPerspective);
        ((CubeTransDrawer) mDrawer).setUnzoom(mUnzoom);
        ((CubeTransDrawer) mDrawer).setReflection(mReflection);
        ((CubeTransDrawer) mDrawer).setFloating(mFloating);
    }
}
