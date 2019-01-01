package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.PolarFunctionTransShader;

/**
 * Polar function transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PolarFunctionTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new PolarFunctionTransShader();
    }

    public void setSegments(int segments) {
        GLES20.glUseProgram(mProgramId);
        ((PolarFunctionTransShader) mTransitionShader).setUSegments(segments);
    }
}
