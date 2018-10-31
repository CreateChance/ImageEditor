package com.createchance.imageeditor;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.createchance.imageeditor.filters.OpenGlUtils;
import com.createchance.imageeditor.freetype.FreeType;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Draw text texture to video frame.
 *
 * @author createchance
 * @date 2018-10-08
 */
public class TextDrawer {
    private static final String TAG = "TextDrawer";

    private final String VERTEX_SHADER =
            "uniform mat4 u_Matrix;\n" +
                    "\n" +
                    "attribute vec4 a_Position;\n" +
                    "attribute vec2 a_TextureCoordinates;\n" +
                    "\n" +
                    "varying vec2 v_TextureCoordinates;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    v_TextureCoordinates = a_TextureCoordinates;\n" +
                    "    gl_Position = u_Matrix * a_Position;\n" +
                    "}\n";

    private final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "\n" +
                    "uniform sampler2D u_TextureUnit;\n" +
                    "varying vec2 v_TextureCoordinates;\n" +
                    "uniform vec3 u_TextColor;" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 sampledColor = vec4(1.0, 1.0, 1.0, texture2D(u_TextureUnit, v_TextureCoordinates).a);\n" +
                    "    gl_FragColor = vec4(u_TextColor, 1.0) * sampledColor;\n" +
                    "}\n";

    // Uniform
    private final String U_MATRIX = "u_Matrix";
    private final String U_TEXTURE_UNIT = "u_TextureUnit";
    private final String U_TEXT_COLOR = "u_TextColor";

    // Attribute
    private final String A_POSITION = "a_Position";
    private final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    private int mProgramId;

    private int mMatrixLocation, mTextureUnitLocation, mPositionLocaiton, mTextureCoorLocation, mTextColorLocation;

    private FloatBuffer mVertexPositionBuffer;
    private FloatBuffer mTextureCoordinateBuffer;

    private List<LoadedText> mLoadedTextList;

    private int mPosX, mPosY;
    private float mScaleFactor = 1.0f;
    private float mRed = 1.0f;
    private float mGreen = 1.0f;
    private float mBlue = 1.0f;

    private Bitmap mBackground;

    public TextDrawer() {
        mLoadedTextList = new ArrayList<>();

        mProgramId = OpenGlUtils.loadProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        // init locations
        mMatrixLocation = GLES20.glGetUniformLocation(mProgramId, U_MATRIX);
        mTextureUnitLocation = GLES20.glGetUniformLocation(mProgramId, U_TEXTURE_UNIT);
        mTextColorLocation = GLES20.glGetUniformLocation(mProgramId, U_TEXT_COLOR);
        mPositionLocaiton = GLES20.glGetAttribLocation(mProgramId, A_POSITION);
        mTextureCoorLocation = GLES20.glGetAttribLocation(mProgramId, A_TEXTURE_COORDINATES);

        GLES20.glUseProgram(mProgramId);

        mVertexPositionBuffer = OpenGlUtils.createFloatBuffer(
                new float[]{
                        -1.0f, 1.0f,
                        -1.0f, -1.0f,
                        1.0f, 1.0f,
                        1.0f, -1.0f,
                }
        );

        mTextureCoordinateBuffer = OpenGlUtils.createFloatBuffer(
                new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                }
        );

        // set matrix
        float[] projectionMatrix = new float[16];
        float[] modelMatrix = new float[16];
//        perspectiveM(projectionMatrix,45, (float) surfaceWidth
//                / (float) surfaceHeight, 1f, 10f);
        Matrix.orthoM(
                projectionMatrix,
                0,
                -1f,
                1f,
                1f,
                -1f,
                0.1f,
                100f
        );

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix, 0, 180f, 0f, 0f, 1f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
        OpenGlUtils.flip(projectionMatrix, true, false);

        glUniformMatrix4fv(
                mMatrixLocation,
                1,
                false,
                projectionMatrix,
                0);
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

    public void setParams(int posX, int posY, float red, float green, float blue, Bitmap background) {
        mPosX = posX;
        mPosY = posY;
        mRed = red;
        mGreen = green;
        mBlue = blue;
        mBackground = background;
    }

    public void draw() {
        GLES20.glUseProgram(mProgramId);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glActiveTexture(GL_TEXTURE0);
        glUniform1i(mTextureUnitLocation, 0);
        int currentPosX = mPosX;

        for (LoadedText loadedText : mLoadedTextList) {
            int xpos = currentPosX + (int) (loadedText.left * mScaleFactor);
            int ypos = mPosY - (int) ((loadedText.height - loadedText.top) * mScaleFactor);

            int w = (int) (loadedText.width * mScaleFactor);
            int h = (int) (loadedText.height * mScaleFactor);
            GLES20.glViewport(xpos, ypos, w, h);

            GLES20.glClearColor(0, 0, 0, 1);

            // bind texture
            glBindTexture(GL_TEXTURE_2D, loadedText.textureId);

            glEnableVertexAttribArray(mPositionLocaiton);
            mVertexPositionBuffer.position(0);
            glVertexAttribPointer(
                    mPositionLocaiton,
                    2,
                    GLES20.GL_FLOAT,
                    false,
                    0,
                    mVertexPositionBuffer);
            glEnableVertexAttribArray(mTextureCoorLocation);
            mTextureCoordinateBuffer.position(0);
            glVertexAttribPointer(
                    mTextureCoorLocation,
                    2,
                    GLES20.GL_FLOAT,
                    false,
                    0,
                    mTextureCoordinateBuffer);
            GLES20.glUniform3f(mTextColorLocation, mRed, mGreen, mBlue);
            glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            glDisableVertexAttribArray(mPositionLocaiton);
            glDisableVertexAttribArray(mTextureCoorLocation);

            currentPosX += (loadedText.advanceX >> 6) * mScaleFactor;
        }
        glDisable(GL_BLEND);
    }

    public void release() {
        glDeleteProgram(mProgramId);
    }

    public int getWidth() {
        int width = 0;
        int currentPosX = 0;
        for (LoadedText loadedText : mLoadedTextList) {
            width = currentPosX + (int) (loadedText.left * mScaleFactor);
            currentPosX += (loadedText.advanceX >> 6) * mScaleFactor;
        }
        width += mLoadedTextList.get(mLoadedTextList.size() - 1).width +
                (int) (mLoadedTextList.get(mLoadedTextList.size() - 1).left * mScaleFactor);

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
