package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/12/22
 */
public class WindowSliceTransShader extends TransitionMainFragmentShader {

    private static final String TAG = "WindowSliceTransShader";

    private final String TRANS_SHADER = "windowslice.glsl";

    private final String U_COUNT = "count";
    private final String U_SMOOTHNESS = "smoothness";

    private int mUCount, mUSmoothness;

    public WindowSliceTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        mUCount = GLES20.glGetUniformLocation(programId, U_COUNT);
        mUSmoothness = GLES20.glGetUniformLocation(programId, U_SMOOTHNESS);
        loadLocation(programId);
    }

    public void setUCount(float count) {
        GLES20.glUniform1f(mUCount, count);
    }

    public void setUSmoothness(float smoothness) {
        GLES20.glUniform1f(mUSmoothness, smoothness);
    }
}
