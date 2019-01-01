package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.SqueezeTransDrawer;

/**
 * Squeeze transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SqueezeTransition extends AbstractTransition {

    private static final String TAG = "SqueezeTransition";

    private float mColorSeparation = 0.04f;

    public SqueezeTransition() {
        super(SqueezeTransition.class.getSimpleName(), TRANS_SQUEEZE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new SqueezeTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((SqueezeTransDrawer) mDrawer).setColorSeparation(mColorSeparation);
    }
}
