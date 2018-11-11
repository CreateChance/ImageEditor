package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.BaseVertexShader;
import com.createchance.imageeditor.shaders.VignetteFragmentShader;

import java.nio.FloatBuffer;

/**
 * Vignetting effect drawer.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class VignetteDrawer extends AbstractDrawer {
    private static final String TAG = "VignetteDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer;

    private BaseVertexShader mVertexShader;
    private VignetteFragmentShader mFragmentShader;

    public VignetteDrawer() {
        mVertexShader = new BaseVertexShader();
        mFragmentShader = new VignetteFragmentShader();
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

    public void setVignetteCenter(float x, float y) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUVignetteCenter(x, y);
    }

    public void setVignetteColor(float red, float green, float blue) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUVignetteColor(red, green, blue);
    }

    public void setVignetteStart(float startValue) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUVignetteStart(startValue);
    }

    public void setVignetteEnd(float endValue) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUVignetteEnd(endValue);
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
