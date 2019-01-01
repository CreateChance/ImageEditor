package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.PolkaDotsCurtainTransDrawer;

/**
 * Polka dots curtain transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PolkaDotsCurtainTransition extends AbstractTransition {

    private static final String TAG = "PolkaDotsCurtainTransit";

    private float mDots = 20;
    private float mCenterX, mCenterY;

    public PolkaDotsCurtainTransition() {
        super(PolkaDotsCurtainTransition.class.getSimpleName(), TRANS_POLKA_DOTS_CURTAIN);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new PolkaDotsCurtainTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((PolkaDotsCurtainTransDrawer) mDrawer).setDots(mDots);
        ((PolkaDotsCurtainTransDrawer) mDrawer).setCenter(mCenterX, mCenterY);
    }
}
