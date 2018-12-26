package com.createchance.imageeditor;

import android.graphics.SurfaceTexture;

import com.createchance.imageeditor.gles.EglCore;

/**
 * ${DESC}
 *
 * @author gaochao02
 * @date 2018/12/24
 */
public interface IRenderTarget {

    void init(EglCore eglCore, SurfaceTexture surfaceTexture);

    int getInputTextureId();

    int getOutputTextureId();

    void bindOffScreenFrameBuffer();

    void attachOffScreenTexture(int textureId);

    void bindDefaultFrameBuffer();

    int getSurfaceWidth();

    int getSurfaceHeight();

    void swapTexture();

    void makeCurrent();

    void swapBuffers();

    void release();
}
