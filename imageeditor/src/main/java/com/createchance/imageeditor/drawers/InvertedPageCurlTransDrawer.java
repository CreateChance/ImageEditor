package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.InvertedPageCurlTransShader;
import com.createchance.imageeditor.shaders.TransitionMainVertexShader;

import java.nio.FloatBuffer;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/12/23
 */
public class InvertedPageCurlTransDrawer extends AbstractDrawer {

    private static final String TAG = "InvertedPageCurlTransDr";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer;

    private TransitionMainVertexShader mVertexShader;
    private InvertedPageCurlTransShader mTransitionShader;

    public InvertedPageCurlTransDrawer() {
        mVertexShader = new TransitionMainVertexShader();
        mTransitionShader = new InvertedPageCurlTransShader();
        loadProgram(mVertexShader.getShaderId(), mTransitionShader.getShaderId());
        mVertexShader.initLocation(mProgramId);
        mTransitionShader.initLocation(mProgramId);

        mInputCoordinateBuffer = createFloatBuffer(
                new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
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

    public void setProgress(float progress) {
        GLES20.glUseProgram(mProgramId);
        mTransitionShader.setUProgress(progress);
    }

    public void draw(int textureId,
                     int textureId2,
                     int posX,
                     int posY,
                     int width,
                     int height) {
        GLES20.glUseProgram(mProgramId);

        GLES20.glViewport(posX, posY, width, height);

        GLES20.glClearColor(0, 0, 0, 0);

        mVertexShader.setAPosition(mVertexPositionBuffer);
        mVertexShader.setATextureCoordinates(mInputCoordinateBuffer);
        mTransitionShader.setUInputTexture(GLES20.GL_TEXTURE0, textureId);
        mTransitionShader.setUInputTexture2(GLES20.GL_TEXTURE3, textureId2);

        // draw filter
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        mVertexShader.unsetAPosition();
        mVertexShader.unsetATextureCoordinates();
    }
}
