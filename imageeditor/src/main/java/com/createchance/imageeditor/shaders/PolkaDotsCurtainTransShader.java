package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Polka dots curtain transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PolkaDotsCurtainTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "PolkaDotsCurtain.glsl";

    private final String U_DOTS = "dots";
    private final String U_CENTER = "center";

    public PolkaDotsCurtainTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_DOTS, true);
        addLocation(U_CENTER, true);
        loadLocation(programId);
    }

    public void setUDots(float dots) {
        setUniform(U_DOTS, dots);
    }

    public void setUCenter(float centerX, float centerY) {
        setUniform(U_CENTER, centerX, centerY);
    }
}
