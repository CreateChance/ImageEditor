package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.createchance.imageeditor.shaders.BaseFragmentShader;
import com.createchance.imageeditor.shaders.ModelViewVertexShader;

import java.nio.FloatBuffer;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/10/29
 */
public class BaseImageDrawer extends AbstractDrawer {

    private static final String TAG = "BaseImageDrawer";

    private FloatBuffer mVertexPositionBuffer;

    private FloatBuffer mTextureCoordinateBuffer;

    private ModelViewVertexShader mVertexShader;
    private BaseFragmentShader mFragmentShader;

    private float[] mModelMatrix, mViewMatrix, mProjectionMatrix;

    public BaseImageDrawer(float widthScaleFactor, float heightScaleFactor, boolean flipY) {
        mVertexShader = new ModelViewVertexShader();
        mFragmentShader = new BaseFragmentShader();
        loadProgram(mVertexShader.getShaderId(), mFragmentShader.getShaderId());
        mVertexShader.initLocation(mProgramId);
        mFragmentShader.initLocation(mProgramId);

        mVertexPositionBuffer = createFloatBuffer(
                new float[]{
                        -1.0f * widthScaleFactor, 1.0f * heightScaleFactor,
                        -1.0f * widthScaleFactor, -1.0f * heightScaleFactor,
                        1.0f * widthScaleFactor, 1.0f * heightScaleFactor,
                        1.0f * widthScaleFactor, -1.0f * heightScaleFactor,
                }
        );
        mTextureCoordinateBuffer = createFloatBuffer(
                new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                }
        );

        mModelMatrix = new float[16];
        mViewMatrix = new float[16];
        mProjectionMatrix = new float[16];
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mProjectionMatrix, 0);
        if (flipY) {
            Matrix.rotateM(mModelMatrix, 0, 180, 1, 0, 0);
        }
    }

    public void draw(int inputTexture,
                     int posX,
                     int posY,
                     int width,
                     int height) {
        GLES20.glUseProgram(mProgramId);

        GLES20.glViewport(posX, posY, width, height);

        GLES20.glClearColor(0, 0, 0, 0);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        mVertexShader.setUModelMatrix(mModelMatrix);
        mVertexShader.setUViewMatrix(mViewMatrix);
        mVertexShader.setUProjectionMatrix(mProjectionMatrix);
        mVertexShader.setAPosition(mVertexPositionBuffer);
        mVertexShader.setATextureCoordinates(mTextureCoordinateBuffer);
        mFragmentShader.setUInputTexture(GLES20.GL_TEXTURE0, inputTexture);

        // draw it.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        mVertexShader.unsetAPosition();
        mVertexShader.unsetATextureCoordinates();
    }
}
