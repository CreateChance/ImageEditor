package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Pixelize transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PixelizeTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "pixelize.glsl";

    private final String U_SQUARES_MIN = "squaresMin";
    private final String U_STEP = "step";

    public PixelizeTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SQUARES_MIN, true);
        addLocation(U_STEP, true);
        loadLocation(programId);
    }

    public void setUSquaresMin(int width, int height) {
        setUniform(U_SQUARES_MIN, width, height);
    }

    public void setUStep(int step) {
        setUniform(U_STEP, step);
    }
}
