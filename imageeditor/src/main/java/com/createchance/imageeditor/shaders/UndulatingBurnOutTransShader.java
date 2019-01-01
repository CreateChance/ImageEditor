package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Undulating burnout transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class UndulatingBurnOutTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "undulatingBurnOut.glsl";

    private final String U_SMOOTHNESS = "smoothness";
    private final String U_CENTER = "center";
    private final String U_COLOR = "color";

    public UndulatingBurnOutTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SMOOTHNESS, true);
        addLocation(U_CENTER, true);
        addLocation(U_COLOR, true);
        loadLocation(programId);
    }

    public void setUSmoothness(float smoothness) {
        setUniform(U_SMOOTHNESS, smoothness);
    }

    public void setUCenter(float centerX, float centerY) {
        setUniform(U_CENTER, centerX, centerY);
    }

    public void setUColor(float red, float green, float blue) {
        setUniform(U_COLOR, red, green, blue);
    }
}
