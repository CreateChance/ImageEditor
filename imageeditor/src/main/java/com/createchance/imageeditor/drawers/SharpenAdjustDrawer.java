package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.SharpenAdjustFragmentShader;
import com.createchance.imageeditor.shaders.SharpenAdjustVertexShader;

import java.nio.FloatBuffer;

/**
 * Sharpen adjust drawer.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class SharpenAdjustDrawer extends AbstractDrawer {
    private static final String TAG = "SharpenAdjustDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer;

    private SharpenAdjustVertexShader mVertexShader;
    private SharpenAdjustFragmentShader mFragmentShader;

    public SharpenAdjustDrawer() {
        mVertexShader = new SharpenAdjustVertexShader();
        mFragmentShader = new SharpenAdjustFragmentShader();
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

    public void setImageSizeFactor(int width, int height) {
        GLES20.glUseProgram(mProgramId);
        mVertexShader.setImageSizeFactor(width, height);
    }

    public void setSharpness(float sharpness) {
        GLES20.glUseProgram(mProgramId);
        mVertexShader.setSharpness(sharpness);
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
