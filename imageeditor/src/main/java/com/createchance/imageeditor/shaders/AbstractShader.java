package com.createchance.imageeditor.shaders;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

import com.createchance.imageeditor.IEManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    private Map<String, Location> mLocationMap = new HashMap<>();

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

    protected final void addLocation(String name, boolean isUniform) {
        mLocationMap.put(name, new Location(isUniform));
    }

    protected void loadLocation(int programId) {
        Set<String> nameSet = mLocationMap.keySet();
        for (String name : nameSet) {
            Location location = mLocationMap.get(name);
            if (location.isUniform) {
                location.location = GLES20.glGetUniformLocation(programId, name);
            } else {
                location.location = GLES20.glGetAttribLocation(programId, name);
            }
        }
    }

    protected final void setUniform(String name, float value) {
        GLES20.glUniform1f(mLocationMap.get(name).location, value);
    }

    protected final void setUniform(String name, int value) {
        GLES20.glUniform1i(mLocationMap.get(name).location, value);
    }

    protected final void setUniform(String name, boolean value) {
        GLES20.glUniform1i(mLocationMap.get(name).location, value ? 1 : 0);
    }

    protected final void setUniform(String name, int v1, int v2) {
        int[] values = new int[2];
        values[0] = v1;
        values[1] = v2;
        GLES20.glUniform2iv(mLocationMap.get(name).location, 1, values, 0);
    }

    protected final void setUniform(String name, float v1, float v2) {
        float[] values = new float[2];
        values[0] = v1;
        values[1] = v2;
        GLES20.glUniform2fv(mLocationMap.get(name).location, 1, values, 0);
    }

    protected final void setUniform(String name, float v1, float v2, float v3) {
        float[] values = new float[3];
        values[0] = v1;
        values[1] = v2;
        values[2] = v3;
        GLES20.glUniform3fv(mLocationMap.get(name).location, 1, values, 0);
    }

    protected final void setUniform(String name, float v1, float v2, float v3, float v4) {
        float[] values = new float[4];
        values[0] = v1;
        values[1] = v2;
        values[2] = v3;
        values[3] = v4;
        GLES20.glUniform4fv(mLocationMap.get(name).location, 1, values, 0);
    }

    public int getShaderId() {
        return mShaderId;
    }

    public abstract void initLocation(int programId);

    private String loadShader(String[] assetPathList) {
        String result = null;
        InputStream in = null;
        BufferedReader bufferedReader = null;
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
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private class Location {
        int location = -1;
        boolean isUniform;

        Location(boolean isUniform) {
            this.isUniform = isUniform;
        }
    }
}
