package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Gamma adjust fragment shader.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class GammaAdjustFragmentShader extends AbstractShader {

    private static final String TAG = "GammaAdjustFragmentShader";

    private final String FRAGMENT_SHADER = "GammaAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_GAMMA = "u_Gamma";

    private int mUInputTexture, mUGamma;

    public GammaAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUGamma = GLES20.glGetUniformLocation(programId, U_GAMMA);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUGamma(float gamma) {
        GLES20.glUniform1f(mUGamma, gamma);
    }
}
