package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.ThreeXThreeSampleFragmentShader;
import com.createchance.imageeditor.shaders.ThreeXThreeSampleVertexShader;

import java.nio.FloatBuffer;

/**
 * 3 x 3 sampling drawer.
 *
 * @author createchance
 * @date 2018/11/27
 */
public class ThreeXThreeSampleDrawer extends AbstractDrawer {

    private static final String TAG = "ThreeXThreeSampleDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer;

    private ThreeXThreeSampleVertexShader mVertexShader;
    private ThreeXThreeSampleFragmentShader mFragmentShader;

    public ThreeXThreeSampleDrawer() {
        mVertexShader = new ThreeXThreeSampleVertexShader();
        mFragmentShader = new ThreeXThreeSampleFragmentShader();
        loadProgram(mVertexShader.getShaderId(), mFragmentShader.getShaderId());
        mVertexShader.initLocation(mProgramId);
        mFragmentShader.initLocation(mProgramId);

        mInputCoordinateBuffer = createFloatBuffer(
                new float[]{
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
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
    }

    public void setWidthStep(float widthStep) {
        GLES20.glUseProgram(mProgramId);
        mVertexShader.setUWidthStep(widthStep);
    }

    public void setHeightStep(float heightStep) {
        GLES20.glUseProgram(mProgramId);
        mVertexShader.setUHeightStep(heightStep);
    }

    public void setSampleKernel(float[] sampleKernel) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUSampleKernel(sampleKernel);
    }

    public void draw(int textureId,
                     int posX,
                     int posY,
                     int width,
                     int height) {
        GLES20.glUseProgram(mProgramId);

        GLES20.glViewport(posX, posY, width, height);

        GLES20.glClearColor(0, 0, 0, 0);

        mVertexShader.setAPosition(mVertexPositionBuffer);
        mVertexShader.setATextureCoordinates(mInputCoordinateBuffer);
        mFragmentShader.setUInputTexture(GLES20.GL_TEXTURE0, textureId);

        // draw filter
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        mVertexShader.unsetAPosition();
        mVertexShader.unsetATextureCoordinates();
    }
}
