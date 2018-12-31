package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Cross hatch transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrossHatchTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "crosshatch.glsl";

    private final String U_CENTER = "center";
    private final String U_THRESHOLD = "threshold";
    private final String U_FADE_EDGE = "fadeEdge";

    public CrossHatchTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_CENTER, true);
        addLocation(U_THRESHOLD, true);
        addLocation(U_FADE_EDGE, true);
        loadLocation(programId);
    }

    public void setUCenter(float centerX, float centerY) {
        setUniform(U_CENTER, centerX, centerY);
    }

    public void setUThreshold(float threshold) {
        setUniform(U_THRESHOLD, threshold);
    }

    public void setUFadeEdge(float fadeEdge) {
        setUniform(U_FADE_EDGE, fadeEdge);
    }
}
