package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.MorphTransDrawer;

/**
 * Morph transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class MorphTransition extends AbstractTransition {

    private static final String TAG = "MorphTransition";

    private float mStrength = 0.1f;

    public MorphTransition() {
        super(MorphTransition.class.getSimpleName(), TRANS_MORPH);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new MorphTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((MorphTransDrawer) mDrawer).setStrength(mStrength);
    }
}
