package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.CannabisLeafTransShader;

/**
 * Cannabis leaf transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CannabisLeafTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new CannabisLeafTransShader();
    }
}
