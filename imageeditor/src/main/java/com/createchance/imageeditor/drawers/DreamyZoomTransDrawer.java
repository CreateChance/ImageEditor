package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.DreamyZoomTransShader;

/**
 * Dreamy zoom transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DreamyZoomTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new DreamyZoomTransShader();
    }

    public void setRotation(float rotation) {
        GLES20.glUseProgram(mProgramId);
        ((DreamyZoomTransShader) mTransitionShader).setURotation(rotation);
    }

    public void setScale(float scale) {
        GLES20.glUseProgram(mProgramId);
        ((DreamyZoomTransShader) mTransitionShader).setUScale(scale);
    }
}
