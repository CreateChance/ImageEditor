package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Shader adjust fragment shader class.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class ShadowAdjustFragmentShader extends AbstractShader {

    private static final String TAG = "ShadowAdjustFragmentSha";

    private final String FRAGMENT_SHADER = "ShadowAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_SHADOW = "u_Shadow";

    private int mUInputTexture, mUShadow;

    public ShadowAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUShadow = GLES20.glGetUniformLocation(programId, U_SHADOW);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUShadow(float shadow) {
        GLES20.glUniform1f(mUShadow, shadow);
    }
}
