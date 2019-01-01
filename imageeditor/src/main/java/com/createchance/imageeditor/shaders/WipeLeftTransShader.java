package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Wipe left transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WipeLeftTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "wipeLeft.glsl";

    public WipeLeftTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        loadLocation(programId);
    }
}
