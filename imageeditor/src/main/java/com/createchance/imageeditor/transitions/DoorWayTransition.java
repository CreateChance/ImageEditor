package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.DoorWayTransDrawer;

/**
 * Door way transition.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DoorWayTransition extends AbstractTransition {

    private static final String TAG = "DoorWayTransition";

    private float mReflection = 0.4f;
    private float mPerspective = 0.4f;
    private float mDepth = 3;

    public DoorWayTransition() {
        super(DoorWayTransition.class.getSimpleName(), TRANS_DOOR_WAY);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new DoorWayTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((DoorWayTransDrawer) mDrawer).setReflection(mReflection);
        ((DoorWayTransDrawer) mDrawer).setPerspective(mPerspective);
        ((DoorWayTransDrawer) mDrawer).setDepth(mDepth);
    }
}
