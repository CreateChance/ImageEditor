package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Color balance adjustment fragment shader.
 *
 * @author createchance
 * @date 2018/12/21
 */
public class ColorBalanceFragmentShader extends AbstractShader {

    private static final String TAG = "ColorBalanceFragmentSha";

    private final String FRAGMENT_SHADER = "ColorBalanceFragmentShader.glsl";
    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_RED_SHIFT = "u_RedShift";
    private final String U_GREEN_SHIFT = "u_GreenShift";
    private final String U_BLUE_SHIFT = "u_BlueShift";

    private int mUInputTexture, mURedShift, mUGreenShift, mUBlueShift;

    public ColorBalanceFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mURedShift = GLES20.glGetUniformLocation(programId, U_RED_SHIFT);
        mUGreenShift = GLES20.glGetUniformLocation(programId, U_GREEN_SHIFT);
        mUBlueShift = GLES20.glGetUniformLocation(programId, U_BLUE_SHIFT);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setURedShift(float redShift) {
        GLES20.glUniform1f(mURedShift, redShift);
    }

    public void setUGreenShift(float greenShift) {
        GLES20.glUniform1f(mUGreenShift, greenShift);
    }

    public void setUBlueShift(float blueShift) {
        GLES20.glUniform1f(mUBlueShift, blueShift);
    }
}
