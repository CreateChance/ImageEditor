package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.DirectionalWarpTransDrawer;

/**
 * Directional warp transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DirectionalWarpTransition extends AbstractTransition {

    private static final String TAG = "DirectionalWarpTransiti";

    private float mDirectionX = -1, mDirectionY = 1;

    public DirectionalWarpTransition() {
        super(DirectionalWarpTransition.class.getSimpleName(), TRANS_DIRECTIONAL_WARP);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new DirectionalWarpTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((DirectionalWarpTransDrawer) mDrawer).setDirectional(mDirectionX, mDirectionY);
    }
}
