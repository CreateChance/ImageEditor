package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Vignetting fragment shader.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class VignetteFragmentShader extends AbstractShader {
    private static final String TAG = "VignetteFragmentShader";

    private final String FRAGMENT_SHADER = "VignetteFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_VIGNETTE_CENTER = "u_VignetteCenter";
    private final String U_VIGNETTE_COLOR = "u_VignetteColor";
    private final String U_VIGNETTE_START = "u_VignetteStart";
    private final String U_VIGNETTE_END = "u_VignetteEnd";

    private int mUInputTexture, mUVignetteCenter, mUVignetteColor, mUVignetteStart, mUVignetteEnd;

    public VignetteFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUVignetteCenter = GLES20.glGetUniformLocation(programId, U_VIGNETTE_CENTER);
        mUVignetteColor = GLES20.glGetUniformLocation(programId, U_VIGNETTE_COLOR);
        mUVignetteStart = GLES20.glGetUniformLocation(programId, U_VIGNETTE_START);
        mUVignetteEnd = GLES20.glGetUniformLocation(programId, U_VIGNETTE_END);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUVignetteCenter(float x, float y) {
        float[] point = new float[2];
        point[0] = x;
        point[1] = y;
        GLES20.glUniform2fv(mUVignetteCenter, 1, point, 0);
    }

    public void setUVignetteColor(float red, float green, float blue) {
        GLES20.glUniform3f(mUVignetteColor, red, green, blue);
    }

    public void setUVignetteStart(float startValue) {
        GLES20.glUniform1f(mUVignetteStart, startValue);
    }

    public void setUVignetteEnd(float endValue) {
        GLES20.glUniform1f(mUVignetteEnd, endValue);
    }
}
