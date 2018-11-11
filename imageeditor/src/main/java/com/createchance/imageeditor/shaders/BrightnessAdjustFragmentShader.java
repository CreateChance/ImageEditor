package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Brightness adjust fragment shader.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class BrightnessAdjustFragmentShader extends AbstractShader {

    private static final String TAG = "BrightnessAdjustFragmen";

    private final String FRAGMENT_SHADER = "BrightnessAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_BRIGHTNESS = "u_Brightness";

    private int mUInputTexture, mUBrightness;

    public BrightnessAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUBrightness = GLES20.glGetUniformLocation(programId, U_BRIGHTNESS);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUBrightness(float brightness) {
        GLES20.glUniform1f(mUBrightness, brightness);
    }
}
