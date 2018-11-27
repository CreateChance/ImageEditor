package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * 3 x 3 sampling fragment shader.
 *
 * @author createchance
 * @date 2018/11/27
 */
public class ThreeXThreeSampleFragmentShader extends AbstractShader {

    private static final String TAG = "ThreeXThreeSampleFragme";

    private final String FRAGMENT_SHADER = "3x3SampleFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_SAMPLE_KERNEL = "u_SampleKernel";

    private int mUInputTexture, mUSampleKernel;

    public ThreeXThreeSampleFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUSampleKernel = GLES20.glGetUniformLocation(programId, U_SAMPLE_KERNEL);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUSampleKernel(float[] sampleKernel) {
        GLES20.glUniformMatrix3fv(mUSampleKernel, 1, false, sampleKernel, 0);
    }
}
