package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.WipeDownTransDrawer;

/**
 * Wipe down transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WipeDownTransition extends AbstractTransition {

    public WipeDownTransition() {
        super(WipeDownTransition.class.getSimpleName(), TRANS_WIPE_DOWN);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new WipeDownTransDrawer();
    }
}
