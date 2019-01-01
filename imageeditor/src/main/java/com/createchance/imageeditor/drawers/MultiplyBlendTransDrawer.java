package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.MultiplyBlendTransShader;

/**
 * Multiply blend transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class MultiplyBlendTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new MultiplyBlendTransShader();
    }
}
