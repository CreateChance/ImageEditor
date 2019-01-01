package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.WipeDownTransShader;

/**
 * Wipe down transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WipeDownTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new WipeDownTransShader();
    }
}
