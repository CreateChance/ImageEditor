package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.StereoViewerTransDrawer;

/**
 * Stereo viewer transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class StereoViewerTransition extends AbstractTransition {

    private static final String TAG = "StereoViewerTransition";

    private float mZoom = 0.88f;
    private float mCornerRadius = 0.22f;

    public StereoViewerTransition() {
        super(StereoViewerTransition.class.getSimpleName(), TRANS_STEREO_VIEWER);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new StereoViewerTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((StereoViewerTransDrawer) mDrawer).setZoom(mZoom);
        ((StereoViewerTransDrawer) mDrawer).setCornerRadius(mCornerRadius);
    }
}
