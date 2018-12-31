package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.BurnTransShader;

/**
 * Burn transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BurnTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new BurnTransShader();
    }

    public void setColor(float red, float green, float blue) {
        GLES20.glUseProgram(mProgramId);
        ((BurnTransShader) mTransitionShader).setUColor(red, green, blue);
    }
}
