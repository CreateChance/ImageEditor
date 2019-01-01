package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.HeartTransDrawer;

/**
 * Heart transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class HeartTransition extends AbstractTransition {

    private static final String TAG = "HeartTransition";

    public HeartTransition() {
        super(HeartTransition.class.getSimpleName(), TRANS_HEART);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new HeartTransDrawer();
    }
}
