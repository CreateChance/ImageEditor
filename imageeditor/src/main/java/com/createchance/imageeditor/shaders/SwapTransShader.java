package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Swap transition shader.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class SwapTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "swap.glsl";

    private final String U_REFLECTION = "reflection";
    private final String U_PERSPECTIVE = "perspective";
    private final String U_DEPTH = "depth";

    public SwapTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_REFLECTION, true);
        addLocation(U_PERSPECTIVE, true);
        addLocation(U_DEPTH, true);
        loadLocation(programId);
    }

    public void setUReflection(float reflection) {
        setUniform(U_REFLECTION, reflection);
    }

    public void setUPerspective(float perspective) {
        setUniform(U_PERSPECTIVE, perspective);
    }

    public void setUDepth(float depth) {
        setUniform(U_DEPTH, depth);
    }
}
