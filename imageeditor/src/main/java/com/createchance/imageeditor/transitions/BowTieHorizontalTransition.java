package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.BowTieHorizontalTransDrawer;

/**
 * Bow tie horizontal transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BowTieHorizontalTransition extends AbstractTransition {

    private static final String TAG = "BowTieHorizontalTransit";

    public BowTieHorizontalTransition() {
        super(BowTieHorizontalTransition.class.getSimpleName(), TRANS_BOW_TIE_HORIZONTAL);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new BowTieHorizontalTransDrawer();
    }
}
