package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * 5 x 5 sample vertex shader.
 *
 * @author createchance
 * @date 2018/12/2
 */
public class FiveXFiveSampleVertexShader extends AbstractShader {

    private static final String TAG = "FiveXFiveSampleVertexShader";

    private final String VERTEX_SHADER = "5x5SampleVertexShader.glsl";

    private final String A_POSITION = "a_Position";
    private final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    private final String U_WIDTH_STEP = "u_WidthStep";
    private final String U_HEIGHT_STEP = "u_HeightStep";

    private int mAPosition, mATextureCoordinates, mUWidthStep, mUHeightStep;

    public FiveXFiveSampleVertexShader() {
        initShader(VERTEX_SHADER, GLES20.GL_VERTEX_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mAPosition = GLES20.glGetAttribLocation(programId, A_POSITION);
        mATextureCoordinates = GLES20.glGetAttribLocation(programId, A_TEXTURE_COORDINATES);
        mUWidthStep = GLES20.glGetUniformLocation(programId, U_WIDTH_STEP);
        mUHeightStep = GLES20.glGetUniformLocation(programId, U_HEIGHT_STEP);
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

    public void setUWidthStep(float widthStep) {
        GLES20.glUniform1f(mUWidthStep, widthStep);
    }

    public void setUHeightStep(float heightStep) {
        GLES20.glUniform1f(mUHeightStep, heightStep);
    }

    public void unsetAPosition() {
        GLES20.glDisableVertexAttribArray(mAPosition);
    }

    public void unsetATextureCoordinates() {
        GLES20.glDisableVertexAttribArray(mATextureCoordinates);
    }
}
