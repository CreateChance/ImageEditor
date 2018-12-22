package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.BaseVertexShader;
import com.createchance.imageeditor.shaders.BokehFilterFragmentShader;

import java.nio.FloatBuffer;

/**
 * Bokeh filter drawer.
 *
 * @author createchance
 * @date 2018/11/28
 */
public class BokehFilterDrawer extends AbstractDrawer {
    private static final String TAG = "BokehFilterDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer;

    private BaseVertexShader mVertexShader;
    private BokehFilterFragmentShader mFragmentShader;

    public BokehFilterDrawer() {
        mVertexShader = new BaseVertexShader();
        mFragmentShader = new BokehFilterFragmentShader();
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

    public void setResolution(float width, float height) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUResolution(width, height);
    }

    public void setRadius(float radius) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setURadius(radius);
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
