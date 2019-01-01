package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Glitch Memories transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class GlitchMemoriesTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "GlitchMemories.glsl";

    public GlitchMemoriesTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        loadLocation(programId);
    }
}
