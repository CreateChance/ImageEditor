package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.DirectionalWipeTransDrawer;

/**
 * Directional wipe transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DirectionalWipeTransition extends AbstractTransition {

    private static final String TAG = "DirectionalWipeTransiti";

    private float mDirectionX = 1, mDirectionY = -1;
    private float mSmoothness = 0.5f;

    public DirectionalWipeTransition() {
        super(DirectionalWipeTransition.class.getSimpleName(), TRANS_DIRECTIONAL_WIPE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new DirectionalWipeTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((DirectionalWipeTransDrawer) mDrawer).setDirection(mDirectionX, mDirectionY);
        ((DirectionalWipeTransDrawer) mDrawer).setSmoothness(mSmoothness);
    }
}
