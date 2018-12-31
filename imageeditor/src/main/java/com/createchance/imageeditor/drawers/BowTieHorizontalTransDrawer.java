package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.BowTieHorizontalTransShader;

/**
 * Bow tie horizontal transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BowTieHorizontalTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new BowTieHorizontalTransShader();
    }
}
