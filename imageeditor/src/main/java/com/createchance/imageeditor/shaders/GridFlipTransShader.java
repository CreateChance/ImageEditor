package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Grid flip transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class GridFlipTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "GridFlip.glsl";

    private final String U_SIZE = "size";
    private final String U_PAUSE = "pause";
    private final String U_DIVIDER_WIDTH = "dividerWidth";
    private final String U_BG_COLOR = "bgcolor";
    private final String U_RANDOMNESS = "randomness";

    public GridFlipTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SIZE, true);
        addLocation(U_PAUSE, true);
        addLocation(U_DIVIDER_WIDTH, true);
        addLocation(U_BG_COLOR, true);
        addLocation(U_RANDOMNESS, true);
        loadLocation(programId);
    }

    public void setUSize(int width, int height) {
        setUniform(U_SIZE, width, height);
    }

    public void setUPause(float pause) {
        setUniform(U_PAUSE, pause);
    }

    public void setUDividerWidth(float dividerWidth) {
        setUniform(U_DIVIDER_WIDTH, dividerWidth);
    }

    public void setUBackColor(float red, float green, float blue, float alpha) {
        setUniform(U_BG_COLOR, red, green, blue, alpha);
    }

    public void setURandomness(float randomness) {
        setUniform(U_RANDOMNESS, randomness);
    }
}
