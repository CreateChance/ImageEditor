package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.CrossWarpTransDrawer;

/**
 * Cross warp transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrossWarpTransition extends AbstractTransition {

    private static final String TAG = "CrossWarpTransition";

    public CrossWarpTransition() {
        super(CrossWarpTransition.class.getSimpleName(), TRANS_CROSS_WARP);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new CrossWarpTransDrawer();
    }
}
