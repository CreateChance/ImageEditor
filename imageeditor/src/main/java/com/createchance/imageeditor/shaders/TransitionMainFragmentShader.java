package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Transition main fragment shader.
 *
 * @author createchance
 * @date 2018/12/23
 */
public class TransitionMainFragmentShader extends AbstractShader {

    private static final String TAG = "TransitionMainFragmentS";

    protected final String BASE_SHADER = "TransitionMainFragmentShader.glsl";

    private final String U_INPUT_TEXTURE = "u_InputTexture";
    private final String U_INPUT_TEXTURE2 = "u_InputTexture2";
    private final String U_PROGRESS = "progress";

    TransitionMainFragmentShader() {

    }

    @Override
    public void initLocation(int programId) {
        addLocation(U_INPUT_TEXTURE, true);
        addLocation(U_INPUT_TEXTURE2, true);
        addLocation(U_PROGRESS, true);
    }

    public void setUInputTexture(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        setUniform(U_INPUT_TEXTURE, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUInputTexture2(int textureTarget, int textureId) {
        // bind texture
        GLES20.glActiveTexture(textureTarget);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        setUniform(U_INPUT_TEXTURE2, textureTarget - GLES20.GL_TEXTURE0);
    }

    public void setUProgress(float progress) {
        setUniform(U_PROGRESS, progress);
    }
}
