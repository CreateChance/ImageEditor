package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Hexagonalize transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class HexagonalizeTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "hexagonalize.glsl";

    private final String U_STEPS = "steps";
    private final String U_HORIZONTAL_HEX = "horizontalHexagons";

    public HexagonalizeTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_STEPS, true);
        addLocation(U_HORIZONTAL_HEX, true);
        loadLocation(programId);
    }

    public void setUStep(int step) {
        setUniform(U_STEPS, step);
    }

    public void setUHorizontalHexagons(float horizontalHexagons) {
        setUniform(U_HORIZONTAL_HEX, horizontalHexagons);
    }
}
