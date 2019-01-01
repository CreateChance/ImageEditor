package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Squares wire transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SquaresWireTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "squareswire.glsl";

    private final String U_SQUARES = "squares";
    private final String U_DIRECTION = "direction";
    private final String U_SMOOTHNESS = "smoothness";

    public SquaresWireTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SQUARES, true);
        addLocation(U_DIRECTION, true);
        addLocation(U_SMOOTHNESS, true);
        loadLocation(programId);
    }

    public void setUSquares(int width, int height) {
        setUniform(U_SQUARES, width, height);
    }

    public void setUDirection(float x, float y) {
        setUniform(U_DIRECTION, x, y);
    }

    public void setUSmoothness(float smoothness) {
        setUniform(U_SMOOTHNESS, smoothness);
    }
}
