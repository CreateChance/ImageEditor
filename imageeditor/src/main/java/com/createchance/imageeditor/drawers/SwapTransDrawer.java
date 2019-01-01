package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.SwapTransShader;

/**
 * Swap transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SwapTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new SwapTransShader();
    }

    public void setReflection(float reflection) {
        GLES20.glUseProgram(mProgramId);
        ((SwapTransShader) mTransitionShader).setUReflection(reflection);
    }

    public void setPerspective(float perspective) {
        GLES20.glUseProgram(mProgramId);
        ((SwapTransShader) mTransitionShader).setUPerspective(perspective);
    }

    public void setDepth(float depth) {
        GLES20.glUseProgram(mProgramId);
        ((SwapTransShader) mTransitionShader).setUDepth(depth);
    }
}
