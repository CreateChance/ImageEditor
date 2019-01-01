package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.SquaresWireTransShader;

/**
 * Squares wire transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SquaresWireTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new SquaresWireTransShader();
    }

    public void setSquare(int width, int height) {
        GLES20.glUseProgram(mProgramId);
        ((SquaresWireTransShader) mTransitionShader).setUSquares(width, height);
    }

    public void setDirection(float x, float y) {
        GLES20.glUseProgram(mProgramId);
        ((SquaresWireTransShader) mTransitionShader).setUDirection(x, y);
    }

    public void setSmoothness(float smoothness) {
        GLES20.glUseProgram(mProgramId);
        ((SquaresWireTransShader) mTransitionShader).setUSmoothness(smoothness);
    }
}
