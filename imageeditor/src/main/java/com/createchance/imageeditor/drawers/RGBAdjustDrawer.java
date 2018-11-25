package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.BaseVertexShader;
import com.createchance.imageeditor.shaders.RGBAdjustFragmentShader;

import java.nio.FloatBuffer;

/**
 * RGB channel adjust drawer.
 *
 * @author createchance
 * @date 2018/11/25
 */
public class RGBAdjustDrawer extends AbstractDrawer {

    private static final String TAG = "RGBAdjustDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer;

    private BaseVertexShader mVertexShader;
    private RGBAdjustFragmentShader mFragmentShader;

    public RGBAdjustDrawer() {
        mVertexShader = new BaseVertexShader();
        mFragmentShader = new RGBAdjustFragmentShader();
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

    public void setRed(float red) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setURed(red);
    }

    public void setGreen(float green) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUGreen(green);
    }

    public void setBlue(float blue) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUBlue(blue);
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
