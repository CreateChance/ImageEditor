package com.createchance.imageeditordemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.createchance.imageeditor.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate video by image.
 *
 * @author createchance
 * @date 2018-12-23
 */
public class VideoGenerateActivity extends AppCompatActivity {

    private static final String TAG = "VideoGenerateActivity";

    private static final String EXTRA_IMAGE_PATH_LIST = "image path list";

    private static boolean mIsStarted;

    private List<String> mImagePathList;

    public static void start(Context context, List<String> imagePathList) {
        if (mIsStarted) {
            Logger.d(TAG, "VideoGenerateActivity started already, no need!");
            return;
        }

        mIsStarted = true;

        ArrayList<String> copiedList = new ArrayList<>(imagePathList);
        Intent intent = new Intent(context, VideoGenerateActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGE_PATH_LIST, copiedList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_generate);

        Intent intent = getIntent();
        if (intent != null) {
            mImagePathList = intent.getStringArrayListExtra(EXTRA_IMAGE_PATH_LIST);
        }

        if (mImagePathList == null || mImagePathList.size() == 0) {
            Logger.e(TAG, "Image path list can not be null or empty!");
            Toast.makeText(this, "Start failed!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // for debug
        for (String path : mImagePathList) {
            Logger.d(TAG, "Image path: " + path);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsStarted = false;
    }
}
