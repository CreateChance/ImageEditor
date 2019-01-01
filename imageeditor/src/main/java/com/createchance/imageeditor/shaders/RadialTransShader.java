package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Radial transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RadialTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "Radial.glsl";

    private final String U_SMOOTHNESS = "smoothness";

    public RadialTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SMOOTHNESS, true);
        loadLocation(programId);
    }

    public void setUSmoothness(float smoothness) {
        setUniform(U_SMOOTHNESS, smoothness);
    }
}
