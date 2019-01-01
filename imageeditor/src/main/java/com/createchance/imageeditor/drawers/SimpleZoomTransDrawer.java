package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.SimpleZoomTransShader;

/**
 * Simple zoom transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SimpleZoomTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new SimpleZoomTransShader();
    }

    public void setZoomQuickness(float zoomQuickness) {
        GLES20.glUseProgram(mProgramId);
        ((SimpleZoomTransShader) mTransitionShader).setUZoomQuickness(zoomQuickness);
    }
}
