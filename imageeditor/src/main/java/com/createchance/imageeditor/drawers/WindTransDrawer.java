package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.WindTransShader;

/**
 * Wind transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WindTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new WindTransShader();
    }

    public void setSize(float size) {
        GLES20.glUseProgram(mProgramId);
        ((WindTransShader) mTransitionShader).setUSize(size);
    }
}
