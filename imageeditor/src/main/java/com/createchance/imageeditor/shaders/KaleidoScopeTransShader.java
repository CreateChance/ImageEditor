package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Kaleido scope transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class KaleidoScopeTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "kaleidoscope.glsl";

    private final String U_SPEED = "speed";
    private final String U_ANGLE = "angle";
    private final String U_POWER = "power";

    public KaleidoScopeTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SPEED, true);
        addLocation(U_ANGLE, true);
        addLocation(U_POWER, true);
        loadLocation(programId);
    }

    public void setUSpeed(float speed) {
        setUniform(U_SPEED, speed);
    }

    public void setUAngle(float angle) {
        setUniform(U_ANGLE, angle);
    }

    public void setUPower(float power) {
        setUniform(U_POWER, power);
    }
}
