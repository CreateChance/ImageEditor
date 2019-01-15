package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;

import com.createchance.imageeditor.shaders.TransitionMainFragmentShader;
import com.createchance.imageeditor.shaders.TransitionMainVertexShader;

import java.nio.FloatBuffer;

/**
 * Abstract transition drawer.
 *
 * @author createchance
 * @date 2018/12/30
 */
public abstract class AbstractTransDrawer extends AbstractDrawer {
    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer;

    protected TransitionMainVertexShader mVertexShader;
    protected TransitionMainFragmentShader mTransitionShader;

    public AbstractTransDrawer() {
        mVertexShader = new TransitionMainVertexShader();
        getTransitionShader();
        loadProgram(mVertexShader.getShaderId(), mTransitionShader.getShaderId());
        mVertexShader.initLocation(mProgramId);
        mTransitionShader.initLocation(mProgramId);

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

    protected abstract void getTransitionShader();

    public void setProgress(float progress) {
        GLES20.glUseProgram(mProgramId);
        mTransitionShader.setUProgress(progress);
    }

    public void setRatio(float ratio) {
        GLES20.glUseProgram(mProgramId);
        mTransitionShader.setURatio(ratio);
    }

    public void setToRatio(float ratio) {
        GLES20.glUseProgram(mProgramId);
        mTransitionShader.setUToRatio(ratio);
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
