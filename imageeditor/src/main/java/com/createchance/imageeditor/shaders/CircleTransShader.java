package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Circle transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CircleTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "circle.glsl";

    private final String U_CENTER = "center";
    private final String U_BACK_COLOR = "backColor";

    public CircleTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_CENTER, true);
        addLocation(U_BACK_COLOR, true);
        loadLocation(programId);
    }

    public void setUCenter(float centerX, float centerY) {
        setUniform(U_CENTER, centerX, centerY);
    }

    public void setUBackColor(float red, float green, float blue) {
        setUniform(U_BACK_COLOR, red, green, blue);
    }
}
