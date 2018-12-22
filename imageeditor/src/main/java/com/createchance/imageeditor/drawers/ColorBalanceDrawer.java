package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.BaseVertexShader;
import com.createchance.imageeditor.shaders.ColorBalanceFragmentShader;

import java.nio.FloatBuffer;

/**
 * Color balance adjustment drawer.
 *
 * @author gaochao02
 * @date 2018/12/21
 */
public class ColorBalanceDrawer extends AbstractDrawer {

    private static final String TAG = "ColorBalanceDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer;

    private BaseVertexShader mVertexShader;
    private ColorBalanceFragmentShader mFragmentShader;

    public ColorBalanceDrawer() {
        mVertexShader = new BaseVertexShader();
        mFragmentShader = new ColorBalanceFragmentShader();
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

    public void setRedShift(float redShift) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setURedShift(redShift);
    }

    public void setGreenShift(float greenShift) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUGreenShift(greenShift);
    }

    public void setBlueShift(float blueShift) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUBlueShift(blueShift);
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
