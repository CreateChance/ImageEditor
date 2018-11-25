package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * RGB channel adjust fragment shader.
 *
 * @author createchance
 * @date 2018/11/25
 */
public class RGBAdjustFragmentShader extends AbstractShader {

    private static final String TAG = "RGBAdjustFragmentShader";

    private final String FRAGMENT_SHADER = "RGBAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_RED = "u_Red";
    private final String U_GREEN = "u_Green";
    private final String U_BLUE = "u_Blue";

    private int mUInputTexture, mURed, mUGreen, mUBlue;

    public RGBAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mURed = GLES20.glGetUniformLocation(programId, U_RED);
        mUGreen = GLES20.glGetUniformLocation(programId, U_GREEN);
        mUBlue = GLES20.glGetUniformLocation(programId, U_BLUE);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setURed(float red) {
        GLES20.glUniform1f(mURed, red);
    }

    public void setUGreen(float green) {
        GLES20.glUniform1f(mUGreen, green);
    }

    public void setUBlue(float blue) {
        GLES20.glUniform1f(mUBlue, blue);
    }
}
