package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.FadeColorTransDrawer;

/**
 * Fade color transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class FadeColorTransition extends AbstractTransition {

    private static final String TAG = "FadeColorTransition";

    private float mRed, mGreen, mBlue;
    private float mColorPhase = 0.4f;

    public FadeColorTransition() {
        super(FadeColorTransition.class.getSimpleName(), TRANS_FADE_COLOR);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new FadeColorTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((FadeColorTransDrawer) mDrawer).setColor(mRed, mGreen, mBlue);
        ((FadeColorTransDrawer) mDrawer).setColorPhase(mColorPhase);
    }
}
