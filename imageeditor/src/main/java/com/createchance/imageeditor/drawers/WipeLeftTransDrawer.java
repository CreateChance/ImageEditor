package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.WipeLeftTransShader;

/**
 * Wipe left transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WipeLeftTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new WipeLeftTransShader();
    }
}
