package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Butterfly Wave Scrawler transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class ButterflyWaveScrawlerTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "ButterflyWaveScrawler.glsl";

    private final String U_AMPLITUDE = "amplitude";
    private final String U_WAVES = "waves";
    private final String U_COLOR_SEPARATION = "colorSeparation";

    public ButterflyWaveScrawlerTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_AMPLITUDE, true);
        addLocation(U_WAVES, true);
        addLocation(U_COLOR_SEPARATION, true);
        loadLocation(programId);
    }

    public void setUAmplitude(float amplitude) {
        setUniform(U_AMPLITUDE, amplitude);
    }

    public void setUWaves(float waves) {
        setUniform(U_WAVES, waves);
    }

    public void setUColorSeparation(float colorSeparation) {
        setUniform(U_COLOR_SEPARATION, colorSeparation);
    }
}
