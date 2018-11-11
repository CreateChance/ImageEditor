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
 * @date 2018/11/9
 */
public class ModelViewDrawer extends AbstractDrawer {
    private static final String TAG = "ModelViewDrawer";

    private FloatBuffer mVertexPositionBuffer;

    private FloatBuffer mTextureCoordinateBuffer;

    private ModelViewVertexShader mVertexShader;
    private BaseFragmentShader mFragmentShader;

    private float[] mModelMatrix, mViewMatrix, mProjectionMatrix;

    public ModelViewDrawer() {
        mVertexShader = new ModelViewVertexShader();
        mFragmentShader = new BaseFragmentShader();
        loadProgram(mVertexShader.getShaderId(), mFragmentShader.getShaderId());
        mVertexShader.initLocation(mProgramId);
        mFragmentShader.initLocation(mProgramId);

        mVertexPositionBuffer = createFloatBuffer(
                new float[]{
                        -1.0f, 1.0f,
                        -1.0f, -1.0f,
                        1.0f, 1.0f,
                        1.0f, -1.0f,
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
    }

    public void setModel(float translateX,
                         float translateY,
                         float translateZ,
                         float rotateX,
                         float rotateY,
                         float rotateZ) {
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, translateX, translateY, -translateZ);
        Matrix.rotateM(mModelMatrix, 0, rotateX, 1, 0, 0);
        Matrix.rotateM(mModelMatrix, 0, rotateY, 0, 1, 0);
        Matrix.rotateM(mModelMatrix, 0, rotateZ, 0, 0, 1);
    }

    public void setPerspectiveProjection(float fov,
                                         float aspectRatio,
                                         float near,
                                         float far) {
        Matrix.setIdentityM(mProjectionMatrix, 0);
        Matrix.perspectiveM(mProjectionMatrix, 0, fov, aspectRatio, near, far);
    }

    public void setOrthographicProjection(float left,
                                          float right,
                                          float bottom,
                                          float top,
                                          float near,
                                          float far) {
        Matrix.setIdentityM(mProjectionMatrix, 0);
        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    public void draw(int inputTexture,
                     int posX,
                     int posY,
                     int width,
                     int height) {
        GLES20.glUseProgram(mProgramId);

        GLES20.glViewport(posX, posY, width, height);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        mVertexShader.setAPosition(mVertexPositionBuffer);
        mVertexShader.setATextureCoordinates(mTextureCoordinateBuffer);
        mVertexShader.setUModelMatrix(mModelMatrix);
        mVertexShader.setUViewMatrix(mViewMatrix);
        mVertexShader.setUProjectionMatrix(mProjectionMatrix);
        mFragmentShader.setUInputTexture(GLES20.GL_TEXTURE0, inputTexture);

        // draw it.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        mVertexShader.unsetAPosition();
        mVertexShader.unsetATextureCoordinates();
    }

}
