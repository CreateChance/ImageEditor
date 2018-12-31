package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.CrossHatchTransShader;

/**
 * Cross hatch transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrossHatchTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new CrossHatchTransShader();
    }

    public void setCenter(float centerX, float centerY) {
        GLES20.glUseProgram(mProgramId);
        ((CrossHatchTransShader) mTransitionShader).setUCenter(centerX, centerY);
    }

    public void setThreshold(float threshold) {
        GLES20.glUseProgram(mProgramId);
        ((CrossHatchTransShader) mTransitionShader).setUThreshold(threshold);
    }

    public void setFadeEdge(float fadeEdge) {
        GLES20.glUseProgram(mProgramId);
        ((CrossHatchTransShader) mTransitionShader).setUFadeEdge(fadeEdge);
    }
}
