package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.FadeGrayScaleTransDrawer;

/**
 * Fade gray scale transition
 *
 * @author createchance
 * @date 2019/1/1
 */
public class FadeGrayScaleTransition extends AbstractTransition {

    private static final String TAG = "FadeGrayScaleTransition";

    private float mIntensity = 0.3f;

    public FadeGrayScaleTransition() {
        super(FadeGrayScaleTransition.class.getSimpleName(), TRANS_FADE_GRAY_SCALE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new FadeGrayScaleTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((FadeGrayScaleTransDrawer) mDrawer).setIntensity(mIntensity);
    }
}
