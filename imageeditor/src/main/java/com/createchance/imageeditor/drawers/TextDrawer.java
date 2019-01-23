package com.createchance.imageeditor.drawers;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.createchance.imageeditor.freetype.FreeType;
import com.createchance.imageeditor.shaders.ModelViewVertexShader;
import com.createchance.imageeditor.shaders.TextFragmentShader;
import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditor.utils.OpenGlUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Draw text texture to image.
 *
 * @author createchance
 * @date 2018-10-08
 */
public class TextDrawer extends AbstractDrawer {
    private static final String TAG = "TextDrawer";

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mTextureCoordinateBuffer;

    private List<LoadedText> mLoadedTextList;

    private ModelViewVertexShader mVertexShader;
    private TextFragmentShader mFragmentShader;

    private int mBackgroundTextureId = -1;

    private float[] mModelMatrix, mViewMatrix, mProjectionMatrix;

    public TextDrawer() {
        mVertexShader = new ModelViewVertexShader();
        mFragmentShader = new TextFragmentShader();
        loadProgram(mVertexShader.getShaderId(), mFragmentShader.getShaderId());
        mVertexShader.initLocation(mProgramId);
        mFragmentShader.initLocation(mProgramId);

        mLoadedTextList = new ArrayList<>();

        mVertexPositionBuffer = createFloatBuffer(
                new float[]{
                        -1.0f, 1.0f,
                        -1.0f, -1.0f,
                        1.0f, 1.0f,
                        1.0f, -1.0f,
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

        mModelMatrix = new float[16];
        mViewMatrix = new float[16];
        mProjectionMatrix = new float[16];
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mProjectionMatrix, 0);
    }

    public void setText(String fontPath, String text, int textSize) {
        char[] textArray = text.toCharArray();
        int[] unicodeTextArray = new int[textArray.length];
        mLoadedTextList.clear();
        for (int i = 0; i < textArray.length; i++) {
            mLoadedTextList.add(new LoadedText(textArray[i]));
            unicodeTextArray[i] = textArray[i];
        }

        int[] result = FreeType.loadText(fontPath, unicodeTextArray, textSize);
        for (int i = 0, j = 0; i < result.length; i += 7) {
            LoadedText loadedText = mLoadedTextList.get(j++);
            loadedText.textureId = result[i];
            loadedText.width = result[i + 1];
            loadedText.height = result[i + 2];
            loadedText.left = result[i + 3];
            loadedText.top = result[i + 4];
            loadedText.advanceX = result[i + 5];
            loadedText.advanceY = result[i + 6];
        }
    }

    public void setTextColor(float red, float green, float blue) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUUseBackground(false);
        if (mBackgroundTextureId != -1) {
            GLES20.glDeleteTextures(0, new int[]{mBackgroundTextureId}, 0);
            mBackgroundTextureId = -1;
        }
        mFragmentShader.setUTextColor(red, green, blue);
    }

    public void setTextAlpha(float alphaFactor) {
        GLES20.glUseProgram(mProgramId);
        mFragmentShader.setUAlphaFactor(alphaFactor);
    }

    public void setTextBackground(Bitmap background) {
        GLES20.glUseProgram(mProgramId);
        if (background != null) {
            mFragmentShader.setUUseBackground(true);
            mBackgroundTextureId = OpenGlUtils.loadTexture(background, OpenGlUtils.NO_TEXTURE, true);
        } else {
            Logger.e(TAG, "Text background set failed, background can not be null!");
        }
    }

    public void draw(int posX, int posY) {
        GLES20.glUseProgram(mProgramId);

        GLES20.glEnable(GL_BLEND);
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE);

        int currentPosX = posX;

        for (LoadedText loadedText : mLoadedTextList) {
            int xpos = currentPosX + loadedText.left;
            int ypos = posY - (loadedText.height - loadedText.top);

            int w = loadedText.width;
            int h = loadedText.height;
            GLES20.glViewport(xpos, ypos, w, h);

            GLES20.glClearColor(0, 0, 0, 1);

            mFragmentShader.setUInputTexture(GLES20.GL_TEXTURE0, loadedText.textureId);

            mVertexShader.setAPosition(mVertexPositionBuffer);
            mVertexShader.setATextureCoordinates(mTextureCoordinateBuffer);
            mVertexShader.setUModelMatrix(mModelMatrix);
            mVertexShader.setUViewMatrix(mViewMatrix);
            mVertexShader.setUProjectionMatrix(mProjectionMatrix);

            // draw text
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            // draw background if need
            if (mBackgroundTextureId != -1) {
                mFragmentShader.setUBackground(GLES20.GL_TEXTURE3, mBackgroundTextureId);
                glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            }

            mVertexShader.unsetAPosition();
            mVertexShader.unsetATextureCoordinates();

            currentPosX += loadedText.advanceX >> 6;
        }
        GLES20.glDisable(GL_BLEND);
    }

    public void release() {
        glDeleteProgram(mProgramId);
    }

    public int getWidth() {
        int width = 0;
        int currentPosX = 0;
        for (LoadedText loadedText : mLoadedTextList) {
            width = currentPosX + loadedText.left;
            currentPosX += loadedText.advanceX >> 6;
        }
        width += mLoadedTextList.get(mLoadedTextList.size() - 1).width +
                mLoadedTextList.get(mLoadedTextList.size() - 1).left;

        return width;
    }

    public int getHeight() {
        int height = 0;
        for (LoadedText loadedText : mLoadedTextList) {
            if (loadedText.height > height) {
                height = loadedText.height;
            }
        }

        return height;
    }

    private class LoadedText {
        public int value;
        public int textureId;
        public int width;
        public int height;
        public int left;
        public int top;
        public int advanceX;
        public int advanceY;

        public LoadedText(int value) {
            this.value = value;
        }
    }
}
