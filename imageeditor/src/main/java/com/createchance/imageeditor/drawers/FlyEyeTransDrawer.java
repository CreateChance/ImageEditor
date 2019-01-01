package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.FlyEyeTransShader;

/**
 * Fly eye transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class FlyEyeTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new FlyEyeTransShader();
    }

    public void setSize(float size) {
        GLES20.glUseProgram(mProgramId);
        ((FlyEyeTransShader) mTransitionShader).setUSize(size);
    }

    public void setZoom(float zoom) {
        GLES20.glUseProgram(mProgramId);
        ((FlyEyeTransShader) mTransitionShader).setUZoom(zoom);
    }

    public void setColorSeparation(float colorSeparation) {
        GLES20.glUseProgram(mProgramId);
        ((FlyEyeTransShader) mTransitionShader).setUColorSeparation(colorSeparation);
    }
}
