package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.SwapTransDrawer;

/**
 * Swap transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SwapTransition extends AbstractTransition {

    private static final String TAG = "SwapTransition";

    private float mReflection = 0.4f;
    private float mPerspective = 0.2f;
    private float mDepth = 3.0f;

    public SwapTransition() {
        super(SwapTransition.class.getSimpleName(), TRANS_SWAP);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new SwapTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((SwapTransDrawer) mDrawer).setReflection(mReflection);
        ((SwapTransDrawer) mDrawer).setPerspective(mPerspective);
        ((SwapTransDrawer) mDrawer).setDepth(mDepth);
    }
}
