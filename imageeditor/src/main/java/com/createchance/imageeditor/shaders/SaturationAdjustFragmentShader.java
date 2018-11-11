package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Saturation adjust fragment shader.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class SaturationAdjustFragmentShader extends AbstractShader {

    private static final String TAG = "SaturationAdjustFragmen";

    private final String FRAGMENT_SHADER = "SaturationAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_SATURATION = "u_Saturation";

    private int mUInputTexture, mUSaturation;

    public SaturationAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUSaturation = GLES20.glGetUniformLocation(programId, U_SATURATION);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUSaturate(float saturate) {
        GLES20.glUniform1f(mUSaturation, saturate);
    }
}
