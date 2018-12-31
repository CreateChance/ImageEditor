package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.ColorDistanceTransShader;

/**
 * Color distance transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class ColorDistanceTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new ColorDistanceTransShader();
    }

    public void setPower(float power) {
        GLES20.glUseProgram(mProgramId);
        ((ColorDistanceTransShader) mTransitionShader).setUPower(power);
    }
}
