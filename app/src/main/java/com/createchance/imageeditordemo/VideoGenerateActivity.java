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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.IEPreviewView;
import com.createchance.imageeditor.SaveListener;
import com.createchance.imageeditor.transitions.AbstractTransition;
import com.createchance.imageeditor.transitions.AngularTransition;
import com.createchance.imageeditor.transitions.BounceTransition;
import com.createchance.imageeditor.transitions.BowTieHorizontalTransition;
import com.createchance.imageeditor.transitions.BowTieVerticalTransition;
import com.createchance.imageeditor.transitions.BurnTransition;
import com.createchance.imageeditor.transitions.ButterflyWaveScrawlerTransition;
import com.createchance.imageeditor.transitions.CannabisLeafTransition;
import com.createchance.imageeditor.transitions.CircleCropTransition;
import com.createchance.imageeditor.transitions.CircleOpenTransition;
import com.createchance.imageeditor.transitions.CircleTransition;
import com.createchance.imageeditor.transitions.ColorDistanceTransition;
import com.createchance.imageeditor.transitions.ColorPhaseTransition;
import com.createchance.imageeditor.transitions.CrazyParametricFunTransition;
import com.createchance.imageeditor.transitions.CrossHatchTransition;
import com.createchance.imageeditor.transitions.CrossWarpTransition;
import com.createchance.imageeditor.transitions.CrossZoomTransition;
import com.createchance.imageeditor.transitions.CubeTransition;
import com.createchance.imageeditor.transitions.DirectionalTransition;
import com.createchance.imageeditor.transitions.DirectionalWarpTransition;
import com.createchance.imageeditor.transitions.DirectionalWipeTransition;
import com.createchance.imageeditor.transitions.DoomScreenTransition;
import com.createchance.imageeditor.transitions.DoorWayTransition;
import com.createchance.imageeditor.transitions.DreamyTransition;
import com.createchance.imageeditor.transitions.DreamyZoomTransition;
import com.createchance.imageeditor.transitions.FadeColorTransition;
import com.createchance.imageeditor.transitions.FadeGrayScaleTransition;
import com.createchance.imageeditor.transitions.FadeTransition;
import com.createchance.imageeditor.transitions.FlyEyeTransition;
import com.createchance.imageeditor.transitions.GlitchDisplaceTransition;
import com.createchance.imageeditor.transitions.GlitchMemoriesTransition;
import com.createchance.imageeditor.transitions.GridFlipTransition;
import com.createchance.imageeditor.transitions.HeartTransition;
import com.createchance.imageeditor.transitions.HexagonalTransition;
import com.createchance.imageeditor.transitions.InvertedPageCurlTransition;
import com.createchance.imageeditor.transitions.KaleidoScopeTransition;
import com.createchance.imageeditor.transitions.LinearBlurTransition;
import com.createchance.imageeditor.transitions.LuminanceMeltTransition;
import com.createchance.imageeditor.transitions.MorphTransition;
import com.createchance.imageeditor.transitions.MosaicTransition;
import com.createchance.imageeditor.transitions.MultiplyBlendTransition;
import com.createchance.imageeditor.transitions.PerlinTransition;
import com.createchance.imageeditor.transitions.PinWheelTransition;
import com.createchance.imageeditor.transitions.PixelizeTransition;
import com.createchance.imageeditor.transitions.PolarFunctionTransition;
import com.createchance.imageeditor.transitions.PolkaDotsCurtainTransition;
import com.createchance.imageeditor.transitions.RadialTransition;
import com.createchance.imageeditor.transitions.RandomSquaresTransition;
import com.createchance.imageeditor.transitions.RippleTransition;
import com.createchance.imageeditor.transitions.RotateScaleFadeTransition;
import com.createchance.imageeditor.transitions.SimpleZoomTransition;
import com.createchance.imageeditor.transitions.SquaresWireTransition;
import com.createchance.imageeditor.transitions.SqueezeTransition;
import com.createchance.imageeditor.transitions.StereoViewerTransition;
import com.createchance.imageeditor.transitions.SwapTransition;
import com.createchance.imageeditor.transitions.SwirlTransition;
import com.createchance.imageeditor.transitions.UndulatingBurnOutTransition;
import com.createchance.imageeditor.transitions.WaterDropTransition;
import com.createchance.imageeditor.transitions.WindTransition;
import com.createchance.imageeditor.transitions.WindowBlindsTransition;
import com.createchance.imageeditor.transitions.WindowSliceTransition;
import com.createchance.imageeditor.transitions.WipeDownTransition;
import com.createchance.imageeditor.transitions.WipeLeftTransition;
import com.createchance.imageeditor.transitions.WipeRightTransition;
import com.createchance.imageeditor.transitions.WipeUpTransition;
import com.createchance.imageeditor.transitions.ZoomInCirclesTransition;
import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditordemo.model.Transition;
import com.createchance.imageeditordemo.utils.AssetsUtil;
import com.createchance.imageeditordemo.utils.DensityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private List<Transition> mTransitionList;

    private TransitionSelectWindow mVwTransitionSelect;

    // views
    private IEPreviewView mVwPreview;
    private ImageView mIvPlayControl;
    private TextView mTvCurBgmFile, mTvTime, mTvTransition;
    private HorizontalThumbnailListView mVwThumbnailList;

    private String mSelectAudioFilePath;

    private int mCurClipIndex = 0;

    private long mTotalDuration;

    private HorizontalThumbnailListView.ImageGroupListener mImageGroupListener = new HorizontalThumbnailListView.ImageGroupListener() {
        @Override
        public void onImageGroupProcess(int index, float progress, boolean isFromUser) {
            super.onImageGroupProcess(index, progress, isFromUser);

            mCurClipIndex = index;

            Log.d(TAG, "onImageGroupProcess: " + index + ", progress: " + progress + ", is from user: " + isFromUser);
            long position = (long) (progress * mTotalDuration);
            IEManager.getInstance().seek((long) (IEManager.getInstance().getTotalDuration() * progress));
            mTvTime.setText(String.format(Locale.getDefault(), "%02d:%02d", position / (60 * 1000), (position / 1000) % 60));
            mTvTransition.setText(mVwThumbnailList.getCurImageGroup().getStringExtra());

        }

        @Override
        public void onImageGroupClicked(int index) {
            super.onImageGroupClicked(index);
            Logger.d(TAG, "onImageGroupClicked, index: " + index);
            showTransitionSelection();
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
        mTvTransition = findViewById(R.id.tv_transition);
        findViewById(R.id.btn_add_bgm).setOnClickListener(this);

        initThumbnailList();

        initTransitionList();

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
            mTvCurBgmFile.setText(mSelectAudioFilePath);
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
                        10000,
                        new SaveListener() {
                            @Override
                            public void onSaveFailed() {
                                Toast.makeText(VideoGenerateActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Log.d(TAG, "onSaveFailed: " + Thread.currentThread().getName());
                            }

                            @Override
                            public void onSaveProgress(float progress) {
                                super.onSaveProgress(progress);
                                dialog.setProgress(progress);
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
            mTotalDuration += 3000;
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
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void initTransitionList() {
        mTransitionList = AssetsUtil.parseJsonToList(this, "transitions/transition_list.json", Transition.class);
    }

    private void showTransitionSelection() {
        if (mVwTransitionSelect == null) {
            mVwTransitionSelect = new TransitionSelectWindow(this, mTransitionList, new TransitionSelectWindow.TransitionSelectListener() {
                @Override
                public void onTransitionSelected(Transition transition) {
                    Logger.d(TAG, "Transition selected: " + transition);

                    IEManager.getInstance().setTransition(mCurClipIndex, getTransitionById(transition.mId), 2000, false);
                    mVwThumbnailList.getCurImageGroup().setStringExtra(transition.mName);
                    mTvTransition.setText(transition.mName);
                }
            });
            mVwTransitionSelect.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                }
            });
        }
        mVwTransitionSelect.showAtLocation(findViewById(R.id.vw_root),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        // set dark background color.
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
    }

    private AbstractTransition getTransitionById(int id) {
        AbstractTransition transition = null;

        switch (id) {
            case 0:
                transition = new WindowSliceTransition();
                break;
            case 1:
                transition = new InvertedPageCurlTransition();
                break;
            case 2:
                transition = new AngularTransition();
                break;
            case 3:
                transition = new BounceTransition();
                break;
            case 4:
                transition = new BowTieHorizontalTransition();
                break;
            case 5:
                transition = new BowTieVerticalTransition();
                break;
            case 6:
                transition = new BurnTransition();
                break;
            case 7:
                transition = new ButterflyWaveScrawlerTransition();
                break;
            case 8:
                transition = new CannabisLeafTransition();
                break;
            case 9:
                transition = new CircleTransition();
                break;
            case 10:
                transition = new CircleCropTransition();
                break;
            case 11:
                transition = new CircleOpenTransition();
                break;
            case 12:
                transition = new ColorPhaseTransition();
                break;
            case 13:
                transition = new ColorDistanceTransition();
                break;
            case 14:
                transition = new CrazyParametricFunTransition();
                break;
            case 15:
                transition = new CrossHatchTransition();
                break;
            case 16:
                transition = new CrossWarpTransition();
                break;
            case 17:
                transition = new CrossZoomTransition();
                break;
            case 18:
                transition = new CubeTransition();
                break;
            case 19:
                transition = new DirectionalTransition();
                break;
            case 20:
                transition = new DirectionalWarpTransition();
                break;
            case 21:
                transition = new DirectionalWipeTransition();
                break;
            case 22:
                transition = new DoomScreenTransition();
                break;
            case 23:
                transition = new DoorWayTransition();
                break;
            case 24:
                transition = new DreamyTransition();
                break;
            case 25:
                transition = new DreamyZoomTransition();
                break;
            case 26:
                transition = new FadeTransition();
                break;
            case 27:
                transition = new FadeColorTransition();
                break;
            case 28:
                transition = new FadeGrayScaleTransition();
                break;
            case 29:
                transition = new FlyEyeTransition();
                break;
            case 30:
                transition = new GlitchDisplaceTransition();
                break;
            case 31:
                transition = new GlitchMemoriesTransition();
                break;
            case 32:
                transition = new GridFlipTransition();
                break;
            case 33:
                transition = new HeartTransition();
                break;
            case 34:
                transition = new HexagonalTransition();
                break;
            case 35:
                transition = new KaleidoScopeTransition();
                break;
            case 36:
                transition = new LinearBlurTransition();
                break;
            case 37:
                transition = new LuminanceMeltTransition();
                break;
            case 38:
                transition = new MorphTransition();
                break;
            case 39:
                transition = new MosaicTransition();
                break;
            case 40:
                transition = new MultiplyBlendTransition();
                break;
            case 41:
                transition = new PerlinTransition();
                break;
            case 42:
                transition = new PinWheelTransition();
                break;
            case 43:
                transition = new PixelizeTransition();
                break;
            case 44:
                transition = new PolarFunctionTransition();
                break;
            case 45:
                transition = new PolkaDotsCurtainTransition();
                break;
            case 46:
                transition = new RadialTransition();
                break;
            case 47:
                transition = new RandomSquaresTransition();
                break;
            case 48:
                transition = new RippleTransition();
                break;
            case 49:
                transition = new RotateScaleFadeTransition();
                break;
            case 50:
                transition = new SimpleZoomTransition();
                break;
            case 51:
                transition = new SquaresWireTransition();
                break;
            case 52:
                transition = new SqueezeTransition();
                break;
            case 53:
                transition = new StereoViewerTransition();
                break;
            case 54:
                transition = new SwapTransition();
                break;
            case 55:
                transition = new SwirlTransition();
                break;
            case 56:
                transition = new UndulatingBurnOutTransition();
                break;
            case 57:
                transition = new WaterDropTransition();
                break;
            case 58:
                transition = new WindTransition();
                break;
            case 59:
                transition = new WindowBlindsTransition();
                break;
            case 60:
                transition = new WipeDownTransition();
                break;
            case 61:
                transition = new WipeUpTransition();
                break;
            case 62:
                transition = new WipeLeftTransition();
                break;
            case 63:
                transition = new WipeRightTransition();
                break;
            case 64:
                transition = new ZoomInCirclesTransition();
                break;
            default:
                break;
        }

        return transition;
    }
}
