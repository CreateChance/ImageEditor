package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.DirectionalWarpTransShader;

/**
 * Directional warp transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DirectionalWarpTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new DirectionalWarpTransShader();
    }

    public void setDirectional(float directionalX, float directionalY) {
        ((DirectionalWarpTransShader) mTransitionShader).setUDirectional(directionalX, directionalY);
    }
}
