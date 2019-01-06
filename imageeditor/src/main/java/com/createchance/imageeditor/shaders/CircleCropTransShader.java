package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Circle crop transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CircleCropTransShader extends TransitionMainFragmentShader {

    private final String TRANS_SHADER = "CircleCrop.glsl";

    private final String U_BACK_COLOR = "bgcolor";

    public CircleCropTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_BACK_COLOR, true);
        loadLocation(programId);
    }

    public void setUBackColor(float red, float green, float blue, float alpha) {
        setUniform(U_BACK_COLOR, red, green, blue, alpha);
    }
}
