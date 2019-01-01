package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.WipeUpTransShader;

/**
 * Wipe up transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WipeUpTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new WipeUpTransShader();
    }
}
