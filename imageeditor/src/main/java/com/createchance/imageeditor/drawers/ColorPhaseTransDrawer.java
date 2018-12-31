package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.ColorPhaseTransShader;

/**
 * Color phase transition drawer
 *
 * @author createchance
 * @date 2018/12/31
 */
public class ColorPhaseTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new ColorPhaseTransShader();
    }

    public void setFromStep(float red, float green, float blue, float alpha) {
        GLES20.glUseProgram(mProgramId);
        ((ColorPhaseTransShader) mTransitionShader).setUFromStep(red, green, blue, alpha);
    }

    public void setToStep(float red, float green, float blue, float alpha) {
        GLES20.glUseProgram(mProgramId);
        ((ColorPhaseTransShader) mTransitionShader).setUToStep(red, green, blue, alpha);
    }
}
