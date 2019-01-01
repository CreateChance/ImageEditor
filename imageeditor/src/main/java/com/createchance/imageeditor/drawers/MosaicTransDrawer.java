package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.MosaicTransShader;

/**
 * Mosaic transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class MosaicTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new MosaicTransShader();
    }

    public void setEndX(int endX) {
        GLES20.glUseProgram(mProgramId);
        ((MosaicTransShader) mTransitionShader).setUEndX(endX);
    }

    public void setEndY(int endY) {
        GLES20.glUseProgram(mProgramId);
        ((MosaicTransShader) mTransitionShader).setUEndY(endY);
    }
}
