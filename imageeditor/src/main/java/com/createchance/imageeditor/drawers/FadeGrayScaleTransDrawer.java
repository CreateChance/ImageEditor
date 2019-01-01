package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.FadeGrayScaleTransShader;

/**
 * Fade gray scale transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class FadeGrayScaleTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new FadeGrayScaleTransShader();
    }

    public void setIntensity(float intensity) {
        GLES20.glUseProgram(mProgramId);
        ((FadeGrayScaleTransShader) mTransitionShader).setUIntensity(intensity);
    }
}
