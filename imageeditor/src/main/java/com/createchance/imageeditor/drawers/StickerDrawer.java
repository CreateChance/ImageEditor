package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.createchance.imageeditor.shaders.ModelViewVertexShader;
import com.createchance.imageeditor.shaders.StickerFragmentShader;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.glDeleteProgram;

/**
 * Image drawer.
 *
 * @author createchance
 * @date 2018-10-12
 */
public class StickerDrawer extends AbstractDrawer {
    private static final String TAG = "StickerDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mTextureCoordinateBuffer;

    private ModelViewVertexShader mVertexShader;
    private StickerFragmentShader mFragmentShader;

    private float[] mModelMatrix, mViewMatrix, mProjectionMatrix;

    public StickerDrawer() {
        mVertexShader = new ModelViewVertexShader();
        mFragmentShader = new StickerFragmentShader();
        loadProgram(mVertexShader.getShaderId(), mFragmentShader.getShaderId());
        mVertexShader.initLocation(mProgramId);
        mFragmentShader.initLocation(mProgramId);

        mTextureCoordinateBuffer = createFloatBuffer(
                new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                }
        );
        mVertexPositionBuffer = createFloatBuffer(
                new float[]{
                        -1.0f, 1.0f,
                        -1.0f, -1.0f,
                        1.0f, 1.0f,
                        1.0f, -1.0f,
                }
        );

        mModelMatrix = new float[16];
        mViewMatrix = new float[16];
        mProjectionMatrix = new float[16];
    }

    public void setColor(float red,
                         float green,
                         float blue) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUStickerColor(red, green, blue);
    }

    public void setAlphaFactor(float alphaFactor) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUAlphaFactor(alphaFactor);
    }

    public void setRotate(float rotateX, float rotateY, float rotateZ) {
        GLES20.glUseProgram(mProgramId);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mProjectionMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, rotateX, 1, 0, 0);
        Matrix.rotateM(mModelMatrix, 0, rotateY, 0, 1, 0);
        Matrix.rotateM(mModelMatrix, 0, rotateZ, 0, 0, 1);
        mVertexShader.setUModelMatrix(mModelMatrix);
        mVertexShader.setUViewMatrix(mViewMatrix);
        mVertexShader.setUProjectionMatrix(mProjectionMatrix);
    }

    public void draw(int textureId,
                     int posX,
                     int posY,
                     int width,
                     int height) {
        GLES20.glUseProgram(mProgramId);

        GLES20.glViewport(posX, posY, width, height);

        GLES20.glClearColor(0, 0, 0, 0);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE);

        mFragmentShader.setUInputTexture(GLES20.GL_TEXTURE0, textureId);

        mVertexShader.setAPosition(mVertexPositionBuffer);
        mVertexShader.setATextureCoordinates(mTextureCoordinateBuffer);
        // draw sticker
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        mVertexShader.unsetAPosition();
        mVertexShader.unsetATextureCoordinates();
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public void release() {
        glDeleteProgram(mProgramId);
    }
}
