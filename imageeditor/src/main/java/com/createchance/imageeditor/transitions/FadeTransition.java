package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.FadeTransDrawer;

/**
 * Fade transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class FadeTransition extends AbstractTransition {

    private static final String TAG = "FadeTransition";

    public FadeTransition() {
        super(FadeTransition.class.getSimpleName(), TRANS_FADE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new FadeTransDrawer();
    }
}
