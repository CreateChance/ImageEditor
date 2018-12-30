package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.BounceTransShader;

/**
 * Bounce transition drawer.
 *
 * @author createchance
 * @date 2018/12/30
 */
public class BounceTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new BounceTransShader();
    }

    public void setShadowColor(float red, float green, float blue, float alpha) {
        GLES20.glUseProgram(mProgramId);
        ((BounceTransShader) mTransitionShader).setUShadowColor(red, green, blue, alpha);
    }

    public void setShadowHeight(float shadowHeight) {
        GLES20.glUseProgram(mProgramId);
        ((BounceTransShader) mTransitionShader).setUShadowHeight(shadowHeight);
    }

    public void setBounces(float bounces) {
        GLES20.glUseProgram(mProgramId);
        ((BounceTransShader) mTransitionShader).setUBounces(bounces);
    }
}
