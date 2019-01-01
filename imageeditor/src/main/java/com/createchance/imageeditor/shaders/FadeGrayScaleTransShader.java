package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Fade gray scale transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class FadeGrayScaleTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "fadegrayscale.glsl";

    private final String U_INTENSITY = "intensity";

    public FadeGrayScaleTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_INTENSITY, true);
        loadLocation(programId);
    }

    public void setUIntensity(float intensity) {
        setUniform(U_INTENSITY, intensity);
    }
}
