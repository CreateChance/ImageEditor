package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * 5 x 5 sample fragment shader.
 *
 * @author createchance
 * @date 2018/12/2
 */
public class FiveXFiveSampleFragmentShader extends AbstractShader {

    private static final String TAG = "FiveXFiveSampleFragment";

    private final String FRAGMENT_SHADER = "5x5SampleFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_SAMPLE_KERNEL = "u_SampleKernel";

    private int mUInputTexture, mUSampleKernel;

    public FiveXFiveSampleFragmentShader() {
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
        for (int i = 0; i < sampleKernel.length; i++) {
            GLES20.glUniform1f(mUSampleKernel + i, sampleKernel[i]);
        }
    }
}
