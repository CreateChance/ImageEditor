package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.CircleTransShader;

/**
 * Circle transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CircleTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new CircleTransShader();
    }

    public void setCenter(float centerX, float centerY) {
        GLES20.glUseProgram(mProgramId);
        ((CircleTransShader) mTransitionShader).setUCenter(centerX, centerY);
    }

    public void setBackColor(float red, float green, float blue) {
        GLES20.glUseProgram(mProgramId);
        ((CircleTransShader) mTransitionShader).setUBackColor(red, green, blue);
    }
}
