package com.createchance.imageeditor;

import android.content.Context;
import android.opengl.GLES20;
import android.util.AttributeSet;
import android.view.TextureView;

import com.createchance.imageeditor.gles.EglCore;
import com.createchance.imageeditor.gles.WindowSurface;

/**
 * Preview view.
 *
 * @author createchance
 * @date 2018/12/24
 */
public class IEPreviewView extends TextureView implements IRenderTarget {

    private static final String TAG = "IEPreviewView";

    private int[] mOffScreenFrameBuffer = new int[1];
    private int[] mOffScreenTextureIds = new int[2];
    private int mInputTextureIndex = 0, mOutputTextureIndex = 1;

    private WindowSurface mWindowSurface;

    public IEPreviewView(Context context) {
        super(context);
    }

    public IEPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IEPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void makeCurrent() {
        if (mWindowSurface != null) {
            mWindowSurface.makeCurrent();
        }
    }

    @Override
    public void swapBuffers() {
        if (mWindowSurface != null) {
            mWindowSurface.swapBuffers();
        }
    }

    @Override
    public void init(EglCore eglCore) {
        mWindowSurface = new WindowSurface(eglCore, getSurfaceTexture());
        mWindowSurface.makeCurrent();
        createOffScreenFrameBuffer();
        createOffScreenTextures();
    }

    @Override
    public int getSurfaceWidth() {
        return getWidth();
    }

    @Override
    public int getSurfaceHeight() {
        return getHeight();
    }

    @Override
    public int getInputTextureId() {
        return mOffScreenTextureIds[mInputTextureIndex];
    }

    @Override
    public int getOutputTextureId() {
        return mOffScreenTextureIds[mOutputTextureIndex];
    }

    @Override
    public void swapTexture() {
        int tmp = mInputTextureIndex;
        mInputTextureIndex = mOutputTextureIndex;
        mOutputTextureIndex = tmp;
    }

    @Override
    public void release() {
        deleteOffScreenFrameBuffer();
        if (mWindowSurface != null) {
            mWindowSurface.release();
        }
    }

    @Override
    public void bindOffScreenFrameBuffer() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mOffScreenFrameBuffer[0]);
    }

    @Override
    public void attachOffScreenTexture(int textureId) {
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, textureId, 0);
    }

    @Override
    public void bindDefaultFrameBuffer() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void createOffScreenFrameBuffer() {
        GLES20.glGenFramebuffers(mOffScreenFrameBuffer.length, mOffScreenFrameBuffer, 0);
    }

    private void createOffScreenTextures() {
        GLES20.glGenTextures(mOffScreenTextureIds.length, mOffScreenTextureIds, 0);
        for (int mTextureId : mOffScreenTextureIds) {
            // bind to fbo texture cause we are going to do setting.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, getWidth(), getHeight(),
                    0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
            // 设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            // 设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            // 设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            // 设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            // unbind fbo texture.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    private void deleteOffScreenFrameBuffer() {
        GLES20.glDeleteFramebuffers(mOffScreenFrameBuffer.length, mOffScreenFrameBuffer, 0);
    }
}
