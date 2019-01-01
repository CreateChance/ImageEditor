package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.PinWheelTransShader;

/**
 * Pin wheel transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PinWheelTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new PinWheelTransShader();
    }

    public void setSpeed(float speed) {
        GLES20.glUseProgram(mProgramId);
        ((PinWheelTransShader) mTransitionShader).setUSpeed(speed);
    }
}
