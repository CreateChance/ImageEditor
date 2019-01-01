package com.createchance.imageeditor.shaders;


import android.opengl.GLES20;

/**
 * Rotate scale fade transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class RotateScaleFadeTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "rotate_scale_fade.glsl";

    private final String U_CENTER = "center";
    private final String U_ROTATIONS = "rotations";
    private final String U_SCALE = "scale";
    private final String U_BACK_COLOR = "backColor";

    public RotateScaleFadeTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_CENTER, true);
        addLocation(U_ROTATIONS, true);
        addLocation(U_SCALE, true);
        addLocation(U_BACK_COLOR, true);
        loadLocation(programId);
    }

    public void setUCenter(float centerX, float centerY) {
        setUniform(U_CENTER, centerX, centerY);
    }

    public void setURotations(float rotations) {
        setUniform(U_ROTATIONS, rotations);
    }

    public void setUScale(float scale) {
        setUniform(U_SCALE, scale);
    }

    public void setUBackColor(float red, float green, float blue, float alpha) {
        setUniform(U_BACK_COLOR, red, green, blue, alpha);
    }
}
