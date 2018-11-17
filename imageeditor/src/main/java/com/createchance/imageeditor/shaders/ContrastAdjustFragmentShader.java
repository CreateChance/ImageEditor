package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Contrast adjust fragment shader.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class ContrastAdjustFragmentShader extends AbstractShader {
    private static final String TAG = "ContrastAdjustFragmentS";

    private final String FRAGMENT_SHADER = "ContrastAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_CONTRAST = "u_Contrast";

    private int mUInputTexture, mUContrast;

    public ContrastAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUContrast = GLES20.glGetUniformLocation(programId, U_CONTRAST);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUContrast(float contrast) {
        GLES20.glUniform1f(mUContrast, contrast);
    }
}
