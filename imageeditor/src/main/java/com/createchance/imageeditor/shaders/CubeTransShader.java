package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Cube transition shader.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CubeTransShader extends TransitionMainFragmentShader {
    private final String TRANS_SHADER = "cube.glsl";

    private final String U_PERSPECTIVE = "persp";
    private final String U_UNZOOM = "unzoom";
    private final String U_REFLECTION = "reflection";
    private final String U_FLOATING = "floating";

    public CubeTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        addLocation(U_PERSPECTIVE, true);
        addLocation(U_UNZOOM, true);
        addLocation(U_REFLECTION, true);
        addLocation(U_FLOATING, true);
        loadLocation(programId);
    }

    public void setUPerspective(float perspective) {
        setUniform(U_PERSPECTIVE, perspective);
    }

    public void setUUnzoom(float unzoom) {
        setUniform(U_UNZOOM, unzoom);
    }

    public void setUReflection(float reflection) {
        setUniform(U_REFLECTION, reflection);
    }

    public void setUFloating(float floating) {
        setUniform(U_FLOATING, floating);
    }
}
