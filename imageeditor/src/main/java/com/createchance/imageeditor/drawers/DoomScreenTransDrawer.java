package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.DoomScreenTransShader;

/**
 * Doom screen transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DoomScreenTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new DoomScreenTransShader();
    }

    public void setBars(int bars) {
        GLES20.glUseProgram(mProgramId);
        ((DoomScreenTransShader) mTransitionShader).setUBars(bars);
    }

    public void setAmplitude(float amplitude) {
        GLES20.glUseProgram(mProgramId);
        ((DoomScreenTransShader) mTransitionShader).setUAmplitude(amplitude);
    }

    public void setNoise(float noise) {
        GLES20.glUseProgram(mProgramId);
        ((DoomScreenTransShader) mTransitionShader).setUNoise(noise);
    }

    public void setFrequency(float frequency) {
        GLES20.glUseProgram(mProgramId);
        ((DoomScreenTransShader) mTransitionShader).setUFrequency(frequency);
    }

    public void setDripScale(float dripScale) {
        GLES20.glUseProgram(mProgramId);
        ((DoomScreenTransShader) mTransitionShader).setUDripScale(dripScale);
    }
}
