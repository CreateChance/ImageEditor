package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.ZoomInCirclesTransDrawer;

/**
 * Zoom int circles transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class ZoomInCirclesTransition extends AbstractTransition {

    public ZoomInCirclesTransition() {
        super(ZoomInCirclesTransition.class.getSimpleName(), TRANS_ZOOM_IN_CIRCLES);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new ZoomInCirclesTransDrawer();
    }
}
