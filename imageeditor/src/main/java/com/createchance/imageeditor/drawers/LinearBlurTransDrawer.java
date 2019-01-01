package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.LinearBlurTransShader;

/**
 * Linear blur transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class LinearBlurTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new LinearBlurTransShader();
    }

    public void setIntensity(float intensity) {
        GLES20.glUseProgram(mProgramId);
        ((LinearBlurTransShader) mTransitionShader).setUIntensity(intensity);
    }

    public void setPasses(int passes) {
        GLES20.glUseProgram(mProgramId);
        ((LinearBlurTransShader) mTransitionShader).setUPasses(passes);
    }

}
