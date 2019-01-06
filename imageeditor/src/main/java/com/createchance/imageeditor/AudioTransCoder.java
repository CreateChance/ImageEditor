package com.createchance.imageeditor;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.createchance.imageeditor.utils.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Transcode mpeg audio file to aac file with 4 working thread.
 *
 * @author createchance
 * @date 25/03/2018
 */

class AudioTransCoder {

    private static final String TAG = "AudioTransCoder";

    private BlockingQueue<RawBuffer> mRawQueue = new LinkedBlockingQueue<>(10);

    private File mInputFile;
    private File mOutputFile;
    private long mStartPosMs;
    private long mDurationMs;
    private MediaCodec decoder, encoder;

    private Callback mCallback;

    void start(Callback callback) {
        Logger.d(TAG, "Audio trans code started, start pos: " + mStartPosMs + ", duration: " + mDurationMs);
        mCallback = callback;
        if (checkRational()) {
            DecodeInputWorker decodeWorker = new DecodeInputWorker();
            decodeWorker.start();
        } else {
            if (mCallback != null) {
                mCallback.onFailed();
            }
        }
    }

    private boolean checkRational() {
        return mInputFile != null &&
                mInputFile.exists() &&
                mInputFile.isFile() &&
                mStartPosMs >= 0 &&
                mDurationMs >= 0;

    }

    public static class Builder {
        private AudioTransCoder transCodeAction = new AudioTransCoder();

        public Builder transcode(File input) {
            transCodeAction.mInputFile = input;

            return this;
        }

        public Builder from(long fromMs) {
            transCodeAction.mStartPosMs = fromMs;

            return this;
        }

        public Builder duration(long durationMs) {
            transCodeAction.mDurationMs = durationMs;

            return this;
        }

        public Builder saveAs(File output) {
            transCodeAction.mOutputFile = output;

            return this;
        }

        public AudioTransCoder build() {
            return transCodeAction;
        }
    }

    private class DecodeInputWorker extends Thread {
        private final long TIME_OUT = 5000;
        private MediaExtractor extractor;

        @Override
        public void run() {
            try {
                prepare();

                // start decode output worker
                DecodeOutputWorker decoderOutputWorker = new DecodeOutputWorker();
                decoderOutputWorker.start();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    decodeInput21();
                } else {
                    decodeInput20();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                release();
            }

            Log.d(TAG, "Decode input worker done.");
        }

        private void release() {
            if (extractor != null) {
                extractor.release();
                extractor = null;
            }
        }


        private void prepare() throws IOException {
            extractor = new MediaExtractor();
            extractor.setDataSource(mInputFile.getAbsolutePath());
            int numTracks = extractor.getTrackCount();
            for (int i = 0; i < numTracks; i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mine = format.getString(MediaFormat.KEY_MIME);
                if (!TextUtils.isEmpty(mine) && mine.startsWith("audio")) {
                    extractor.selectTrack(i);
                    if (mDurationMs == 0) {
                        try {
                            mDurationMs = format.getLong(MediaFormat.KEY_DURATION) / 1000;
                        } catch (Exception e) {
                            e.printStackTrace();
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(mInputFile.getAbsolutePath());
                            mediaPlayer.prepare();
                            mDurationMs = mediaPlayer.getDuration();
                            mediaPlayer.release();
                        }
                    }

                    if (mDurationMs == 0) {
                        throw new IllegalStateException("We can not get duration info from input file: " + mInputFile);
                    }

                    decoder = MediaCodec.createDecoderByType(mine);
                    decoder.configure(format, null, null, 0);
                    decoder.start();
                    break;
                }
            }
        }

