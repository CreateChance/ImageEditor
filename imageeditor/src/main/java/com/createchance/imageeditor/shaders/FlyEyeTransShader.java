package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Fly eye transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class FlyEyeTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "flyeye.glsl";

    private final String U_SIZE = "size";
    private final String U_ZOOM = "zoom";
    private final String U_COLOR_SEPARATION = "colorSeparation";

    public FlyEyeTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SIZE, true);
        addLocation(U_ZOOM, true);
        addLocation(U_COLOR_SEPARATION, true);
        loadLocation(programId);
    }

    public void setUSize(float size) {
        setUniform(U_SIZE, size);
    }

    public void setUZoom(float zoom) {
        setUniform(U_ZOOM, zoom);
    }

    public void setUColorSeparation(float colorSeparation) {
        setUniform(U_COLOR_SEPARATION, colorSeparation);
    }
}
