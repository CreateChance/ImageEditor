package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.DreamyZoomTransDrawer;

/**
 * Dreamy zoom transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DreamyZoomTransition extends AbstractTransition {

    private static final String TAG = "DreamyZoomTransition";

    private float mRotation = 6;
    private float mScale = 1.2f;

    public DreamyZoomTransition() {
        super(DreamyZoomTransition.class.getSimpleName(), TRANS_DREAMY_ZOOM);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new DreamyZoomTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((DreamyZoomTransDrawer) mDrawer).setRotation(mRotation);
        ((DreamyZoomTransDrawer) mDrawer).setScale(mScale);
    }
}
