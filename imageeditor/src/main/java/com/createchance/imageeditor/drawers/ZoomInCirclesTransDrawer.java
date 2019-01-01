package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.ZoomInCirclesTransShader;

/**
 * Zoom int circles transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class ZoomInCirclesTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new ZoomInCirclesTransShader();
    }
}
