package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.FadeColorTransShader;

/**
 * Fade color transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class FadeColorTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new FadeColorTransShader();
    }

    public void setColor(float red, float green, float blue) {
        GLES20.glUseProgram(mProgramId);
        ((FadeColorTransShader) mTransitionShader).setUColor(red, green, blue);
    }

    public void setColorPhase(float colorPhase) {
        GLES20.glUseProgram(mProgramId);
        ((FadeColorTransShader) mTransitionShader).setUColorPhase(colorPhase);
    }
}
