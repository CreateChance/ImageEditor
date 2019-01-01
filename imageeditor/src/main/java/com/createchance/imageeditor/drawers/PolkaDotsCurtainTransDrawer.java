package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.PolkaDotsCurtainTransShader;

/**
 * Polka dots curtain transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PolkaDotsCurtainTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new PolkaDotsCurtainTransShader();
    }

    public void setDots(float dots) {
        GLES20.glUseProgram(mProgramId);
        ((PolkaDotsCurtainTransShader) mTransitionShader).setUDots(dots);
    }

    public void setCenter(float centerX, float centerY) {
        GLES20.glUseProgram(mProgramId);
        ((PolkaDotsCurtainTransShader) mTransitionShader).setUCenter(centerX, centerY);
    }
}
