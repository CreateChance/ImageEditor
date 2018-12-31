package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.CrossWarpTransShader;

/**
 * Cross warp transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrossWarpTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new CrossWarpTransShader();
    }
}
