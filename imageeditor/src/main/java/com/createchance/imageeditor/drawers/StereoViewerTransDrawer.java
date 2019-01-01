package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.StereoViewerTransShader;

/**
 * Stereo viewer transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class StereoViewerTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new StereoViewerTransShader();
    }

    public void setZoom(float zoom) {
        GLES20.glUseProgram(mProgramId);
        ((StereoViewerTransShader) mTransitionShader).setUZoom(zoom);
    }

    public void setCornerRadius(float cornerRadius) {
        GLES20.glUseProgram(mProgramId);
        ((StereoViewerTransShader) mTransitionShader).setUCornerRadius(cornerRadius);
    }
}
