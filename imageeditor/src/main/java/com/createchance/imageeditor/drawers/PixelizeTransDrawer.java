package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.PixelizeTransShader;

/**
 * Pixelize transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PixelizeTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new PixelizeTransShader();
    }

    public void setSquaresMin(int width, int height) {
        GLES20.glUseProgram(mProgramId);
        ((PixelizeTransShader) mTransitionShader).setUSquaresMin(width, height);
    }

    public void setStep(int step) {
        GLES20.glUseProgram(mProgramId);
        ((PixelizeTransShader) mTransitionShader).setUStep(step);
    }
}
