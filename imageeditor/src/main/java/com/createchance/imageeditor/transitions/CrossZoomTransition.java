package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.CrossZoomTransDrawer;
import com.createchance.imageeditor.shaders.CrossZoomTransShader;

/**
 * Cross zoom transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrossZoomTransition extends AbstractTransition {

    private static final String TAG = "CrossZoomTransition";

    private float mStength = 0.4f;

    public CrossZoomTransition() {
        super(CrossZoomTransShader.class.getSimpleName(), TRANS_CROSS_ZOOM);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new CrossZoomTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((CrossZoomTransDrawer) mDrawer).setStrength(mStength);
    }
}
