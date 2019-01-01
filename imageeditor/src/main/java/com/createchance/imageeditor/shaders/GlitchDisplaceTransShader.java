package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Glitch displace transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class GlitchDisplaceTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "GlitchDisplace.glsl";

    public GlitchDisplaceTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        loadLocation(programId);
    }
}
