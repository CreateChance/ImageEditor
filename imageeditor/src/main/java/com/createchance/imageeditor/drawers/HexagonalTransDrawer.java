package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.HexagonalizeTransShader;

/**
 * Hexagonal transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class HexagonalTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new HexagonalizeTransShader();
    }

    public void setStep(int step) {
        GLES20.glUseProgram(mProgramId);
        ((HexagonalizeTransShader) mTransitionShader).setUStep(step);
    }

    public void setHorizontalHexagons(float horizontalHexagons) {
        GLES20.glUseProgram(mProgramId);
        ((HexagonalizeTransShader) mTransitionShader).setUHorizontalHexagons(horizontalHexagons);
    }
}
