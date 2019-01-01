package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.LuminanceMeltTransDrawer;

/**
 * Luminance melt transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class LuminanceMeltTransition extends AbstractTransition {

    private static final String TAG = "LuminanceMeltTransition";

    private boolean mDown = true;
    private float mThreshold = 0.8f;
    private boolean mAbove = false;

    public LuminanceMeltTransition() {
        super(LuminanceMeltTransition.class.getSimpleName(), TRANS_LUMINANCE_MELT);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new LuminanceMeltTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((LuminanceMeltTransDrawer) mDrawer).setAbove(mAbove);
        ((LuminanceMeltTransDrawer) mDrawer).setDirection(mDown);
        ((LuminanceMeltTransDrawer) mDrawer).setThreshold(mThreshold);
    }
}
