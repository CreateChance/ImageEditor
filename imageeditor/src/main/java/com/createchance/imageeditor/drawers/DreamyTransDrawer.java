package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.DreamyTransShader;

/**
 * Dreamy transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DreamyTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new DreamyTransShader();
    }
}
