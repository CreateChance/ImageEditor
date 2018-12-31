package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Dreamy zoom transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DreamyZoomTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "DreamyZoom.glsl";

    private final String U_ROTATION = "rotation";
    private final String U_SCALE = "scale";

    public DreamyZoomTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_ROTATION, true);
        addLocation(U_SCALE, true);
        loadLocation(programId);
    }

    public void setURotation(float rotation) {
        setUniform(U_ROTATION, rotation);
    }

    public void setUScale(float scale) {
        setUniform(U_SCALE, scale);
    }
}
