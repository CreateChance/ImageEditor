package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Temperature adjust fragment shader.
 *
 * @author gaochao02
 * @date 2018/11/17
 */
public class TempAdjustFragmentShader extends AbstractShader {

    private static final String TAG = "TempAdjustFragmentShade";

    private final String FRAGMENT_SHADER = "TempAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_TEMPERATURE = "u_Temperature";

    private int mUInputTexture, mUTemperature;

    public TempAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUTemperature = GLES20.glGetUniformLocation(programId, U_TEMPERATURE);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUTemperature(float temperature) {
        GLES20.glUniform1f(mUTemperature, temperature);
    }
}
