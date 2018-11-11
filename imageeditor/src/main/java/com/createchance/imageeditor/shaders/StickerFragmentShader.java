package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;


/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/10
 */
public class StickerFragmentShader extends AbstractShader {

    private final String FRAGMENT_SHADER = "StickerFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_ALPHA_FACTOR = "u_AlphaFactor";
    private final String U_STICKER_COLOR = "u_StickerColor";

    private int mUInputTexture, mUAlphaFactor, mUStickerColor;


    public StickerFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUAlphaFactor = GLES20.glGetUniformLocation(programId, U_ALPHA_FACTOR);
        mUStickerColor = GLES20.glGetUniformLocation(programId, U_STICKER_COLOR);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUAlphaFactor(float alphaFactor) {
        GLES20.glUniform1f(mUAlphaFactor, alphaFactor);
    }

    public void setUStickerColor(float red, float green, float blue) {
        GLES20.glUniform3f(mUStickerColor, red, green, blue);
    }
}
