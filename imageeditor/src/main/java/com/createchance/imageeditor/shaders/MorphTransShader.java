package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Morph transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class MorphTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "morph.glsl";

    private final String U_STRENGTH = "strength";

    public MorphTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_STRENGTH, true);
        loadLocation(programId);
    }

    public void setUStrength(float strength) {
        setUniform(U_STRENGTH, strength);
    }
}
