package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.WindowSliceTransShader;

/**
 * Window slice transition drawer.
 *
 * @author createchance
 * @date 2018/12/23
 */
public class WindowSliceTransDrawer extends AbstractTransDrawer {

    private static final String TAG = "WindowSliceTransDrawer";

    @Override
    protected void getTransitionShader() {
        mTransitionShader = new WindowSliceTransShader();
    }

    public void setCount(float count) {
        GLES20.glUseProgram(mProgramId);
        ((WindowSliceTransShader) mTransitionShader).setUCount(count);
    }

    public void setSmoothness(float smoothness) {
        GLES20.glUseProgram(mProgramId);
        ((WindowSliceTransShader) mTransitionShader).setUSmoothness(smoothness);
    }
}
