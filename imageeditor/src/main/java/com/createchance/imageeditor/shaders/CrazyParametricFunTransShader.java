package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Crazy parametric fun transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrazyParametricFunTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "CrazyParametricFun.glsl";

    private final String U_A = "a";
    private final String U_B = "b";
    private final String U_AMPLITUDE = "amplitude";
    private final String U_SMOOTHNESS = "smoothness";

    public CrazyParametricFunTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_A, true);
        addLocation(U_B, true);
        addLocation(U_AMPLITUDE, true);
        addLocation(U_SMOOTHNESS, true);
        loadLocation(programId);
    }

    public void setUA(float a) {
        setUniform(U_A, a);
    }

    public void setUB(float b) {
        setUniform(U_B, b);
    }

    public void setUAmplitude(float amplitude) {
        setUniform(U_AMPLITUDE, amplitude);
    }

    public void setUSmoothness(float smoothness) {
        setUniform(U_SMOOTHNESS, smoothness);
    }
}
