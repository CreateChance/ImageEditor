package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Bounce transition shader.
 *
 * @author createchance
 * @date 2018/12/30
 */
public class BounceTransShader extends TransitionMainFragmentShader {
    private static final String TAG = "AngularTransShader";

    private final String TRANS_SHADER = "Bounce.glsl";

    private final String U_SHADOW_COLOR = "shadow_colour";
    private final String U_SHADOW_HEIGHT = "shadow_height";
    private final String U_BOUNCES = "bounces";

    public BounceTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_SHADOW_COLOR, true);
        addLocation(U_SHADOW_HEIGHT, true);
        addLocation(U_BOUNCES, true);
        loadLocation(programId);
    }

    public void setUShadowColor(float red, float green, float blue, float alpha) {
        setUniform(U_SHADOW_COLOR, red, green, blue, alpha);
    }

    public void setUShadowHeight(float shadowHeight) {
        setUniform(U_SHADOW_HEIGHT, shadowHeight);
    }

    public void setUBounces(float bounces) {
        setUniform(U_BOUNCES, bounces);
    }
}
