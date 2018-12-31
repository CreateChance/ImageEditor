package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Directional wipe transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DirectionalWipeTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "directionalwipe.glsl";

    private final String U_DIRECTIONAL = "direction";
    private final String U_SMOOTHNESS = "smoothness";

    public DirectionalWipeTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_DIRECTIONAL, true);
        addLocation(U_SMOOTHNESS, true);
        loadLocation(programId);
    }

    public void setUDirectional(float directionalX, float directionalY) {
        setUniform(U_DIRECTIONAL, directionalX, directionalY);
    }

    public void setUSmoothness(float smoothness) {
        setUniform(U_SMOOTHNESS, smoothness);
    }
}
