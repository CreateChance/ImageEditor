package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Pin wheel transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PinWheelTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "pinwheel.glsl";

    private final String U_SPEED = "speed";

    public PinWheelTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SPEED, true);
        loadLocation(programId);
    }

    public void setUSpeed(float speed) {
        setUniform(U_SPEED, speed);
    }
}
