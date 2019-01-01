package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Random square transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RandomSquaresTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "randomsquares.glsl";

    private final String U_SIZE = "size";
    private final String U_SMOOTHNESS = "smoothness";

    public RandomSquaresTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SMOOTHNESS, true);
        addLocation(U_SIZE, true);
        loadLocation(programId);
    }

    public void setUSize(int width, int height) {
        setUniform(U_SIZE, width, height);
    }

    public void setUSmoothness(float smoothness) {
        setUniform(U_SMOOTHNESS, smoothness);
    }
}
