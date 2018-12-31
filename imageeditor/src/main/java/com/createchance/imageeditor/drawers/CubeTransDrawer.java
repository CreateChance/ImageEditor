package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.CubeTransShader;

/**
 * Cube transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CubeTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new CubeTransShader();
    }

    public void setPerspective(float perspective) {
        GLES20.glUseProgram(mProgramId);
        ((CubeTransShader) mTransitionShader).setUPerspective(perspective);
    }

    public void setUnzoom(float unzoom) {
        GLES20.glUseProgram(mProgramId);
        ((CubeTransShader) mTransitionShader).setUUnzoom(unzoom);
    }

    public void setReflection(float reflection) {
        GLES20.glUseProgram(mProgramId);
        ((CubeTransShader) mTransitionShader).setUReflection(reflection);
    }

    public void setFloating(float floating) {
        GLES20.glUseProgram(mProgramId);
        ((CubeTransShader) mTransitionShader).setUFloating(floating);
    }
}
