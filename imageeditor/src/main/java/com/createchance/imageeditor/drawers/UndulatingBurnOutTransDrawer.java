package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.UndulatingBurnOutTransShader;

/**
 * Undulating burnout transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class UndulatingBurnOutTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new UndulatingBurnOutTransShader();
    }

    public void setSmoothness(float smoothness) {
        GLES20.glUseProgram(mProgramId);
        ((UndulatingBurnOutTransShader) mTransitionShader).setUSmoothness(smoothness);
    }

    public void setCenter(float centerX, float centerY) {
        GLES20.glUseProgram(mProgramId);
        ((UndulatingBurnOutTransShader) mTransitionShader).setUCenter(centerX, centerY);
    }

    public void setColor(float red, float green, float blue) {
        GLES20.glUseProgram(mProgramId);
        ((UndulatingBurnOutTransShader) mTransitionShader).setUColor(red, green, blue);
    }
}
