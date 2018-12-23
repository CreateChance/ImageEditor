package com.createchance.imageeditordemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditordemo.model.Clip;
import com.createchance.imageeditordemo.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate video by image.
 *
 * @author createchance
 * @date 2018-12-23
 */
public class VideoGenerateActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "VideoGenerateActivity";

    private static final String EXTRA_IMAGE_PATH_LIST = "image path list";

    private final int REQUEST_CHOOSE_BGM = 100;

    private static boolean mIsStarted;

    private List<String> mImagePathList;
    private List<Clip> mClipList = new ArrayList<>();

    // views
    private TextureView mVwPreview;
    private ImageView mIvPlayControl;
    private TextView mTvCurBgmFile, mTvTime;
    private HorizontalThumbnailListView mVwThumbnailList;

    private String mSelectAudioFilePath;

    private HorizontalThumbnailListView.ImageGroupListener mImageGroupListener = new HorizontalThumbnailListView.ImageGroupListener() {
        @Override
        public void onImageGroupLeftShrink(int index, float leftProgress) {
            super.onImageGroupLeftShrink(index, leftProgress);

            Log.d(TAG, "onImageGroupLeftShrink: " + index + ", left progress: " + leftProgress);
        }

        @Override
        public void onImageGroupLeftExpand(int index, float leftProgress) {
            super.onImageGroupLeftExpand(index, leftProgress);

            Log.d(TAG, "onImageGroupLeftExpand: " + index + ", left progress: " + leftProgress);
        }

        @Override
        public void onImageGroupRightShrink(int index, float rightProgress) {
            super.onImageGroupRightShrink(index, rightProgress);

            Log.d(TAG, "onImageGroupRightShrink: " + index + ", right progress: " + rightProgress);
        }

        @Override
        public void onImageGroupRightExpand(int index, float rightProgress) {
            super.onImageGroupRightExpand(index, rightProgress);

            Log.d(TAG, "onImageGroupRightExpand: " + index + ", right progress: " + rightProgress);
        }

        @Override
        public void onImageGroupStart(int index, boolean isFromUser) {
            super.onImageGroupStart(index, isFromUser);

            Log.d(TAG, "onImageGroupStart: " + index + ", is from user: " + isFromUser);
        }

        @Override
        public void onImageGroupProcess(int index, float progress, boolean isFromUser) {
            super.onImageGroupProcess(index, progress, isFromUser);

            Log.d(TAG, "onImageGroupProcess: " + index + ", progress: " + progress + ", is from user: " + isFromUser);
        }

        @Override
        public void onImageGroupEnd(int index, boolean isFromUser) {
            super.onImageGroupEnd(index, isFromUser);

            Log.d(TAG, "onImageGroupEnd: " + index + ", is from user: " + isFromUser);
        }
    };

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

        initClipList();

        // init views.
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_done).setOnClickListener(this);
        mVwPreview = findViewById(R.id.vw_preview);
        mIvPlayControl = findViewById(R.id.iv_play_control);
        mIvPlayControl.setOnClickListener(this);
        mVwThumbnailList = findViewById(R.id.vw_thumbnail_list);
        mTvCurBgmFile = findViewById(R.id.tv_current_bgm_file);
        mTvTime = findViewById(R.id.tv_time);
        findViewById(R.id.btn_add_bgm).setOnClickListener(this);

        initThumbnailList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CHOOSE_BGM) {
            Uri uri = data.getData();
            if (uri == null) {
                Logger.e(TAG, "Bgm choose failed cause we can not get uri, request code: " + requestCode);
                Toast.makeText(this, "Audio select failed", Toast.LENGTH_SHORT).show();
                return;
            }
            mSelectAudioFilePath = uri.getPath();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsStarted = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                onBackPressed();
                break;
            case R.id.btn_done:
                // real generate video here.
                break;
            case R.id.iv_play_control:
                // play or pause it.
                break;
            case R.id.btn_add_bgm:
                // choose audio file here.
                chooseAudioFile();
                break;
            default:
                break;
        }
    }

    private void chooseAudioFile() {
        Intent intent = new Intent();
        intent.setType("audio/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CHOOSE_BGM);
    }

    private void initClipList() {
        for (String imagePath : mImagePathList) {
            Bitmap image = BitmapFactory.decodeFile(imagePath);
            if (image != null) {
                // todo: 3000ms default.
                mClipList.add(new Clip(image, 3000));
            } else {
                Logger.e(TAG, "Image decode failed: " + imagePath);
                Toast.makeText(this, "Image decode failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initThumbnailList() {
        int imageSize = DensityUtil.dip2px(this, 40);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int groupPadding = DensityUtil.dip2px(this, 8);
        mVwThumbnailList.setImageHeight(imageSize);
        mVwThumbnailList.setImageWidth(imageSize);
        mVwThumbnailList.setStartPaddingWidth(screenWidth / 2 - groupPadding);
        mVwThumbnailList.setEndPaddingWidth(screenWidth / 2 - groupPadding);
        mVwThumbnailList.setGroupPaddingWidth(groupPadding);
        mVwThumbnailList.setPaddingVerticalHeight(DensityUtil.dip2px(this, 2));
        mVwThumbnailList.setSelectedGroupBg(getResources().getDrawable(R.drawable.shape_solid_white_corner_10));
        mVwThumbnailList.setImageGroupListener(mImageGroupListener);
        // todo: one thumbnail stands for 1000ms default.
        for (Clip clip : mClipList) {
            List<HorizontalThumbnailListView.ImageItem> imageItemList = new ArrayList<>();
            imageItemList.add(new HorizontalThumbnailListView.ImageItem(clip.bitmap, imageSize, 0, imageSize));
            imageItemList.add(new HorizontalThumbnailListView.ImageItem(clip.bitmap, imageSize, 0, imageSize));
            imageItemList.add(new HorizontalThumbnailListView.ImageItem(clip.bitmap, imageSize, 0, imageSize));
            mVwThumbnailList.newImageGroup(imageItemList);
        }
    }
}
