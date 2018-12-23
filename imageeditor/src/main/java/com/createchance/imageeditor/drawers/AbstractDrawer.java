package com.createchance.imageeditor.drawers;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Abstract drawer.
 *
 * @author createchance
 * @date 2018/11/9
 */
public abstract class AbstractDrawer {

    private static final int SIZEOF_FLOAT = 4;

    protected int mProgramId;

    protected void loadProgram(int... shaderIds) {
        int[] result = new int[1];
        int programId = GLES20.glCreateProgram();

        for (int id : shaderIds) {
            GLES20.glAttachShader(programId, id);
        }

        GLES20.glLinkProgram(programId);

        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, result, 0);
        if (result[0] <= 0) {
            Log.e("Load Program", "Linking Failed");
            return;
        }

        for (int id : shaderIds) {
            GLES20.glDeleteShader(id);
        }

        mProgramId = programId;
    }

    protected FloatBuffer createFloatBuffer(float[] coords) {
        // Allocate a direct ByteBuffer, using 4 bytes per float, and copy coords into it.
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * SIZEOF_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(coords);
        fb.position(0);
        return fb;
    }
}
