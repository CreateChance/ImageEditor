package com.createchance.imageeditor;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import com.createchance.imageeditor.gles.EglCore;
import com.createchance.imageeditor.gles.WindowSurface;
import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditor.utils.UiThreadUtil;

import java.io.File;
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
    private File mVideoFile, mAudioFile;
    private File mBgmFile;
    private long mBgmStartTime;
    private int mSurfaceWidth, mSurfaceHeight;
    private int mOrientation;

    private int[] mOffScreenFrameBuffer = new int[1];
    private int[] mOffScreenTextureIds = new int[2];
    private int mInputTextureIndex = 0, mOutputTextureIndex = 1;

    private final long mVideoDuration;

    private WindowSurface mWindowSurface;
    private MediaCodec mVideoEncoder;
    private int mVideoTrackId = -1, mAudioTrackId = -1;
    private boolean mRequestStop;
    private SaveListener mListener;

    private int mBitRate = 10000000;
    private int mFrameRate = 25;

    private SaverThread mSaveThread;

    public VideoSaver(int width,
                      int height,
                      int orientation,
                      long videoDurationMs,
                      File outputFile,
                      File bgmFile,
                      long bgmStartTime,
                      SaveListener listener) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        mOrientation = orientation;
        mVideoDuration = videoDurationMs * 1000;
        mOutputFile = outputFile;
        mVideoFile = new File(outputFile.getParent(), "video_track_only.mp4");
        mAudioFile = new File(outputFile.getParent(), "audio_track_only.aac");
        mBgmFile = bgmFile;
        mBgmStartTime = bgmStartTime;
        mListener = listener;
    }

    @Override
    public void init(EglCore eglCore) {
        prepare(eglCore);
        mWindowSurface.makeCurrent();
        createOffScreenFrameBuffer();
        createOffScreenTextures();
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
        try {
            // init video format
            MediaFormat videoFormat = MediaFormat.createVideoFormat("video/avc", mSurfaceWidth, mSurfaceHeight);
            videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate);
            videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, mFrameRate);
            videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            // init audio format
            MediaFormat audioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", 44100, 2);
            audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, 96000);
            audioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 512 * 1024);
            audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);

            // init video encoder
            mVideoEncoder = MediaCodec.createEncoderByType("video/avc");
            mVideoEncoder.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            Surface inputSurface = mVideoEncoder.createInputSurface();
            mWindowSurface = new WindowSurface(eglCore, inputSurface, true);
            mVideoEncoder.start();

            // init saver thread.
            mSaveThread = new SaverThread();
            mSaveThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (mVideoEncoder != null) {
                mVideoEncoder.stop();
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
            if (mBgmFile != null) {
                if (doMuxVideo()) {
                    doMuxAudio();
                }
            } else {
                doMuxVideo();
            }
        }

        private boolean doMuxVideo() {
            int outputBufferId;
            final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            ByteBuffer buffer;
            long framePts = 1000 * 1000 / mFrameRate;
            long nowPts = 0;
            MediaMuxer muxer = null;
            int tempVideoTrackId = -1;
            try {
                muxer = new MediaMuxer(mVideoFile.getAbsolutePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                while (true) {
                    if (mRequestStop) {
                        mRequestStop = false;
                        Logger.d(TAG, "Request stop, so we are stopping.");
                        break;
                    }

                    outputBufferId = mVideoEncoder.dequeueOutputBuffer(bufferInfo, TIME_OUT);
                    if (outputBufferId >= 0) {
                        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            Logger.d(TAG, "Reach video eos.");
                            mVideoEncoder.signalEndOfInputStream();
                            break;
                        }

                        if (Build.VERSION.SDK_INT >= 21) {
                            buffer = mVideoEncoder.getOutputBuffer(outputBufferId);
                        } else {
                            buffer = mVideoEncoder.getOutputBuffers()[outputBufferId];
                        }

                        bufferInfo.presentationTimeUs = nowPts;
                        nowPts += framePts;
                        Logger.i(TAG, "doMux..........., pts: " + bufferInfo.presentationTimeUs);
                        if (mListener != null) {
                            UiThreadUtil.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mBgmFile == null) {
                                        mListener.onSaveProgress(bufferInfo.presentationTimeUs * 1.0f / mVideoDuration);
                                    } else {
                                        mListener.onSaveProgress(bufferInfo.presentationTimeUs * 0.5f / mVideoDuration);
                                    }
                                }
                            });
                        }
                        muxer.writeSampleData(tempVideoTrackId, buffer, bufferInfo);
                        mVideoEncoder.releaseOutputBuffer(outputBufferId, false);
