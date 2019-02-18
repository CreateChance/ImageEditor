package com.createchance.imageeditor;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

import com.createchance.imageeditor.gles.EglCore;
import com.createchance.imageeditor.gles.WindowSurface;
import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditor.utils.UiThreadUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.concurrent.Semaphore;

/**
 * Render target to save image.
 *
 * @author createchance
 * @date 2018/12/27
 */
public class ImageSaver implements IRenderTarget, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "ImageSaver";

    private int[] mOffScreenFrameBuffer = new int[2];
    private int[] mOffScreenTextureIds = new int[2];
    private int[] mSaveTextureId = new int[1];
    private int mInputTextureIndex = 0, mOutputTextureIndex = 1;

    private WindowSurface mWindowSurface;

    private int mSurfaceWidth, mSurfaceHeight;

    private int mSaveX, mSaveY, mSaveWidth, mSaveHeight;
    private int mSaveFormat = IEManager.IMG_FORMAT_JPEG, mSaveQuality = 100;

    private File mOutputFile;
    private SaveListener mListener;

    public ImageSaver(int surfaceWidth,
                      int surfaceHeight,
                      int saveX,
                      int saveY,
                      int saveWidth,
                      int saveHeight,
                      int saveFormat,
                      int saveQuality,
                      File outputFile,
                      SaveListener saveListener) {
        mSurfaceWidth = surfaceWidth;
        mSurfaceHeight = surfaceHeight;
        mSaveX = saveX;
        mSaveY = saveY;
        mSaveWidth = saveWidth;
        mSaveHeight = saveHeight;
        mSaveFormat = saveFormat;
        mSaveQuality = saveQuality;
        mOutputFile = outputFile;
        mListener = saveListener;
    }

    @Override
    public void init(EglCore eglCore) {
        createOffScreenFrameBuffer();
        createOffScreenTextures();
        createSaveTexture();
        bindDefaultFrameBuffer();
        attachOffScreenTexture(mSaveTextureId[0]);
        SurfaceTexture surfaceTexture = new SurfaceTexture(mSaveTextureId[0]);
        surfaceTexture.setOnFrameAvailableListener(this);
        mWindowSurface = new WindowSurface(eglCore, surfaceTexture);
        mWindowSurface.makeCurrent();
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
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mOffScreenFrameBuffer[1]);
    }

    @Override
    public int getSurfaceWidth() {
        return mSurfaceWidth;
    }

    @Override
    public int getSurfaceHeight() {
        return mSurfaceHeight;
    }

    @Override
    public void swapTexture() {
        int tmp = mInputTextureIndex;
        mInputTextureIndex = mOutputTextureIndex;
        mOutputTextureIndex = tmp;
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
    public void release() {
        deleteOffScreenFrameBuffer();
        GLES20.glDeleteTextures(mOffScreenTextureIds.length, mOffScreenTextureIds, 0);
        if (mWindowSurface != null) {
            mWindowSurface.release();
        }
    }

    private void createOffScreenFrameBuffer() {
        GLES20.glGenFramebuffers(mOffScreenFrameBuffer.length, mOffScreenFrameBuffer, 0);
    }

    private void createOffScreenTextures() {
        GLES20.glGenTextures(mOffScreenTextureIds.length, mOffScreenTextureIds, 0);
        for (int mTextureId : mOffScreenTextureIds) {
            // bind to fbo texture cause we are going to do setting.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mSurfaceWidth, mSurfaceHeight,
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

    private void createSaveTexture() {
        GLES20.glGenTextures(mSaveTextureId.length, mSaveTextureId, 0);
        for (int mTextureId : mSaveTextureId) {
            // bind to texture cause we are going to do setting.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mSurfaceWidth, mSurfaceHeight,
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

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // save image file here.
        Logger.d(TAG, "Save image, onFrameAvailable, we are going to save it, width: " + mSurfaceWidth +
                ", height: " + mSurfaceHeight +
                ", output file: " + mOutputFile.getAbsolutePath());

        final Semaphore waiter = new Semaphore(0);

        // Take picture on OpenGL thread
        final int[] pixelMirroredArray = new int[mSaveWidth * mSaveHeight];
        final IntBuffer pixelBuffer = IntBuffer.allocate(mSaveWidth * mSaveHeight);
        GLES20.glReadPixels(mSaveX,
                mSaveY,
                mSaveWidth,
                mSaveHeight,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                pixelBuffer);
        int[] pixelArray = pixelBuffer.array();

        // Convert upside down mirror-reversed image to right-side up normal image.
        for (int i = 0; i < mSaveHeight; i++) {
            for (int j = 0; j < mSaveWidth; j++) {
                pixelMirroredArray[(mSaveHeight - i - 1) * mSaveWidth + j] =
                        pixelArray[i * mSaveWidth + j];
            }
        }

        waiter.release();
        try {
            waiter.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            UiThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onSaveFailed();
                    }
                }
            });
            return;
        }

        Bitmap bitmap = Bitmap.createBitmap(mSaveWidth, mSaveHeight, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(pixelMirroredArray));
        saveBitmap(bitmap, mOutputFile);

    }

    private void saveBitmap(Bitmap bitmap, final File picFile) {
        try {
            FileOutputStream out = new FileOutputStream(picFile);
            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
            switch (mSaveFormat) {
                case IEManager.IMG_FORMAT_PNG:
                    format = Bitmap.CompressFormat.PNG;
                    break;
                case IEManager.IMG_FORMAT_JPEG:
                    format = Bitmap.CompressFormat.JPEG;
                    break;
                case IEManager.IMG_FORMAT_WEBP:
                    format = Bitmap.CompressFormat.WEBP;
                    break;
                default:
                    break;
            }
            bitmap.compress(format, mSaveQuality, out);
            out.flush();
            out.close();
            UiThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onSaved(picFile);
                    }
                }
            });
            // save done, release resource now.
            release();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            UiThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onSaveFailed();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            UiThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onSaveFailed();
                    }
                }
            });
        }
    }
}
