package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.HeartTransShader;

/**
 * Heart transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class HeartTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new HeartTransShader();
    }
}
