package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.SqueezeTransShader;

/**
 * Squeeze transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SqueezeTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new SqueezeTransShader();
    }

    public void setColorSeparation(float colorSeparation) {
        GLES20.glUseProgram(mProgramId);
        ((SqueezeTransShader) mTransitionShader).setUColorSeparation(colorSeparation);
    }
}
