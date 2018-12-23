package com.createchance.imageeditor.shaders;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

import com.createchance.imageeditor.IEManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Abstract opengl shader class.
 *
 * @author createchance
 * @date 2018/11/9
 */
public abstract class AbstractShader {

    protected final String SHADER_FOLDER = "shaders/";

    protected final String TRANSITION_FOLDER = "transitions/";

    protected int mShaderId;

    protected boolean initShader(String shaderSource, int type) {
        return initShader(new String[]{SHADER_FOLDER + shaderSource}, type);
    }

    protected boolean initShader(String[] shaderSourceList, int type) {
        int[] compiled = new int[1];
        mShaderId = GLES20.glCreateShader(type);
        GLES20.glShaderSource(mShaderId, loadShader(shaderSourceList));
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

    private String loadShader(String[] assetPathList) {
        String result = null;
        InputStream in = null;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (String assetPath : assetPathList) {
                AssetManager am = IEManager.getInstance().getContext().getAssets();
                in = am.open(assetPath);
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
            }
            result = stringBuilder.toString();
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
