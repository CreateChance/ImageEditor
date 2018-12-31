package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Color distance transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class ColorDistanceTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "ColourDistance.glsl";

    private final String U_POWER = "power";

    public ColorDistanceTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_POWER, true);
        loadLocation(programId);
    }

    public void setUPower(float power) {
        setUniform(U_POWER, power);
    }
}
