package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.GridFlipTransShader;

/**
 * Grid flip transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class GridFlipTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new GridFlipTransShader();
    }

    public void setSize(int width, int height) {
        GLES20.glUseProgram(mProgramId);
        ((GridFlipTransShader) mTransitionShader).setUSize(width, height);
    }

    public void setPause(float pause) {
        GLES20.glUseProgram(mProgramId);
        ((GridFlipTransShader) mTransitionShader).setUPause(pause);
    }

    public void setDividerWidth(float dividerWidth) {
        GLES20.glUseProgram(mProgramId);
        ((GridFlipTransShader) mTransitionShader).setUDividerWidth(dividerWidth);
    }

    public void setBackColor(float red, float green, float blue, float alpha) {
        GLES20.glUseProgram(mProgramId);
        ((GridFlipTransShader) mTransitionShader).setUBackColor(red, green, blue, alpha);
    }

    public void setRandomness(float randomness) {
        GLES20.glUseProgram(mProgramId);
        ((GridFlipTransShader) mTransitionShader).setURandomness(randomness);
    }
}
