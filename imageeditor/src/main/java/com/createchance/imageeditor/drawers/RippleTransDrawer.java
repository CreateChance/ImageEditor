package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.RippleTransShader;

/**
 * Ripple transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RippleTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new RippleTransShader();
    }

    public void setAmplitude(float amplitude) {
        GLES20.glUseProgram(mProgramId);
        ((RippleTransShader) mTransitionShader).setUAmplitude(amplitude);
    }

    public void setSpeed(float speed) {
        GLES20.glUseProgram(mProgramId);
        ((RippleTransShader) mTransitionShader).setUSpeed(speed);
    }
}
