package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.SwirlTransShader;

/**
 * Swirl transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SwirlTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new SwirlTransShader();
    }
}
