package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.BaseVertexShader;
import com.createchance.imageeditor.shaders.ContrastAdjustFragmentShader;

import java.nio.FloatBuffer;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/11
 */
public class ContrastAdjustDrawer extends AbstractDrawer {
    private static final String TAG = "ContrastAdjustDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer;

    private BaseVertexShader mVertexShader;
    private ContrastAdjustFragmentShader mFragmentShader;

    public ContrastAdjustDrawer() {
        mVertexShader = new BaseVertexShader();
        mFragmentShader = new ContrastAdjustFragmentShader();
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

    public void setContrast(float contrast) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUContrast(contrast);
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
