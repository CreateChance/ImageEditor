package com.createchance.imageeditor;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
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

    private long mVideoDuration;

    private WindowSurface mWindowSurface;
    private MediaCodec mVideoEncoder;
    private MediaCodec mAudioDecoder, mAudioEncoder;
    private int mVideoTrackId = -1, mAudioTrackId = -1;
    private boolean mRequestStop;
    private SaveListener mListener;

    private int mBitRate = 10000000;
    private int mFrameRate = 30;

    private SaverThread mSaveThread;

    public VideoSaver(int width,
                      int height,
                      int orientation,
                      File outputFile,
                      File bgmFile,
                      long bgmStartTime,
                      SaveListener listener) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        mOrientation = orientation;
        mOutputFile = outputFile;
        mVideoFile = new File(outputFile.getParent(), "video_track_only.mp4");
        mAudioFile = new File(outputFile.getParent(), "audio_track_only.mp4");
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

            // init bgm decoder and encoder.
            if (mBgmFile != null) {
                MediaExtractor mediaExtractor = new MediaExtractor();
                mediaExtractor.setDataSource(mBgmFile.getAbsolutePath());
                for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
                    MediaFormat mediaFormat = mediaExtractor.getTrackFormat(i);
                    String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                    Logger.d(TAG, "Found bgm file track mime: " + mime);
                    if (mime.startsWith("audio")) {
                        Logger.d(TAG, "Found bgm audio track, track index: " + i);
                        // init audio decoder
                        mAudioDecoder = MediaCodec.createDecoderByType(mime);
                        mAudioDecoder.configure(mediaFormat, null, null, 0);
                        mAudioDecoder.start();
                        // init audio encoder
                        mAudioEncoder = MediaCodec.createEncoderByType("audio/mp4a-latm");
                        mAudioEncoder.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
                        mAudioEncoder.start();
                        break;
                    }
                }
                mediaExtractor.release();
            }

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

        private boolean isFinished = false;

        @Override
        public void run() {
            if (mBgmFile != null) {
                if (doMuxVideo() && doMuxAudio()) {
                    mergeFile();
                }
            } else {
                if (doMuxVideo()) {
                    mVideoFile.renameTo(mOutputFile);
                }
            }
            if (isFinished) {
                UiThreadUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.onSaved(mOutputFile);
                        }
                    }
                });
                // delete temp file.
                if (mVideoFile.exists()) {
                    mVideoFile.delete();
                }
                if (mAudioFile.exists()) {
                    mAudioFile.delete();
                }
                Logger.d(TAG, "Save worker done.");
            } else {
                Logger.e(TAG, "Save worker failed.");
                UiThreadUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.onSaveFailed();
                        }
                    }
                });
            }

            release();
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

                mVideoDuration = nowPts;
                Logger.d(TAG, "Mux video done!");
                if (mBgmFile == null) {
                    isFinished = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (muxer != null) {
                    muxer.stop();
                    muxer.release();
                }
            }

            return true;
        }

        private boolean doMuxAudio() {
            boolean readDone = false;
            boolean decodeDone = false;
            MediaCodec.BufferInfo decodeBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo encodeBufferInfo = new MediaCodec.BufferInfo();
            MediaMuxer muxer = null;
            int audioTrackId = -1;
            try {
                muxer = new MediaMuxer(mAudioFile.getAbsolutePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

                // handle audio then.
                MediaExtractor mediaExtractor = new MediaExtractor();
                mediaExtractor.setDataSource(mBgmFile.getAbsolutePath());
                int bgmAudioTrackIndex = -1;
                for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
                    MediaFormat mediaFormat = mediaExtractor.getTrackFormat(i);
                    String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                    if (mime.startsWith("audio")) {
                        Logger.d(TAG, "Found one audio track from bgm file, we will use this track as bgm.");
                        bgmAudioTrackIndex = i;
                        mediaExtractor.selectTrack(i);
                        break;
                    }
                }
                if (bgmAudioTrackIndex != -1) {
                    Logger.d(TAG, "Found audio track index: " + bgmAudioTrackIndex);
                    while (true) {
                        if (!readDone) {
                            int decodeInputIndex = mAudioDecoder.dequeueInputBuffer(TIME_OUT);
                            if (decodeInputIndex >= 0) {
                                ByteBuffer decodeInputBuffer = mAudioDecoder.getInputBuffers()[decodeInputIndex];
                                int sampleSize = mediaExtractor.readSampleData(decodeInputBuffer, 0);
                                if (sampleSize >= 0) {
                                    // send sample data to decoder
                                    long sampleTime = mediaExtractor.getSampleTime();
                                    Logger.d(TAG, "Sample time: " + sampleTime);
                                    if (sampleTime - mBgmStartTime * 1000 >= mVideoDuration) {
                                        Logger.d(TAG, "We reach end of bgm file.");
                                        mAudioDecoder.queueInputBuffer(
                                                decodeInputIndex,
                                                0,
                                                0,
                                                0,
                                                MediaCodec.BUFFER_FLAG_END_OF_STREAM
                                        );
                                        readDone = true;
                                    } else {
                                        mAudioDecoder.queueInputBuffer(
                                                decodeInputIndex,
                                                0,
                                                sampleSize,
                                                sampleTime,
                                                0
                                        );
                                    }

                                    // advance to next sample.
                                    mediaExtractor.advance();
                                } else if (sampleSize == -1) {
                                    Logger.d(TAG, "We reach end of bgm file.");
                                    mAudioDecoder.queueInputBuffer(
                                            decodeInputIndex,
                                            0,
                                            0,
                                            0,
                                            MediaCodec.BUFFER_FLAG_END_OF_STREAM
                                    );
                                    readDone = true;
                                }
                            } else {
                                Logger.e(TAG, "Get decoder's input buffer index failed!");
                            }

                            // read decoded audio data(pcm data), and then send it to encoder.
                            if (!decodeDone) {
                                // get encode input buffer
                                int encodeInputIndex = mAudioEncoder.dequeueInputBuffer(TIME_OUT);
                                if (encodeInputIndex >= 0) {
                                    while (true) {
                                        int decodeOutputIndex = mAudioDecoder.dequeueOutputBuffer(decodeBufferInfo, TIME_OUT);
                                        if (decodeOutputIndex >= 0) {
                                            if ((decodeBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) == 0) {
                                                // get decode output buffer
                                                ByteBuffer decodeOutputBuffer = mAudioDecoder.getOutputBuffers()[decodeOutputIndex];
                                                ByteBuffer encodeInputBuffer = mAudioEncoder.getInputBuffers()[encodeInputIndex];
                                                // put decoded buffer into encode buffer
                                                encodeInputBuffer.put(decodeOutputBuffer);
                                                // send data to encoder
                                                mAudioEncoder.queueInputBuffer(
                                                        encodeInputIndex,
                                                        0,
                                                        decodeBufferInfo.size,
                                                        decodeBufferInfo.presentationTimeUs,
                                                        0
                                                );
                                            } else {
                                                Logger.d(TAG, "This is the last decode output buffer!");
                                                decodeDone = true;
                                                mAudioEncoder.queueInputBuffer(
                                                        encodeInputIndex,
                                                        0,
                                                        0,
                                                        0,
                                                        MediaCodec.BUFFER_FLAG_END_OF_STREAM
                                                );
                                            }
                                            // release decoder output index.
                                            mAudioDecoder.releaseOutputBuffer(decodeOutputIndex, false);
                                        } else if (decodeOutputIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                                            Logger.d(TAG, "No more data in the decode output buffer, so try again later.");
                                            break;
                                        }
                                    }
                                } else {
                                    Logger.e(TAG, "Get encoder's input buffer index failed!");
                                }
                            }

                            // read encoded buffer from encoder, and mux to output file's audio track.
                            while (true) {
                                int encodeOutputIndex = mAudioEncoder.dequeueOutputBuffer(encodeBufferInfo, TIME_OUT);
                                if (encodeOutputIndex >= 0) {
                                    Logger.d(TAG, "Get encode output buffer index: " + encodeOutputIndex);
                                    ByteBuffer encodeOutputBuffer = mAudioEncoder.getOutputBuffers()[encodeOutputIndex];
                                    // mux buffer to output file.
                                    muxer.writeSampleData(audioTrackId, encodeOutputBuffer, encodeBufferInfo);
                                    mAudioEncoder.releaseOutputBuffer(encodeOutputIndex, false);
                                    if ((encodeBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                                        break;
                                    }
                                } else if (encodeOutputIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                                    Logger.d(TAG, "No more data in encode output buffer, so try again later!");
                                    break;
                                } else if (encodeOutputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                                    MediaFormat audioFormat = mAudioEncoder.getOutputFormat();
                                    audioTrackId = muxer.addTrack(audioFormat);
                                    muxer.start();
                                    Logger.d(TAG, "Audio encode output format: " + audioFormat);
                                }
                            }
                        } else {
                            Logger.d(TAG, "Bgm file read done, so skip!");
                        }

                        if ((encodeBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            Logger.d(TAG, "Mux audio done.");
                            break;
                        }
                    }
                } else {
                    Logger.e(TAG, "No audio track found int bgm file, so exit!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (muxer != null) {
                    muxer.stop();
                    muxer.release();
                }
            }

            return true;
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

                isFinished = true;
            } catch (Exception e) {
                e.printStackTrace();
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
            }
        }

        private void release() {
            if (mVideoEncoder != null) {
                mVideoEncoder.stop();
                mVideoEncoder.release();
            }
            if (mAudioDecoder != null) {
                mAudioDecoder.stop();
                mAudioDecoder.release();
            }
            if (mAudioEncoder != null) {
                mAudioEncoder.stop();
                mAudioEncoder.release();
            }
        }
    }
}