        @TargetApi(20)
        private void decodeInput20() {
            ByteBuffer[] inputBuffers = decoder.getInputBuffers();
            extractor.seekTo(mStartPosMs * 1000, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            boolean isEOS = false;
            while (true) {
                long timestamp = 0;
                if (!isEOS) {
                    int inIndex = decoder.dequeueInputBuffer(TIME_OUT);
                    if (inIndex >= 0) {
                        ByteBuffer buffer = inputBuffers[inIndex];
                        int sampleSize = extractor.readSampleData(buffer, 0);
                        timestamp = extractor.getSampleTime();
                        if (timestamp > (mStartPosMs + mDurationMs) * 1000) {
                            sampleSize = -1;
                        }
                        if (sampleSize <= 0) {
                            Logger.d(TAG, "Decode input reach eos.");
                            decoder.queueInputBuffer(
                                    inIndex,
                                    0,
                                    0,
                                    timestamp,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            isEOS = true;
                        } else {
                            decoder.queueInputBuffer(
                                    inIndex,
                                    0,
                                    sampleSize,
                                    timestamp,
                                    0);
                            extractor.advance();
                        }
                    }
                } else {
                    break;
                }

            }

            Logger.d(TAG, "decode done!");
        }

        @TargetApi(21)
        private void decodeInput21() {
            extractor.seekTo(mStartPosMs * 1000, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            long timeStamp;
            while (true) {
                int inputBufferId = decoder.dequeueInputBuffer(TIME_OUT);
                if (inputBufferId >= 0) {
                    ByteBuffer byteBuffer = decoder.getInputBuffer(inputBufferId);
                    int sampleSize = extractor.readSampleData(byteBuffer, 0);
                    timeStamp = extractor.getSampleTime();
                    if (timeStamp > (mStartPosMs + mDurationMs) * 1000) {
                        sampleSize = -1;
                    }

                    if (sampleSize <= 0) {
                        Logger.d(TAG, "Decode input reach eos.");
                        decoder.queueInputBuffer(
                                inputBufferId,
                                0,
                                0,
                                timeStamp,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        break;
                    } else {
                        decoder.queueInputBuffer(
                                inputBufferId,
                                0,
                                sampleSize,
                                timeStamp,
                                0);
                        extractor.advance();
                    }
                }
            }
        }
    }

    private class DecodeOutputWorker extends Thread {
        private final long TIME_OUT = 5000;
        ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

        @Override
        public void run() {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    decodeOutput21();
                } else {
                    decodeOutput20();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (mCallback != null) {
                    mCallback.onFailed();
                }
            } finally {
                release();
            }

            Logger.d(TAG, "Decode output worker done.");
        }

        @TargetApi(20)
        private void decodeOutput20() throws InterruptedException {
            while (true) {
                int outIndex = decoder.dequeueOutputBuffer(info, TIME_OUT);
                switch (outIndex) {
                    case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                        Log.d(TAG, "decodeOutput20: INFO_OUTPUT_BUFFERS_CHANGED");
                        outputBuffers = decoder.getOutputBuffers();
                        break;
                    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                        Log.d(TAG, "decodeOutput20: INFO_OUTPUT_FORMAT_CHANGED");
                        MediaFormat mf = decoder.getOutputFormat();
                        // start encode worker
                        EncodeInputWorker encodeTask = new EncodeInputWorker();
                        int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                        int channelCount = mf.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                        encodeTask.setAudioParams(sampleRate, channelCount);
                        encodeTask.start();
                        break;
                    case MediaCodec.INFO_TRY_AGAIN_LATER:
                        Log.d(TAG, "dequeueOutputBuffer timed out!");
                        break;
                    default:
                        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            mRawQueue.put(new RawBuffer(null, true, info.presentationTimeUs));
                        } else {
                            ByteBuffer buffer = outputBuffers[outIndex];
                            byte[] outData = new byte[info.size];
                            buffer.get(outData, 0, info.size);
                            mRawQueue.put(new RawBuffer(outData, false, info.presentationTimeUs));
                        }
                        decoder.releaseOutputBuffer(outIndex, false);
                        break;
                }
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.d(TAG, "Decode output reach eos.");
                    break;
                }
            }
        }

        @TargetApi(21)
        private void decodeOutput21() throws InterruptedException {
            while (true) {
                int outputBufferId = decoder.dequeueOutputBuffer(info, TIME_OUT);
                switch (outputBufferId) {
                    case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                        Log.d(TAG, "decodeOutput20: INFO_OUTPUT_BUFFERS_CHANGED");
                        break;
                    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                        Log.d(TAG, "decodeOutput20: INFO_OUTPUT_FORMAT_CHANGED");
                        MediaFormat mf = decoder.getOutputFormat();
                        // start encode worker
                        EncodeInputWorker encodeTask = new EncodeInputWorker();
                        int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                        int channelCount = mf.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                        encodeTask.setAudioParams(sampleRate, channelCount);
                        encodeTask.start();
                        break;
                    case MediaCodec.INFO_TRY_AGAIN_LATER:
                        Log.d(TAG, "dequeueOutputBuffer timed out!");
                        break;
                    default:
                        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            mRawQueue.put(new RawBuffer(null, true, info.presentationTimeUs));
                        } else {
                            ByteBuffer buffer = decoder.getOutputBuffer(outputBufferId);
                            byte[] outData = new byte[info.size];
                            buffer.get(outData, 0, info.size);
                            mRawQueue.put(new RawBuffer(outData, false, info.presentationTimeUs));
                        }
                        decoder.releaseOutputBuffer(outputBufferId, false);
                        break;
                }
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.d(TAG, "Decode output reach eos.");
                    break;
                }
            }
        }

