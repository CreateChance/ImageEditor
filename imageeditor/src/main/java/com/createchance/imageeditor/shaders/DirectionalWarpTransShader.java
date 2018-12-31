package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Directional warp transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DirectionalWarpTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "directionalwarp.glsl";

    private final String U_DIRECTIONAL = "direction";

    public DirectionalWarpTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_DIRECTIONAL, true);
        loadLocation(programId);
    }

    public void setUDirectional(float directionalX, float directionalY) {
        setUniform(U_DIRECTIONAL, directionalX, directionalY);
    }
}
