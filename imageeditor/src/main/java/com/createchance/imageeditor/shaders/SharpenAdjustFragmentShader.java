package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Sharpness adjust fragment shader.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class SharpenAdjustFragmentShader extends AbstractShader {
    private static final String TAG = "SharpenAdjustFragmentSh";

    private final String FRAGMENT_SHADER = "SharpenAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";

    private int mUInputTexture;

    public SharpenAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetAttribLocation(programId, U_INPUT_TEXTURE);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }
}
