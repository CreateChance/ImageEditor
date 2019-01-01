package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.WipeUpTransDrawer;

/**
 * Wipe up transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WipeUpTransition extends AbstractTransition {

    public WipeUpTransition() {
        super(WipeUpTransition.class.getSimpleName(), TRANS_WIPE_UP);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new WipeUpTransDrawer();
    }
}
