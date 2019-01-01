package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Perlin transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PerlinTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "perlin.glsl";

    private final String U_SCALE = "scale";
    private final String U_SMOOTHNESS = "smoothness";
    private final String U_SEED = "seed";

    public PerlinTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SCALE, true);
        addLocation(U_SMOOTHNESS, true);
        addLocation(U_SEED, true);
        loadLocation(programId);
    }

    public void setUScale(float scale) {
        setUniform(U_SCALE, scale);
    }

    public void setUSmoothness(float smoothness) {
        setUniform(U_SMOOTHNESS, smoothness);
    }

    public void setUSeed(float seed) {
        setUniform(U_SEED, seed);
    }
}
