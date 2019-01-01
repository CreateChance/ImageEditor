package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.GlitchMemoriesTransShader;

/**
 * Glitch Memories transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class GlitchMemoriesTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new GlitchMemoriesTransShader();
    }
}
