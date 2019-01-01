package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.WindowBlindsTransShader;

/**
 * Window blinds transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WindowBlindsTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new WindowBlindsTransShader();
    }
}
