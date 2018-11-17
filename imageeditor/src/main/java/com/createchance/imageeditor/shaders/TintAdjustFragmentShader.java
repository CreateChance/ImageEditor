package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Tint adjust fragment shader.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class TintAdjustFragmentShader extends AbstractShader {

    private static final String TAG = "TintAdjustFragmentShade";

    private final String FRAGMENT_SHADER = "TintAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_TINT = "u_Tint";

    private int mUInputTexture, mUTint;

    public TintAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUTint = GLES20.glGetUniformLocation(programId, U_TINT);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUTint(float tint) {
        GLES20.glUniform1f(mUTint, tint);
    }
}
