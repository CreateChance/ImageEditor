package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.GlitchMemoriesTransDrawer;

/**
 * Glitch Memories transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class GlitchMemoriesTransition extends AbstractTransition {

    private static final String TAG = "GlitchMemoriesTransitio";

    public GlitchMemoriesTransition() {
        super(GlitchMemoriesTransition.class.getSimpleName(), TRANS_GLITCH_MEMORIES);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new GlitchMemoriesTransDrawer();
    }
}
