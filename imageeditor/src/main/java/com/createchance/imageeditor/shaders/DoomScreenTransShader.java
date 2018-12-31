package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Doom screen transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class DoomScreenTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "DoomScreenTransition.glsl";

    private final String U_BARS = "bars";
    private final String U_AMPLITUDE = "amplitude";
    private final String U_NOISE = "noise";
    private final String U_FREQUENCY = "frequency";
    private final String U_DRIP_SCALE = "dripScale";

    public DoomScreenTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_BARS, true);
        addLocation(U_AMPLITUDE, true);
        addLocation(U_NOISE, true);
        addLocation(U_FREQUENCY, true);
        addLocation(U_DRIP_SCALE, true);
        loadLocation(programId);
    }

    public void setUBars(int bars) {
        setUniform(U_BARS, bars);
    }

    public void setUAmplitude(float amplitude) {
        setUniform(U_AMPLITUDE, amplitude);
    }

    public void setUNoise(float noise) {
        setUniform(U_NOISE, noise);
    }

    public void setUFrequency(float frequency) {
        setUniform(U_FREQUENCY, frequency);
    }

    public void setUDripScale(float dripScale) {
        setUniform(U_DRIP_SCALE, dripScale);
    }
}
