package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Mosaic fragment shader.
 *
 * @author createchance
 * @date 2018/11/18
 */
public class MosaicFragmentShader extends AbstractShader {

    private static final String TAG = "MosaicFragmentShader";

    private final String FRAGMENT_SHADER = "MosaicFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_IMAGE_SIZE = "u_ImageSize";
    private final String U_MOSAIC_SIZE = "u_MosaicSize";

    private int mUInputTexture, mUImageSize, mUMosaicSize;

    public MosaicFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUImageSize = GLES20.glGetUniformLocation(programId, U_IMAGE_SIZE);
        mUMosaicSize = GLES20.glGetUniformLocation(programId, U_MOSAIC_SIZE);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUImageSize(int width, int height) {
        float[] size = new float[]{width, height};
        GLES20.glUniform2fv(mUImageSize, 1, size, 0);
    }

    public void setUMosaicSize(float width, float height) {
        float[] size = new float[]{width, height};
        GLES20.glUniform2fv(mUMosaicSize, 1, size, 0);
    }
}
