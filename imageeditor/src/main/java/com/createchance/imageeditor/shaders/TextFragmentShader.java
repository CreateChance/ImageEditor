package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/10
 */
public class TextFragmentShader extends AbstractShader {

    private static final String TAG = "TextFragmentShader";

    private final String FRAGMENT_SHADER = "TextFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_BACKGROUND = "u_Background";
    private final String U_USE_BACKGROUND = "u_UseBackground";
    private final String U_TEXT_COLOR = "u_TextColor";
    private final String U_ALPHA_FACTOR = "u_AlphaFactor";

    private int mUInputTexture, mUBackground, mUUseBackground, mUTextColor, mUAlphaFactor;

    public TextFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUBackground = GLES20.glGetUniformLocation(programId, U_BACKGROUND);
        mUUseBackground = GLES20.glGetUniformLocation(programId, U_USE_BACKGROUND);
        mUTextColor = GLES20.glGetUniformLocation(programId, U_TEXT_COLOR);
        mUAlphaFactor = GLES20.glGetUniformLocation(programId, U_ALPHA_FACTOR);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUBackground(int textureTarget, int backgroundTextureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, backgroundTextureId);
        GLES20.glUniform1i(mUBackground, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUUseBackground(boolean useBackground) {
        if (useBackground) {
            GLES20.glUniform1i(mUUseBackground, 1);
        } else {
            GLES20.glUniform1i(mUUseBackground, 0);
        }
    }

    public void setUAlphaFactor(float alphaFactor) {
        GLES20.glUniform1f(mUAlphaFactor, alphaFactor);
    }

    public void setUTextColor(float red, float green, float blue) {
        GLES20.glUniform3f(mUTextColor, red, green, blue);
    }
}
