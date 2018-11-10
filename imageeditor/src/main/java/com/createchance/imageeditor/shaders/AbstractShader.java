package com.createchance.imageeditor.shaders;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

import com.createchance.imageeditor.IEManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/11/9
 */
public abstract class AbstractShader {

    private final String SHADER_FOLDER = "shaders/";

    protected int mShaderId;

    protected boolean initShader(String shaderSource, int type) {
        int[] compiled = new int[1];
        mShaderId = GLES20.glCreateShader(type);
        GLES20.glShaderSource(mShaderId, loadShader(shaderSource));
        GLES20.glCompileShader(mShaderId);
        GLES20.glGetShaderiv(mShaderId, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            mShaderId = -1;
            Log.e("Load Shader Failed", "Compilation\n" + GLES20.glGetShaderInfoLog(mShaderId));
            return false;
        }

        return true;
    }

    public int getShaderId() {
        return mShaderId;
    }

    public abstract void initLocation(int programId);

    private String loadShader(String assetPath) {
        String result = null;
        InputStream in = null;
        try {
            AssetManager am = IEManager.getInstance().getContext().getAssets();
            in = am.open(SHADER_FOLDER + assetPath);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            result = new String(buffer, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
