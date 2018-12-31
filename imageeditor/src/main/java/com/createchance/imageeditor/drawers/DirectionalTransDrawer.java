package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.DirectionalTransShader;

/**
 * Directional transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DirectionalTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new DirectionalTransShader();
    }

    public void setDirectional(float directionalX, float directionalY) {
        GLES20.glUseProgram(mProgramId);
        ((DirectionalTransShader) mTransitionShader).setUDirectional(directionalX, directionalY);
    }
}
