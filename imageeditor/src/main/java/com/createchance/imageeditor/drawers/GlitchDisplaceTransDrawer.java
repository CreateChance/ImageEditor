package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.GlitchDisplaceTransShader;

/**
 * Glitch displace transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class GlitchDisplaceTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new GlitchDisplaceTransShader();
    }
}
