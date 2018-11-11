package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/9
 */
public class BaseVertexShader extends AbstractShader {

    private final String VERTEX_SHADER = "BaseVertexShader.glsl";

    // attrs and uniforms
    private final String A_POSITION = "a_Position";
    private final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    private int mAPosition, mATextureCoordinates;

    public BaseVertexShader() {
        initShader(VERTEX_SHADER, GLES20.GL_VERTEX_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mAPosition = GLES20.glGetAttribLocation(programId, A_POSITION);
        mATextureCoordinates = GLES20.glGetAttribLocation(programId, A_TEXTURE_COORDINATES);
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

    public void unsetAPosition() {
        GLES20.glDisableVertexAttribArray(mAPosition);
    }

    public void unsetATextureCoordinates() {
        GLES20.glDisableVertexAttribArray(mATextureCoordinates);
    }
}
