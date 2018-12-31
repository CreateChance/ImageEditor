package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.CannabisLeafTransDrawer;

/**
 * Cannabis leaf transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CannabisLeafTransition extends AbstractTransition {

    private static final String TAG = "CannabisLeafTransition";

    public CannabisLeafTransition() {
        super(CannabisLeafTransition.class.getSimpleName(), TRANS_CANNABIS_LEAF);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new CannabisLeafTransDrawer();
    }
}
