package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.BowTieVerticalTransDrawer;

/**
 * Bow tie vertical transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BowTieVerticalTransition extends AbstractTransition {

    private static final String TAG = "BowTieVerticalTransitio";

    public BowTieVerticalTransition() {
        super(BowTieVerticalTransition.class.getSimpleName(), TRANS_BOW_TIE_VERTICAL);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new BowTieVerticalTransDrawer();
    }
}
