package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.BaseFragmentShader;
import com.createchance.imageeditor.shaders.BaseVertexShader;

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
    private FloatBuffer mTextureCoordinateBufferFliped;

    private BaseVertexShader mVertexShader;
    private BaseFragmentShader mFragmentShader;

    public BaseImageDrawer(float widthScaleFactor, float heightScaleFactor) {
        mVertexShader = new BaseVertexShader();
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
        mTextureCoordinateBufferFliped = createFloatBuffer(
                new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                }
        );
        mTextureCoordinateBuffer = createFloatBuffer(
                new float[]{
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                }
        );
    }

    public void draw(int inputTexture,
                     int posX,
                     int posY,
                     int width,
                     int height,
                     boolean flipY) {
        GLES20.glUseProgram(mProgramId);

        GLES20.glViewport(posX, posY, width, height);

        mVertexShader.setAPosition(mVertexPositionBuffer);
        if (flipY) {
            mVertexShader.setATextureCoordinates(mTextureCoordinateBufferFliped);
        } else {
            mVertexShader.setATextureCoordinates(mTextureCoordinateBuffer);
        }
        mFragmentShader.setUInputTexture(GLES20.GL_TEXTURE0, inputTexture);

        // draw it.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        mVertexShader.unsetAPosition();
        mVertexShader.unsetATextureCoordinates();
    }
}
