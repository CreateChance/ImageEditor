package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.SwirlTransDrawer;

/**
 * Swirl transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SwirlTransition extends AbstractTransition {

    private static final String TAG = "SwirlTransition";

    public SwirlTransition() {
        super(SwirlTransition.class.getSimpleName(), TRANS_SWIRL);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new SwirlTransDrawer();
    }
}
