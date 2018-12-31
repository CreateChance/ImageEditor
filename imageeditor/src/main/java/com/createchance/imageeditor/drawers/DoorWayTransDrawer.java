package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.DoorWayTransShader;

/**
 * Door way transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DoorWayTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new DoorWayTransShader();
    }

    public void setReflection(float reflection) {
        GLES20.glUseProgram(mProgramId);
        ((DoorWayTransShader) mTransitionShader).setUReflection(reflection);
    }

    public void setPerspective(float perspective) {
        GLES20.glUseProgram(mProgramId);
        ((DoorWayTransShader) mTransitionShader).setUPerspective(perspective);
    }

    public void setDepth(float depth) {
        GLES20.glUseProgram(mProgramId);
        ((DoorWayTransShader) mTransitionShader).setUDepth(depth);
    }
}
