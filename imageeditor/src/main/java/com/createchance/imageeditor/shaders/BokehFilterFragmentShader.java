package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Bokeh filter fragment shader
 *
 * @author createchance
 * @date 2018/11/28
 */
public class BokehFilterFragmentShader extends AbstractShader {

    private static final String TAG = "BokehFilterFragmentShad";

    private final String FRAGMENT_SHADER = "BokehFilterFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_RESOLUTION = "u_Resolution";
    private final String U_RADIUS = "u_Radius";

    private int mUInputTexture, mUResolution, mURadius;

    public BokehFilterFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUResolution = GLES20.glGetUniformLocation(programId, U_RESOLUTION);
        mURadius = GLES20.glGetUniformLocation(programId, U_RADIUS);
    }

    public void setUResolution(float width, float height) {
        float[] resolution = new float[]{width, height};
        GLES20.glUniform2fv(mUResolution, 1, resolution, 0);
    }

    public void setURadius(float radius) {
        GLES20.glUniform1f(mURadius, radius);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }
}
