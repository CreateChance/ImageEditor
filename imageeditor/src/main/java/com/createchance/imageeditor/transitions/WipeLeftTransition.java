package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.WipeLeftTransDrawer;

/**
 * Wipe left transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WipeLeftTransition extends AbstractTransition {

    public WipeLeftTransition() {
        super(WipeLeftTransition.class.getSimpleName(), TRANS_WIPE_LEFT);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new WipeLeftTransDrawer();
    }
}
