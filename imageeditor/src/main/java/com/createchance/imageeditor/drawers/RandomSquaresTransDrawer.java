package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.RandomSquaresTransShader;

/**
 * Random square transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RandomSquaresTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new RandomSquaresTransShader();
    }

    public void setSmoothness(float smoothness) {
        GLES20.glUseProgram(mProgramId);
        ((RandomSquaresTransShader) mTransitionShader).setUSmoothness(smoothness);
    }

    public void setSize(int width, int height) {
        GLES20.glUseProgram(mProgramId);
        ((RandomSquaresTransShader) mTransitionShader).setUSize(width, height);
    }
}
