package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Circle open transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CircleOpenTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "circleopen.glsl";

    private final String U_SMOOTHNESS = "smoothness";
    private final String U_OPENING = "opening";

    public CircleOpenTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SMOOTHNESS, true);
        addLocation(U_OPENING, true);
        loadLocation(programId);
    }

    public void setUSmoothness(float smoothness) {
        setUniform(U_SMOOTHNESS, smoothness);
    }

    public void setUOpening(boolean opening) {
        setUniform(U_OPENING, opening);
    }
}
