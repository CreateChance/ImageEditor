package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.MultiplyBlendTransDrawer;

/**
 * Multiply blend transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class MultiplyBlendTransition extends AbstractTransition {

    private static final String TAG = "MultiplyBlendTransition";

    public MultiplyBlendTransition() {
        super(MultiplyBlendTransition.class.getSimpleName(), TRANS_MULTIPLY_BLEND);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new MultiplyBlendTransDrawer();
    }
}
