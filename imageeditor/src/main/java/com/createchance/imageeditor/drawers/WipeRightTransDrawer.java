package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.WipeRightTransShader;

/**
 * Wipe right transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WipeRightTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new WipeRightTransShader();
    }
}
