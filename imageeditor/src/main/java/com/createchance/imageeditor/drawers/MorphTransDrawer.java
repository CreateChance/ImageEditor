package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.MorphTransShader;

/**
 * Morph transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class MorphTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new MorphTransShader();
    }

    public void setStrength(float strength) {
        GLES20.glUseProgram(mProgramId);
        ((MorphTransShader) mTransitionShader).setUStrength(strength);
    }
}
