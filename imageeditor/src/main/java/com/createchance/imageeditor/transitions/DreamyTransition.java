package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.DreamyTransDrawer;

/**
 * Dreamy transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DreamyTransition extends AbstractTransition {

    private static final String TAG = "DreamyTransition";

    public DreamyTransition() {
        super(DreamyTransition.class.getSimpleName(), TRANS_DREAMY);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new DreamyTransDrawer();
    }
}
