package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.WaterDropTransShader;

/**
 * Water drop transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WaterDropTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new WaterDropTransShader();
    }

    public void setAmplitude(float amplitude) {
        GLES20.glUseProgram(mProgramId);
        ((WaterDropTransShader) mTransitionShader).setUAmplitude(amplitude);
    }

    public void setSpeed(float speed) {
        GLES20.glUseProgram(mProgramId);
        ((WaterDropTransShader) mTransitionShader).setUSpeed(speed);
    }
}
