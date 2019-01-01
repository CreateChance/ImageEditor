package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Luminance melt transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class LuminanceMeltTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "luminance_melt.glsl";

    private final String U_DIRECTION = "direction";
    private final String U_THRESHOLD = "l_threshold";
    private final String U_ABOVE = "above";

    public LuminanceMeltTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_DIRECTION, true);
        addLocation(U_THRESHOLD, true);
        addLocation(U_ABOVE, true);
        loadLocation(programId);
    }

    public void setUDirection(boolean down) {
        setUniform(U_DIRECTION, down);
    }

    public void setUThreshold(float threshold) {
        setUniform(U_THRESHOLD, threshold);
    }

    public void setUAbove(boolean above) {
        setUniform(U_ABOVE, above);
    }
}
