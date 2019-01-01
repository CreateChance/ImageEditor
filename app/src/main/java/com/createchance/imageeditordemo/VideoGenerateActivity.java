package com.createchance.imageeditordemo;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.IEPreviewView;
import com.createchance.imageeditor.SaveListener;
import com.createchance.imageeditor.transitions.AbstractTransition;
import com.createchance.imageeditor.transitions.InvertedPageCurlTransition;
import com.createchance.imageeditor.transitions.SimpleZoomTransition;
import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditordemo.utils.DensityUtil;

import java.io.File;
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

    // views
    private IEPreviewView mVwPreview;
    private ImageView mIvPlayControl;
    private TextView mTvCurBgmFile, mTvTime;
    private HorizontalThumbnailListView mVwThumbnailList;

    private String mSelectAudioFilePath;

    private HorizontalThumbnailListView.ImageGroupListener mImageGroupListener = new HorizontalThumbnailListView.ImageGroupListener() {
        @Override
        public void onImageGroupLeftShrink(int index, float leftProgress, boolean isLast) {
            super.onImageGroupLeftShrink(index, leftProgress, isLast);

            Log.d(TAG, "onImageGroupLeftShrink: " + index + ", left progress: " + leftProgress);
        }

        @Override
        public void onImageGroupLeftExpand(int index, float leftProgress, boolean isLast) {
            super.onImageGroupLeftExpand(index, leftProgress, isLast);

            Log.d(TAG, "onImageGroupLeftExpand: " + index + ", left progress: " + leftProgress);
        }

        @Override
        public void onImageGroupRightShrink(int index, float rightProgress, boolean isLast) {
            super.onImageGroupRightShrink(index, rightProgress, isLast);

            Log.d(TAG, "onImageGroupRightShrink: " + index + ", right progress: " + rightProgress);
        }

        @Override
        public void onImageGroupRightExpand(int index, float rightProgress, boolean isLast) {
            super.onImageGroupRightExpand(index, rightProgress, isLast);

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
            IEManager.getInstance().seek((long) (IEManager.getInstance().getTotalDuration() * progress));
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

        // init IE
        IEManager.getInstance().startEngine();
        IEManager.getInstance().attachPreview(mVwPreview);
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
            mSelectAudioFilePath = getAbsolutePath(this, uri);
            Logger.d(TAG, "Audio file selected: " + mSelectAudioFilePath);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsStarted = false;

        // release IE
        IEManager.getInstance().detachPreview(mVwPreview);
        IEManager.getInstance().stopEngine();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                onBackPressed();
                break;
            case R.id.btn_done:
                final OutputDialog dialog = OutputDialog.start(this);
                // real generate video here.
                IEManager.getInstance().saveAsVideo(
                        1080,
                        1920,
                        0,
                        new File(Constants.mBaseDir, System.currentTimeMillis() + ".mp4"),
                        TextUtils.isEmpty(mSelectAudioFilePath) ? null : new File(mSelectAudioFilePath),
                        new SaveListener() {
                            @Override
                            public void onSaveFailed() {
                                Toast.makeText(VideoGenerateActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Log.d(TAG, "onSaveFailed: " + Thread.currentThread().getName());
                            }

                            @Override
                            public void onSaved(File target) {
                                Toast.makeText(VideoGenerateActivity.this, "Save succeed!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Log.d(TAG, "onSaved: " + Thread.currentThread().getName() + ", file: " + target.getAbsolutePath());
                            }
                        }
                );
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
        for (int i = 0; i < mImagePathList.size(); i++) {
            IEManager.getInstance().addClip(mImagePathList.get(i), 3000);
            AbstractTransition transition;
            if (i % 2 == 0) {
                transition = new InvertedPageCurlTransition();
            } else {
                transition = new SimpleZoomTransition();
            }
            IEManager.getInstance().setTransition(i,
                    transition,
                    2000,
                    false);
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
        for (String imagePath : mImagePathList) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            List<HorizontalThumbnailListView.ImageItem> imageItemList = new ArrayList<>();
            imageItemList.add(new HorizontalThumbnailListView.ImageItem(bitmap, imageSize, 0, imageSize));
            imageItemList.add(new HorizontalThumbnailListView.ImageItem(bitmap, imageSize, 0, imageSize));
            imageItemList.add(new HorizontalThumbnailListView.ImageItem(bitmap, imageSize, 0, imageSize));
            mVwThumbnailList.newImageGroup(imageItemList);
        }
    }

    private String getAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
