package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * ${DESC}
 *
 * @author gaochao02
 * @date 2018/11/17
 */
public class DenoiseFragmentShader extends AbstractShader {
    private static final String TAG = "DenoiseFragmentShader";

    private final String FRAGMENT_SHADER = "DenoiseFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_SKETCH_SIZE = "u_Resolution";

    private int mUInputTexture, mUSketchSize;

    public DenoiseFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUSketchSize = GLES20.glGetUniformLocation(programId, U_SKETCH_SIZE);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUSketchSize(float width, float height) {
        float[] vec = new float[]{width, height};
        GLES20.glUniform2fv(mUSketchSize, 1, vec, 0);
    }
}
