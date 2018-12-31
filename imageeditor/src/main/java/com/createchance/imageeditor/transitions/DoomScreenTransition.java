package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.DoomScreenTransDrawer;

/**
 * Doom screen transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DoomScreenTransition extends AbstractTransition {

    private static final String TAG = "DoomScreenTransition";

    private int mBars = 30;
    private float mAmplitude = 2;
    private float mNoise = 0.1f;
    private float mFrequency = 0.5f;
    private float mDripScale = 0.5f;

    public DoomScreenTransition() {
        super(DoomScreenTransition.class.getSimpleName(), TRANS_DOOM_SCREEN);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new DoomScreenTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((DoomScreenTransDrawer) mDrawer).setBars(mBars);
        ((DoomScreenTransDrawer) mDrawer).setAmplitude(mAmplitude);
        ((DoomScreenTransDrawer) mDrawer).setNoise(mNoise);
        ((DoomScreenTransDrawer) mDrawer).setFrequency(mFrequency);
        ((DoomScreenTransDrawer) mDrawer).setDripScale(mDripScale);
    }
}
