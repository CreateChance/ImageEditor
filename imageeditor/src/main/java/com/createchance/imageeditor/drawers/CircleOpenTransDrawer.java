package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.CircleOpenTransShader;

/**
 * Circle open transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CircleOpenTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new CircleOpenTransShader();
    }

    public void setSmoothness(float smoothness) {
        GLES20.glUseProgram(mProgramId);
        ((CircleOpenTransShader) mTransitionShader).setUSmoothness(smoothness);
    }

    public void setOpening(boolean opening) {
        GLES20.glUseProgram(mProgramId);
        ((CircleOpenTransShader) mTransitionShader).setUOpening(opening);
    }
}
