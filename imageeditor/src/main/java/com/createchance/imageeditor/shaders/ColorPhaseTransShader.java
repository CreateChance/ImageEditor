package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Color phase transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class ColorPhaseTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "colorphase.glsl";

    private final String U_FROM_STEP = "fromStep";
    private final String U_TO_STEP = "toStep";

    public ColorPhaseTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_FROM_STEP, true);
        addLocation(U_TO_STEP, true);
        loadLocation(programId);
    }

    public void setUFromStep(float red, float green, float blue, float alpha) {
        setUniform(U_FROM_STEP, red, green, blue, alpha);
    }

    public void setUToStep(float red, float green, float blue, float alpha) {
        setUniform(U_TO_STEP, red, green, blue, alpha);
    }
}
