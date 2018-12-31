package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.WindowSliceTransDrawer;

/**
 * Window slice trans.
 *
 * @author createchance
 * @date 2018/12/23
 */
public class WindowSliceTransition extends AbstractTransition {

    private static final String TAG = "WindowSliceTransition";

    private float mCount = 10.0f;
    private float mSmoothness = 0.5f;

    public WindowSliceTransition() {
        super(WindowSliceTransition.class.getSimpleName(), TRANS_WINDOW_SLICE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new WindowSliceTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();
        ((WindowSliceTransDrawer) mDrawer).setCount(mCount);
        ((WindowSliceTransDrawer) mDrawer).setSmoothness(mSmoothness);
    }
}
