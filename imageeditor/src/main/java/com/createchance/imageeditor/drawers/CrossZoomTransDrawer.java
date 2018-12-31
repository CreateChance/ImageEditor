package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.CrossZoomTransShader;

/**
 * Cross zoom transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrossZoomTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new CrossZoomTransShader();
    }

    public void setStrength(float strength) {
        GLES20.glUseProgram(mProgramId);
        ((CrossZoomTransShader) mTransitionShader).setUStrength(strength);
    }
}
