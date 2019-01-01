package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.MosaicTransDrawer;

/**
 * Mosaic transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class MosaicTransition extends AbstractTransition {

    private static final String TAG = "MosaicTransition";

    private int mEndX = 2, mEndY = -1;

    public MosaicTransition() {
        super(MosaicTransition.class.getSimpleName(), TRANS_MOSAIC);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new MosaicTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((MosaicTransDrawer) mDrawer).setEndX(mEndX);
        ((MosaicTransDrawer) mDrawer).setEndY(mEndY);
    }
}