//                    if (mListener != null) {
//                        UiThreadUtil.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mListener.onSaveGoing(bufferInfo.presentationTimeUs, mOutputFile);
//                            }
//                        });
//                    }
                    } else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        MediaFormat videoFormat = mVideoEncoder.getOutputFormat();
                        Logger.d(TAG, "Video encode output format: " + videoFormat);
                        tempVideoTrackId = muxer.addTrack(videoFormat);
                        muxer.start();
                    }
                }

                Logger.d(TAG, "Mux video done!");
                if (mBgmFile == null) {
                    UiThreadUtil.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onSaved(mOutputFile);
                            }
                        }
                    });
                    // rename output file.
                    mVideoFile.renameTo(mOutputFile);
                    Logger.d(TAG, "Save worker done.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(TAG, "Save worker failed.");
                UiThreadUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.onSaveFailed();
                        }
                    }
                });
                return false;
            } finally {
                if (muxer != null) {
                    muxer.stop();
                    muxer.release();
                }
                if (mVideoEncoder != null) {
                    mVideoEncoder.stop();
                    mVideoEncoder.release();
                }
            }

            return true;
        }

        private void doMuxAudio() {
            AudioTransCoder audioTransCoder = new AudioTransCoder.Builder()
                    .transcode(mBgmFile)
                    .from(mBgmStartTime)
                    .duration(mVideoDuration / 1000)
                    .saveAs(mAudioFile)
                    .build();
            audioTransCoder.start(new AudioTransCoder.Callback() {
                @Override
                public void onProgress(final float progress) {
                    Log.d(TAG, "onProgress: " + progress);
                    if (mListener != null) {
                        UiThreadUtil.post(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onSaveProgress(0.5f + progress * 0.5f);
                            }
                        });
                    }
                }

                @Override
                public void onSucceed(File output) {
                    Log.d(TAG, "onSucceed: ");
                    mergeFile();
                }

                @Override
                public void onFailed() {
                    Log.d(TAG, "onFailed: ");
                    Logger.e(TAG, "Save worker failed.");
                    UiThreadUtil.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onSaveFailed();
                            }
                        }
                    });

                    // delete temp files.
                    if (mVideoFile.exists()) {
                        mVideoFile.delete();
                    }
                    if (mAudioFile.exists()) {
                        mAudioFile.delete();
                    }
                }
            });
        }

        private void mergeFile() {
            MediaMuxer muxer = null;
            MediaExtractor videoExtractor = null, audioExtractor = null;
            ByteBuffer buffer = ByteBuffer.allocate(512 * 1024);
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            try {
                muxer = new MediaMuxer(mOutputFile.getAbsolutePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

                // load video media format.
                videoExtractor = new MediaExtractor();
                videoExtractor.setDataSource(mVideoFile.getAbsolutePath());
                mVideoTrackId = muxer.addTrack(videoExtractor.getTrackFormat(0));
                // load audio media format.
                audioExtractor = new MediaExtractor();
                audioExtractor.setDataSource(mAudioFile.getAbsolutePath());
                mAudioTrackId = muxer.addTrack(audioExtractor.getTrackFormat(0));

                // start muxer
                muxer.start();

                // read video first.
                videoExtractor.selectTrack(0);
                while (true) {
                    int sampleSize = videoExtractor.readSampleData(buffer, 0);
                    if (sampleSize != -1) {
                        bufferInfo.size = sampleSize;
                        bufferInfo.flags = videoExtractor.getSampleFlags();
                        bufferInfo.offset = 0;
                        bufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                        muxer.writeSampleData(mVideoTrackId, buffer, bufferInfo);
                        videoExtractor.advance();
                    } else {
                        Logger.d(TAG, "Read video done.");
                        break;
                    }
                }

                // clear buffer
                buffer.clear();

                // handle audio then
                audioExtractor.selectTrack(0);
                while (true) {
                    int sampleSize = audioExtractor.readSampleData(buffer, 0);
                    if (sampleSize != -1) {
                        bufferInfo.size = sampleSize;
                        bufferInfo.flags = audioExtractor.getSampleFlags();
                        bufferInfo.offset = 0;
                        bufferInfo.presentationTimeUs = audioExtractor.getSampleTime();
                        muxer.writeSampleData(mAudioTrackId, buffer, bufferInfo);
                        audioExtractor.advance();
                    } else {
                        Logger.d(TAG, "Read video done.");
                        break;
                    }
                }

                UiThreadUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.onSaved(mOutputFile);
                        }
                    }
                });
                Logger.d(TAG, "Save worker done.");
            } catch (Exception e) {
                e.printStackTrace();
                UiThreadUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.onSaveFailed();
                        }
                    }
                });
            } finally {
                if (videoExtractor != null) {
                    videoExtractor.release();
                }
                if (audioExtractor != null) {
                    audioExtractor.release();
                }
                if (muxer != null) {
                    muxer.stop();
                    muxer.release();
                }

                // delete temp files.
                if (mVideoFile.exists()) {
                    mVideoFile.delete();
                }
                if (mAudioFile.exists()) {
                    mAudioFile.delete();
                }
            }
        }
    }
}
