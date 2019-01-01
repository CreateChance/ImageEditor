package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Stereo viewer transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class StereoViewerTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "StereoViewer.glsl";

    private final String U_ZOOM = "zoom";
    private final String U_CORNER_RADIUS = "corner_radius";

    public StereoViewerTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_ZOOM, true);
        addLocation(U_CORNER_RADIUS, true);
        loadLocation(programId);
    }

    public void setUZoom(float zoom) {
        setUniform(U_ZOOM, zoom);
    }

    public void setUCornerRadius(float cornerRadius) {
        setUniform(U_CORNER_RADIUS, cornerRadius);
    }
}
