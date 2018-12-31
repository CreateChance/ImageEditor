package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.FadeTransShader;

/**
 * Fade transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class FadeTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new FadeTransShader();
    }
}
