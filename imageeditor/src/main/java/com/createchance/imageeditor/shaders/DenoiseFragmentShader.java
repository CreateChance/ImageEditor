package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;
import android.util.Log;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/17
 */
public class DenoiseFragmentShader extends AbstractShader {
    private static final String TAG = "DenoiseFragmentShader";

    private final String FRAGMENT_SHADER = "DenoiseFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_RESOLUTION = "u_Resolution";
    private final String U_EXPONENT = "u_Exponent";
    private final String U_STRENGTH = "u_Strength";

    private int mUInputTexture, mUResolution, mUExponent, mUStrength;

    public DenoiseFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUResolution = GLES20.glGetUniformLocation(programId, U_RESOLUTION);
//        mUExponent = GLES20.glGetUniformLocation(programId, U_EXPONENT);
//        mUStrength = GLES20.glGetUniformLocation(programId, U_STRENGTH);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUResolution(float width, float height) {
        float[] vec = new float[]{width, height};
        Log.d(TAG, "setUResolution: " + width + ", height: " + height);
        GLES20.glUniform2fv(mUResolution, 1, vec, 0);
    }

    public void setUExponent(float exponent) {
//        GLES20.glUniform1f(mUExponent, exponent);
    }

    public void setUStrength(float strength) {
        GLES20.glUniform1f(mUStrength, strength);
    }
}
