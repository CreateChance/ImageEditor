package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Transition main fragment shader.
 *
 * @author createchance
 * @date 2018/12/23
 */
class TransitionMainFragmentShader extends AbstractShader {

    private static final String TAG = "TransitionMainFragmentS";

    protected final String BASE_SHADER = "TransitionMainFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_INPUT_TEXTURE2 = "u_InputTexture2";
    private final String U_PROGRESS = "progress";

    private int mUInputTexture, mUInputTexture2, mUProgress;

    TransitionMainFragmentShader() {

    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUInputTexture2 = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE2);
        mUProgress = GLES20.glGetUniformLocation(programId, U_PROGRESS);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUInputTexture2(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture2, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUProgress(float progress) {
        GLES20.glUniform1f(mUProgress, progress);
    }
}
