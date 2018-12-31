package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Burn transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class BurnTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "burn.glsl";

    private final String U_COLOR = "color";

    public BurnTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_COLOR, true);
        loadLocation(programId);
    }

    public void setUColor(float red, float green, float blue) {
        setUniform(U_COLOR, red, green, blue);
    }
}
