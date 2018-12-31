package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.ButterflyWaveScrawlerTransShader;

/**
 * Butterfly Wave Scrawler transition drawer
 *
 * @author createchance
 * @date 2018/12/31
 */
public class ButterflyWaveScrawlerTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new ButterflyWaveScrawlerTransShader();
    }

    public void setAmplitude(float amplitude) {
        GLES20.glUseProgram(mProgramId);
        ((ButterflyWaveScrawlerTransShader) mTransitionShader).setUAmplitude(amplitude);
    }

    public void setWaves(float waves) {
        GLES20.glUseProgram(mProgramId);
        ((ButterflyWaveScrawlerTransShader) mTransitionShader).setUWaves(waves);
    }

    public void setColorSeparation(float colorSeparation) {
        GLES20.glUseProgram(mProgramId);
        ((ButterflyWaveScrawlerTransShader) mTransitionShader).setUColorSeparation(colorSeparation);
    }
}
