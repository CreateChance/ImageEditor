package com.createchance.imageeditor.utils;

import android.media.MediaMetadataRetriever;

import java.io.File;

/**
 * Video util
 *
 * @author createchance
 * @date 2018-04-15
 */
public class VideoUtil {
    public static int getVideoRotation(File video) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(video.getAbsolutePath());
        int rotation = Integer.valueOf(
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        retriever.release();
        return rotation;
    }

    public static long getVideoDuration(File video) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(video.getAbsolutePath());
        long duration = Long.valueOf(
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        retriever.release();
        return duration;
    }
}
