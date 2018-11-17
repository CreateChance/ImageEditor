package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Highlight adjust shader class.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class HighlightAdjustFragmentShader extends AbstractShader {

    private static final String TAG = "HighlightAdjustFragment";

    private final String FRAGMENT_SHADER = "HighlightAdjustFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_HIGHLIGHT = "u_Highlight";

    private int mUInputTexture, mUHighlight;

    public HighlightAdjustFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mUHighlight = GLES20.glGetUniformLocation(programId, U_HIGHLIGHT);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUHighlight(float highlight) {
        GLES20.glUniform1f(mUHighlight, highlight);
    }
}
