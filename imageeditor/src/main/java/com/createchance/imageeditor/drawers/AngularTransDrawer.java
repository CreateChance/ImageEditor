package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.AngularTransShader;

/**
 * Angular trans drawer.
 *
 * @author createchance
 * @date 2018/12/30
 */
public class AngularTransDrawer extends AbstractTransDrawer {
    private static final String TAG = "AngularTransDrawer";

    @Override
    protected void getTransitionShader() {
        mTransitionShader = new AngularTransShader();
    }

    public void setStartAngular(float startAngular) {
        GLES20.glUseProgram(mProgramId);
        ((AngularTransShader) mTransitionShader).setUStartAngular(startAngular);
    }
}
