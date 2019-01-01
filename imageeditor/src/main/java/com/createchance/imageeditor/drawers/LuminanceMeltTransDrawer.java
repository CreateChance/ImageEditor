package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.LuminanceMeltTransShader;

/**
 * Luminance melt transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class LuminanceMeltTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new LuminanceMeltTransShader();
    }

    public void setDirection(boolean down) {
        GLES20.glUseProgram(mProgramId);
        ((LuminanceMeltTransShader) mTransitionShader).setUDirection(down);
    }

    public void setThreshold(float threshold) {
        GLES20.glUseProgram(mProgramId);
        ((LuminanceMeltTransShader) mTransitionShader).setUThreshold(threshold);
    }

    public void setAbove(boolean above) {
        GLES20.glUseProgram(mProgramId);
        ((LuminanceMeltTransShader) mTransitionShader).setUAbove(above);
    }
}
