package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * Sharpness adjust vertex shader.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class SharpenAdjustVertexShader extends AbstractShader {
    private static final String TAG = "SharpenAdjustVertexShad";

    private final String VERTEX_SHADER = "SharpenAdjustVertexShader.glsl";

    private final String A_POSITION = "a_Position";
    private final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    private final String U_IMAGE_WIDTH_FACTOR = "u_ImageWidthFactor";
    private final String U_IMAGE_HEIGHT_FACTOR = "u_ImageHeightFactor";
    private final String U_SHARPNESS = "u_Sharpness";

    private int mAPosition, mATextureCoordinates, mUImageWidthFactor, mUImageHeightFactor, mUSharpness;

    public SharpenAdjustVertexShader() {
        initShader(VERTEX_SHADER, GLES20.GL_VERTEX_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mAPosition = GLES20.glGetAttribLocation(programId, A_POSITION);
        mATextureCoordinates = GLES20.glGetAttribLocation(programId, A_TEXTURE_COORDINATES);
        mUImageWidthFactor = GLES20.glGetUniformLocation(programId, U_IMAGE_WIDTH_FACTOR);
        mUImageHeightFactor = GLES20.glGetUniformLocation(programId, U_IMAGE_HEIGHT_FACTOR);
        mUSharpness = GLES20.glGetUniformLocation(programId, U_SHARPNESS);
    }

    public void setAPosition(FloatBuffer buffer) {
        GLES20.glEnableVertexAttribArray(mAPosition);
        buffer.position(0);
        GLES20.glVertexAttribPointer(
                mAPosition,
                2,
                GLES20.GL_FLOAT,
                false,
                0,
                buffer);
    }

    public void setATextureCoordinates(FloatBuffer buffer) {
        GLES20.glEnableVertexAttribArray(mATextureCoordinates);
        buffer.position(0);
        GLES20.glVertexAttribPointer(
                mATextureCoordinates,
                2,
                GLES20.GL_FLOAT,
                false,
                0,
                buffer);
    }

    public void setImageSizeFactor(int width, int height) {
        GLES20.glUniform1f(mUImageWidthFactor, 1.0f / width);
        GLES20.glUniform1f(mUImageHeightFactor, 1.0f / height);
    }

    public void setSharpness(float sharpness) {
        GLES20.glUniform1f(mUSharpness, sharpness);
    }

    public void unsetAPosition() {
        GLES20.glDisableVertexAttribArray(mAPosition);
    }

    public void unsetATextureCoordinates() {
        GLES20.glDisableVertexAttribArray(mATextureCoordinates);
    }
}
