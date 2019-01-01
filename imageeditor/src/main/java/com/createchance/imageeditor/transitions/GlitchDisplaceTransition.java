package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.GlitchDisplaceTransDrawer;

/**
 * Glitch displace transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class GlitchDisplaceTransition extends AbstractTransition {

    private static final String TAG = "GlitchDisplaceTransitio";

    public GlitchDisplaceTransition() {
        super(GlitchDisplaceTransition.class.getSimpleName(), TRANS_GLITCH_DISPLACE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new GlitchDisplaceTransDrawer();
    }
}
