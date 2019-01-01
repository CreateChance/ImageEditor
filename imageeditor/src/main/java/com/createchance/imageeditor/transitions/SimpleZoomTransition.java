package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.SimpleZoomTransDrawer;

/**
 * Simple zoom transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SimpleZoomTransition extends AbstractTransition {

    private static final String TAG = "SimpleZoomTransition";

    private float mZoomQuickness = 0.8f;

    public SimpleZoomTransition() {
        super(SimpleZoomTransition.class.getSimpleName(), TRANS_SIMPLE_ZOOM);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new SimpleZoomTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((SimpleZoomTransDrawer) mDrawer).setZoomQuickness(mZoomQuickness);
    }
}
