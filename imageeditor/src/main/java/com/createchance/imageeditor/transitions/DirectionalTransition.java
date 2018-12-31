package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.DirectionalTransDrawer;

/**
 * Directional transition
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DirectionalTransition extends AbstractTransition {

    private static final String TAG = "DirectionalTransition";

    private float mDirectionX = 0, mDirectionY = 1;

    public DirectionalTransition() {
        super(DirectionalTransition.class.getSimpleName(), TRANS_DIRECTIONAL);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new DirectionalTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((DirectionalTransDrawer) mDrawer).setDirectional(mDirectionX, mDirectionY);
    }
}
