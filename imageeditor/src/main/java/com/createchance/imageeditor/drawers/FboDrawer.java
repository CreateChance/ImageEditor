package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.BaseFragmentShader;
import com.createchance.imageeditor.shaders.BaseVertexShader;

import java.nio.FloatBuffer;

/**
 * ${DESC}
 *
 * @author gaochao02
 * @date 2018/11/22
 */
public class FboDrawer extends AbstractDrawer {

    private static final String TAG = "FboDrawer";

    private FloatBuffer mVertexPositionBuffer;

    private FloatBuffer mTextureCoordinateBuffer;

    private BaseVertexShader mVertexShader;
    private BaseFragmentShader mFragmentShader;

    public FboDrawer(float widthScaleFactor, float heightScaleFactor) {
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
        mTextureCoordinateBuffer = createFloatBuffer(
                new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                }
        );
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

        mVertexShader.setAPosition(mVertexPositionBuffer);
        mVertexShader.setATextureCoordinates(mTextureCoordinateBuffer);
        mFragmentShader.setUInputTexture(GLES20.GL_TEXTURE0, inputTexture);

        // draw it.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        mVertexShader.unsetAPosition();
        mVertexShader.unsetATextureCoordinates();
    }
}
