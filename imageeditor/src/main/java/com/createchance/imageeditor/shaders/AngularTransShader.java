package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Angular transition shader.
 *
 * @author createchance
 * @date 2018/12/30
 */
public class AngularTransShader extends TransitionMainFragmentShader {

    private static final String TAG = "AngularTransShader";

    private final String TRANS_SHADER = "angular.glsl";

    private final String U_START_ANGLE = "startingAngle";

    private int mUStartAngle;

    public AngularTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        mUStartAngle = GLES20.glGetUniformLocation(programId, U_START_ANGLE);
        loadLocation(programId);
    }

    public void setUStartAngular(float startAngular) {
        GLES20.glUniform1f(mUStartAngle, startAngular);
    }
}