        private void release() {
            if (decoder != null) {
                decoder.stop();
                decoder.release();
                decoder = null;
            }
        }
    }

    private class EncodeInputWorker extends Thread {
        private final long TIME_OUT = 5000;
        private int sampleRate;
        private int channelCount;

        public void setAudioParams(int sampleRate, int channelCount) {
            this.sampleRate = sampleRate;
            this.channelCount = channelCount;
        }

        @Override
        public void run() {
            try {
                prepare();

                // start encode output worker
                EncodeOutputWorker encodeOutputWorker = new EncodeOutputWorker();
                encodeOutputWorker.start();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    encodeInput21();
                } else {
                    encodeInput20();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (mCallback != null) {
                    mCallback.onFailed();
                }
            } finally {
                release();
            }

            Log.d(TAG, "Encode input worker done.");
        }

        private void prepare() throws IOException {
            encoder = MediaCodec.createEncoderByType("audio/mp4a-latm");
            MediaFormat format = MediaFormat.createAudioFormat("audio/mp4a-latm", sampleRate, channelCount);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 96000);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 512 * 1024);
            format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            encoder.start();
        }

        @TargetApi(20)
        private void encodeInput20() throws InterruptedException {
            boolean decodeDone = false;
            ByteBuffer[] inputBuffers = encoder.getInputBuffers();

            while (true) {
                if (!decodeDone) {
                    int inputBufferId = encoder.dequeueInputBuffer(TIME_OUT);
                    if (inputBufferId >= 0) {
                        RawBuffer rawBuffer = mRawQueue.take();
                        if (rawBuffer.isLast) {
                            Logger.d(TAG, "Encode input reach eos.");
                            decodeDone = true;
                            encoder.queueInputBuffer(
                                    inputBufferId,
                                    0,
                                    0,
                                    rawBuffer.sampleTime,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                            );
                        } else {
                            ByteBuffer inputBuffer = inputBuffers[inputBufferId];
                            inputBuffer.clear();
                            inputBuffer.put(rawBuffer.data);
                            encoder.queueInputBuffer(
                                    inputBufferId,
                                    0,
                                    rawBuffer.data.length,
                                    rawBuffer.sampleTime,
                                    0);
                        }
                    }
                } else {
                    break;
                }
            }

            Log.d(TAG, "encode done!");
        }

        @TargetApi(21)
        private void encodeInput21() throws InterruptedException {
            boolean decodeDone = false;

            while (true) {
                if (!decodeDone) {
                    int inputBufferId = encoder.dequeueInputBuffer(TIME_OUT);
                    if (inputBufferId >= 0) {
                        RawBuffer rawBuffer = mRawQueue.take();
                        if (rawBuffer.isLast) {
                            Logger.d(TAG, "Encode input reach eos.");
                            decodeDone = true;
                            encoder.queueInputBuffer(
                                    inputBufferId,
                                    0,
                                    0,
                                    rawBuffer.sampleTime,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                            );
                        } else {
                            ByteBuffer inputBuffer = encoder.getInputBuffer(inputBufferId);
                            inputBuffer.clear();
                            inputBuffer.put(rawBuffer.data);
                            encoder.queueInputBuffer(
                                    inputBufferId,
                                    0,
                                    rawBuffer.data.length,
                                    rawBuffer.sampleTime,
                                    0);
                        }
                    }
                } else {
                    break;
                }
            }
        }

        private void release() {
        }
    }

    private class EncodeOutputWorker extends Thread {
        private final long TIME_OUT = 5000;
        ByteBuffer[] outputBuffers = encoder.getOutputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        private OutputStream mOutput;

        @Override
        public void run() {
            try {
                prepare();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    encodeOutput21();
                } else {
                    encodeOutput20();
                }
                if (mCallback != null) {
                    mCallback.onSucceed(mOutputFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (mCallback != null) {
                    mCallback.onFailed();
                }
            } finally {
                release();
            }

            Log.d(TAG, "Encode output worker done.");
        }

        private void prepare() throws FileNotFoundException {
            mOutput = new DataOutputStream(new FileOutputStream(mOutputFile));
        }

        @TargetApi(20)
        private void encodeOutput20() throws IOException {
            while (true) {
                int outIndex = encoder.dequeueOutputBuffer(info, TIME_OUT);
                if (outIndex >= 0) {
                    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        break;
                    }
                    ByteBuffer outputBuffer = outputBuffers[outIndex];
                    int len = info.size + 7;
                    byte[] outData = new byte[len];
                    addADTStoPacket(outData, len);
                    outputBuffer.get(outData, 7, info.size);
                    encoder.releaseOutputBuffer(outIndex, false);
                    mOutput.write(outData);
                    if (mCallback != null) {
                        mCallback.onProgress((info.presentationTimeUs - mStartPosMs * 1000) * 1f /
                                (mDurationMs * 1000));
                    }
                    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        Log.d(TAG, "Encode output reach eos.");
                        if (mCallback != null) {
                            mCallback.onProgress(1f);
                        }
                        break;
                    }
                }
            }
        }

        @TargetApi(21)
        private void encodeOutput21() throws IOException {
            while (true) {
                int outIndex = encoder.dequeueOutputBuffer(info, TIME_OUT);
                if (outIndex >= 0) {
                    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        break;
                    }
                    ByteBuffer outputBuffer = encoder.getOutputBuffer(outIndex);
                    int len = info.size + 7;
                    byte[] outData = new byte[len];
                    addADTStoPacket(outData, len);
                    outputBuffer.get(outData, 7, info.size);
                    encoder.releaseOutputBuffer(outIndex, false);
                    mOutput.write(outData);
                    if (mCallback != null) {
                        mCallback.onProgress((info.presentationTimeUs - mStartPosMs * 1000) * 1f /
                                (mDurationMs * 1000));
                    }
                    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        Log.d(TAG, "Encode output reach eos.");
                        if (mCallback != null) {
                            mCallback.onProgress(1f);
                        }
                        break;
                    }
                }
            }
        }

        private void release() {
            if (encoder != null) {
                encoder.stop();
                encoder.release();
                encoder = null;
            }
            if (mOutput != null) {
                try {
                    mOutput.flush();
                    mOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mOutput = null;
            }
        }

        /**
         * 给编码出的aac裸流添加adts头字段
         *
         * @param packet    要空出前7个字节，否则会搞乱数据
         * @param packetLen
         */
        private void addADTStoPacket(byte[] packet, int packetLen) {
            // AAC LC
            int profile = 2;
            // 44.1KHz
            int freqIdx = 4;
            // CPE
            int chanCfg = 2;
            packet[0] = (byte) 0xFF;
            packet[1] = (byte) 0xF9;
            packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
            packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
            packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
            packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
            packet[6] = (byte) 0xFC;
        }
    }

    private class RawBuffer {
        byte[] data;
        boolean isLast;
        long sampleTime;

        private RawBuffer(byte[] data, boolean isLast, long sampleTime) {
            this.data = data;
            this.isLast = isLast;
            this.sampleTime = sampleTime;
        }

        @Override
        public String toString() {
            return "RawBuffer{" +
                    "data=" + Arrays.toString(data) +
                    ", isLast=" + isLast +
                    '}';
        }
    }

    interface Callback {
        void onProgress(float progress);

        void onSucceed(File output);

        void onFailed();
    }
}
