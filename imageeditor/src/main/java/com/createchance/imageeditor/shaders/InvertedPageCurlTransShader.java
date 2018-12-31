package com.createchance.imageeditor.shaders;

import android.opengl.GLES20;

/**
 * Inverted page curl transition shader.
 *
 * @author createchance
 * @date 2018/12/23
 */
public class InvertedPageCurlTransShader extends TransitionMainFragmentShader {

    private static final String TAG = "InvertedPageCurlTransShader";

    private final String TRANS_SHADER = "InvertedPageCurl.glsl";

    public InvertedPageCurlTransShader() {
        initShader(new String[]{TRANSITION_FOLDER + BASE_SHADER, TRANSITION_FOLDER + TRANS_SHADER}, GLES20.GL_FRAGMENT_SHADER);
    }

    @Override
    public void initLocation(int programId) {
        super.initLocation(programId);
        loadLocation(programId);
    }
}
