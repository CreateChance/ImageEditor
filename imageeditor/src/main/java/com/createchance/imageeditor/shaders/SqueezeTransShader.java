package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Squeeze transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SqueezeTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "squeeze.glsl";

    private final String U_COLOR_SEPARATION = "colorSeparation";

    public SqueezeTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_COLOR_SEPARATION, true);
        loadLocation(programId);
    }

    public void setUColorSeparation(float colorSeparation) {
        setUniform(U_COLOR_SEPARATION, colorSeparation);
    }
}
