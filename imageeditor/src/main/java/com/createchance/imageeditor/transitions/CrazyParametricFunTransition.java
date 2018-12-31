package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.CrazyParametricFunTransDrawer;

/**
 * Crazy parametric fun transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrazyParametricFunTransition extends AbstractTransition {

    private static final String TAG = "CrazyParametricFunTrans";

    private float mA = 4;
    private float mB = 1;
    private float mAmplitude = 120;
    private float mSmoothness = 0.1f;

    public CrazyParametricFunTransition() {
        super(CrazyParametricFunTransition.class.getSimpleName(), TRANS_CRAZY_PARAMETRIC_FUN);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new CrazyParametricFunTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((CrazyParametricFunTransDrawer) mDrawer).setA(mA);
        ((CrazyParametricFunTransDrawer) mDrawer).setB(mB);
        ((CrazyParametricFunTransDrawer) mDrawer).setAmplitude(mAmplitude);
        ((CrazyParametricFunTransDrawer) mDrawer).setSmoothness(mSmoothness);
    }
}
