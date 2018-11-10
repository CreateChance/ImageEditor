package com.createchance.imageeditor.drawers;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.createchance.imageeditor.filters.OpenGlUtils;
import com.createchance.imageeditor.shaders.BaseVertexShader;
import com.createchance.imageeditor.shaders.LookupFragmentShader;

import java.nio.FloatBuffer;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/11/10
 */
public class LookupFilterDrawer extends AbstractDrawer {

    private static final String TAG = "LookupFilterDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mInputCoordinateBuffer, mLookupCoordinateBuffer;

    private BaseVertexShader mVertexShader;
    private LookupFragmentShader mFragmentShader;

    private int mLookupTextureId;

    public LookupFilterDrawer() {
        mVertexShader = new BaseVertexShader();
        mFragmentShader = new LookupFragmentShader();
        loadProgram(mVertexShader.getShaderId(), mFragmentShader.getShaderId());
        mVertexShader.initLocation(mProgramId);
        mFragmentShader.initLocation(mProgramId);

        mInputCoordinateBuffer = createFloatBuffer(
                new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                }
        );
        mLookupCoordinateBuffer = createFloatBuffer(
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

    public void setIntensity(float intensity) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUIntensity(intensity);
    }

    public void setLookup(Bitmap lookup) {
        mLookupTextureId = OpenGlUtils.loadTexture(lookup, OpenGlUtils.NO_TEXTURE);
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

        mVertexShader.setATextureCoordinates(mLookupCoordinateBuffer);
        mFragmentShader.setULookupTexture(GLES20.GL_TEXTURE3, mLookupTextureId);

        // draw filter
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        mVertexShader.unsetAPosition();
        mVertexShader.unsetATextureCoordinates();
    }

}
