package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Polar function transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PolarFunctionTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "polar_function.glsl";

    private final String U_SEGMENTS = "segments";

    public PolarFunctionTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SEGMENTS, true);
        loadLocation(programId);
    }

    public void setUSegments(int segments) {
        setUniform(U_SEGMENTS, segments);
    }
}
