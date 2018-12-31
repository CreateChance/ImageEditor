package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Bow tie vertical transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BowTieVerticalTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "BowTieVertical.glsl";

    public BowTieVerticalTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        loadLocation(programId);
    }
}
