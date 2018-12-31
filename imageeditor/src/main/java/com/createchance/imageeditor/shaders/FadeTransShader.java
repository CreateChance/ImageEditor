package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Fade transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class FadeTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "fade.glsl";

    public FadeTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        loadLocation(programId);
    }
}
