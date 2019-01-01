package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.PerlinTransShader;

/**
 * Perlin transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PerlinTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new PerlinTransShader();
    }

    public void setScale(float scale) {
        GLES20.glUseProgram(mProgramId);
        ((PerlinTransShader) mTransitionShader).setUScale(scale);
    }

    public void setSmoothness(float smoothness) {
        GLES20.glUseProgram(mProgramId);
        ((PerlinTransShader) mTransitionShader).setUSmoothness(smoothness);
    }

    public void setSeed(float seed) {
        GLES20.glUseProgram(mProgramId);
        ((PerlinTransShader) mTransitionShader).setUSeed(seed);
    }
}
