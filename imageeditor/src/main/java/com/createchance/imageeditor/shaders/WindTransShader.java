package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Wind transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WindTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "wind.glsl";

    private final String U_SIZE = "size";

    public WindTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SIZE, true);
        loadLocation(programId);
    }

    public void setUSize(float size) {
        setUniform(U_SIZE, size);
    }
}
