package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Exposure adjust fragment shader.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class ExposureAdjustFragmentShader extends AbstractShader {

    private static final String TAG = "ExposureAdjustFragmentS";

    private final String FRAGMENT_SHADER = "ExposureAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_EXPOSURE = "u_Exposure";

    private int mUInputTexture, mUExposure;

    public ExposureAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUExposure = GLES20.glGetUniformLocation(programId, U_EXPOSURE);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUExposure(float exposure) {
        GLES20.glUniform1f(mUExposure, exposure);
    }
}
