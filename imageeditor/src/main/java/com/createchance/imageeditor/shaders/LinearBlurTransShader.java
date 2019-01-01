package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Linear blur transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class LinearBlurTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "LinearBlur.glsl";

    private final String U_INTENSITY = "intensity";
    private final String U_PASSES = "passes";

    public LinearBlurTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_INTENSITY, true);
        addLocation(U_PASSES, true);
        loadLocation(programId);
    }

    public void setUIntensity(float intensity) {
        setUniform(U_INTENSITY, intensity);
    }

    public void setUPasses(int passes) {
        setUniform(U_PASSES, passes);
    }
}
