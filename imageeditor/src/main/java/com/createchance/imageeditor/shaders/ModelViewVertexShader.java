package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/10
 */
public class ModelViewVertexShader extends AbstractShader {

    private final String VERTEX_SHADER = "ModelViewVertexShader.glsl";

    private final String A_POSITION = "a_Position";
    private final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    private final String U_MODEL_MATRIX = "u_ModelMatrix";
    private final String U_VIEW_MATRIX = "u_ViewMatrix";
    private final String U_PROJECTION_MATRIX = "u_ProjectionMatrix";

    private int mAPosition, mATextureCoordinates, mUModelMatrix, mUViewMatrix, mUProjectionMatrix;

    public ModelViewVertexShader() {
        initShader(VERTEX_SHADER, GLES20.GL_VERTEX_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mAPosition = GLES20.glGetAttribLocation(programId, A_POSITION);
        mATextureCoordinates = GLES20.glGetAttribLocation(programId, A_TEXTURE_COORDINATES);
        mUModelMatrix = GLES20.glGetUniformLocation(programId, U_MODEL_MATRIX);
        mUViewMatrix = GLES20.glGetUniformLocation(programId, U_VIEW_MATRIX);
        mUProjectionMatrix = GLES20.glGetUniformLocation(programId, U_PROJECTION_MATRIX);
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

    public void setUModelMatrix(float[] matrix) {
        if (matrix == null || matrix.length != 16) {
            return;
        }

        GLES20.glUniformMatrix4fv(
                mUModelMatrix,
                1,
                false,
                matrix,
                0);
    }

    public void setUViewMatrix(float[] matrix) {
        if (matrix == null || matrix.length != 16) {
            return;
        }

        GLES20.glUniformMatrix4fv(
                mUViewMatrix,
                1,
                false,
                matrix,
                0);
    }

    public void setUProjectionMatrix(float[] matrix) {
        if (matrix == null || matrix.length != 16) {
            return;
        }

        GLES20.glUniformMatrix4fv(
                mUProjectionMatrix,
                1,
                false,
                matrix,
                0);
    }

    public void unsetAPosition() {
        GLES20.glDisableVertexAttribArray(mAPosition);
    }

    public void unsetATextureCoordinates() {
        GLES20.glDisableVertexAttribArray(mATextureCoordinates);
    }
}
