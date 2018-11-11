package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/10
 */
public class LookupFragmentShader extends AbstractShader {

    private static final String TAG = "LookupFragmentShader";

    private final String FRAGMENT_SHADER = "LookupFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_LOOKUP_TEXTURE = "u_LookupTexture";
    private final String U_INTENSITY = "u_Intensity";

    private int mUInputTexture, mULookupTexture, mUIntensity;

    public LookupFragmentShader() {
        initShader(FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        mUInputTexture = GLES20.glGetUniformLocation(programId, U_INPUT_TEXTURE);
        mULookupTexture = GLES20.glGetUniformLocation(programId, U_LOOKUP_TEXTURE);
        mUIntensity = GLES20.glGetUniformLocation(programId, U_INTENSITY);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mUInputTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setULookupTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mULookupTexture, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUIntensity(float intensity) {
        GLES20.glUniform1f(mUIntensity, intensity);
    }
}
