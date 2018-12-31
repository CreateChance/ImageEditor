package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.DirectionalWipeTransShader;

/**
 * Directional wipe transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DirectionalWipeTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new DirectionalWipeTransShader();
    }

    public void setDirection(float directionX, float directionY) {
        GLES20.glUseProgram(mProgramId);
        ((DirectionalWipeTransShader) mTransitionShader).setUDirectional(directionX, directionY);
    }

    public void setSmoothness(float smoothness) {
        GLES20.glUseProgram(mProgramId);
        ((DirectionalWipeTransShader) mTransitionShader).setUSmoothness(smoothness);
    }
}
