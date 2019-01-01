package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Mosaic transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class MosaicTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "Mosaic.glsl";

    private final String U_END_X = "endx";
    private final String U_END_Y = "endy";

    public MosaicTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_END_X, true);
        addLocation(U_END_Y, true);
        loadLocation(programId);
    }

    public void setUEndX(int endX) {
        setUniform(U_END_X, endX);
    }

    public void setUEndY(int endY) {
        setUniform(U_END_Y, endY);
    }
}
