package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.WipeRightTransDrawer;

/**
 * Wipe up transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WipeRightTransition extends AbstractTransition {

    public WipeRightTransition() {
        super(WipeRightTransition.class.getSimpleName(), TRANS_WIPE_RIGHT);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new WipeRightTransDrawer();
    }
}
