package com.createchance.imageeditor;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.GLES20;
import android.os.Build;
import android.view.Surface;

import com.createchance.imageeditor.gles.EglCore;
import com.createchance.imageeditor.gles.WindowSurface;
import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditor.utils.UiThreadUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Video saver, save images as video.
 *
 * @author createchance
 * @date 2018/12/30
 */
public class VideoSaver implements IRenderTarget {
    private static final String TAG = "VideoSaver";

    private File mOutputFile;
    private int mSurfaceWidth, mSurfaceHeight;
    private int mOrientation;

    private int[] mOffScreenFrameBuffer = new int[1];
    private int[] mOffScreenTextureIds = new int[2];
    private int mInputTextureIndex = 0, mOutputTextureIndex = 1;

    private WindowSurface mWindowSurface;
    private MediaMuxer mMuxer;
    private MediaCodec mEncoder;
    private int mVideoTrackId = -1;
    private boolean mRequestStop;
    private boolean mNeedDelete;
    private SaveListener mListener;

    private int mBitRate = 3000000;
    private int mFrameRate = 30;

    private SaverThread mSaveThread;

    public VideoSaver(int width, int height, int orientation, File outputFile, SaveListener listener) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        mOrientation = orientation;
        mOutputFile = outputFile;
        mListener = listener;
    }

    @Override
    public void init(EglCore eglCore) {
        prepare(eglCore);
        mWindowSurface.makeCurrent();
        createOffScreenFrameBuffer();
        createOffScreenTextures();
        beginSave();
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
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
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
        mRequestStop = true;
        deleteOffScreenFrameBuffer();
        if (mWindowSurface != null) {
            mWindowSurface.release();
        }
    }

    public void setBitrate(int bitrate) {
        mBitRate = bitrate;
    }

    public void setFrameRate(int frameRate) {
        mFrameRate = frameRate;
    }

    private void prepare(EglCore eglCore) {
        // init video format
        MediaFormat videoFormat = MediaFormat.createVideoFormat("video/avc", mSurfaceWidth, mSurfaceHeight);
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate);
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, mFrameRate);
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

        try {
            // init encoder
            mEncoder = MediaCodec.createEncoderByType("video/avc");
            mEncoder.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            Surface inputSurface = mEncoder.createInputSurface();
            mWindowSurface = new WindowSurface(eglCore, inputSurface, true);
            mEncoder.start();
            mSaveThread = new SaverThread();
        } catch (Exception e) {
            e.printStackTrace();
            if (mEncoder != null) {
                mEncoder.stop();
            }
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

    private void beginSave() {
        if (mOutputFile != null) {
            // init muxer
            try {
                mMuxer = new MediaMuxer(mOutputFile.getAbsolutePath(),
                        MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                mMuxer.setOrientationHint(mOrientation);
                mSaveThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                if (mMuxer != null) {
                    mMuxer.release();
                }
            }
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

    private void deleteOffScreenFrameBuffer() {
        GLES20.glDeleteFramebuffers(mOffScreenFrameBuffer.length, mOffScreenFrameBuffer, 0);
    }

    private class SaverThread extends Thread {

        private final long TIME_OUT = 5000;

        @Override
        public void run() {
//            if (mListener != null) {
//                UiThreadUtil.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mListener.onStarted(mOutputFile);
//                    }
//                });
//            }
            doMux();
            release();
            if (mNeedDelete) {
                mNeedDelete = false;
                if (mOutputFile != null) {
                    mOutputFile.delete();
                }
            }
            Logger.d(TAG, "Save worker done.");
        }

        private void doMux() {
            int outputBufferId;
            final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            ByteBuffer buffer;
            long framePts = 1000 * 1000 / mFrameRate;
            long nowPts = 0;
            while (true) {
                if (mRequestStop) {
                    mRequestStop = false;
                    Logger.d(TAG, "Request stop, so we are stopping.");
                    break;
                }

                outputBufferId = mEncoder.dequeueOutputBuffer(bufferInfo, TIME_OUT);
                if (outputBufferId >= 0) {
                    if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        Logger.d(TAG, "Reach video eos.");
                        mEncoder.signalEndOfInputStream();
                        break;
                    }

                    if (Build.VERSION.SDK_INT >= 21) {
                        buffer = mEncoder.getOutputBuffer(outputBufferId);
                    } else {
                        buffer = mEncoder.getOutputBuffers()[outputBufferId];
                    }

                    bufferInfo.presentationTimeUs = nowPts;
                    nowPts += framePts;
                    Logger.i(TAG, "doMux..........., pts: " + bufferInfo.presentationTimeUs);
                    mMuxer.writeSampleData(mVideoTrackId, buffer, bufferInfo);
                    mEncoder.releaseOutputBuffer(outputBufferId, false);
//                    if (mListener != null) {
//                        UiThreadUtil.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mListener.onSaveGoing(bufferInfo.presentationTimeUs, mOutputFile);
//                            }
//                        });
//                    }
                } else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    MediaFormat videoFormat = mEncoder.getOutputFormat();
                    Logger.d(TAG, "Encode format: " + videoFormat);
                    mVideoTrackId = mMuxer.addTrack(videoFormat);
                    mMuxer.start();
                }
            }

            Logger.d(TAG, "Mux done!");
        }

        private void release() {
            if (mEncoder != null) {
                mEncoder.stop();
            }
            if (mMuxer != null) {
                if (mVideoTrackId != -1) {
                    mMuxer.stop();
                }
                mMuxer.release();
            }
            UiThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onSaved(mOutputFile);
                    }
                }
            });
        }
    }
}
