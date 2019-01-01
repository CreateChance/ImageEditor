package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Simple zoom transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SimpleZoomTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "SimpleZoom.glsl";

    private final String U_ZOOM_QUICKNESS = "zoom_quickness";

    public SimpleZoomTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_ZOOM_QUICKNESS, true);
        loadLocation(programId);
    }

    public void setUZoomQuickness(float zoomQuickness) {
        setUniform(U_ZOOM_QUICKNESS, zoomQuickness);
    }
}
