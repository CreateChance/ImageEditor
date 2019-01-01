package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.RadialTransShader;

/**
 * Radial transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RadialTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new RadialTransShader();
    }

    public void setSmoothness(float smoothness) {
        GLES20.glUseProgram(mProgramId);
        ((RadialTransShader) mTransitionShader).setUSmoothness(smoothness);
    }
}
