package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.PerlinTransDrawer;

/**
 * Perlin transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PerlinTransition extends AbstractTransition {

    private static final String TAG = "PerlinTransition";

    private float mScale = 4.0f;
    private float mSmoothness = 0.01f;
    private float mSeed = 12.9898f;

    public PerlinTransition() {
        super(PerlinTransition.class.getSimpleName(), TRANS_PERLIN);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new PerlinTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((PerlinTransDrawer) mDrawer).setScale(mScale);
        ((PerlinTransDrawer) mDrawer).setSmoothness(mSmoothness);
        ((PerlinTransDrawer) mDrawer).setSeed(mSeed);
    }
}
