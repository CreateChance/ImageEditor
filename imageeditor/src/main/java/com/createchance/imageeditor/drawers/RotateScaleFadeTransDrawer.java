package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.RotateScaleFadeTransShader;

/**
 * Rotate scale fade transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RotateScaleFadeTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new RotateScaleFadeTransShader();
    }

    public void setCenter(float centerX, float centerY) {
        GLES20.glUseProgram(mProgramId);
        ((RotateScaleFadeTransShader) mTransitionShader).setUCenter(centerX, centerY);
    }

    public void setRotations(float rotations) {
        GLES20.glUseProgram(mProgramId);
        ((RotateScaleFadeTransShader) mTransitionShader).setURotations(rotations);
    }

    public void setScale(float scale) {
        GLES20.glUseProgram(mProgramId);
        ((RotateScaleFadeTransShader) mTransitionShader).setUScale(scale);
    }

    public void setBackColor(float red, float green, float blue, float alpha) {
        GLES20.glUseProgram(mProgramId);
        ((RotateScaleFadeTransShader) mTransitionShader).setUBackColor(red, green, blue, alpha);
    }
}
