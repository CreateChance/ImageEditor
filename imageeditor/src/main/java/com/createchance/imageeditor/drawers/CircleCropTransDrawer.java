package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.CircleCropTransShader;

/**
 * Circle crop transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CircleCropTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new CircleCropTransShader();
    }

    public void setBackColor(float red, float green, float blue, float alpha) {
        GLES20.glUseProgram(mProgramId);
        ((CircleCropTransShader) mTransitionShader).setUBackColor(red, green, blue, alpha);
    }
}
