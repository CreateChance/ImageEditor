package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.BowTieVerticalTransShader;

/**
 * Bow tie vertical transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BowTieVerticalTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new BowTieVerticalTransShader();
    }
}
