package com.createchance.imageeditordemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.createchance.imageeditordemo.model.SimpleModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Main page
 *
 * @author createchance
 * @date 2018-11-03
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;

    private RecyclerView mWorkListView;
    private WorkListAdapter mWorkListAdapter;
    private List<WorkListAdapter.WorkItem> mWorkList = new ArrayList<>();

    private File mImageFromCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBaseDir();

        mWorkListView = findViewById(R.id.rcv_work_list);
        mWorkListAdapter = new WorkListAdapter(this, mWorkList, new WorkListAdapter.OnWorkSelectListener() {
            @Override
            public void onWorkSelected(WorkListAdapter.WorkItem workItem) {

            }
        });
        mWorkListView.setLayoutManager(new LinearLayoutManager(this));
        mWorkListView.setAdapter(mWorkListAdapter);
        findViewById(R.id.tv_choose_photo).setOnClickListener(this);
        findViewById(R.id.tv_take_photo).setOnClickListener(this);

        WorkRunner.addTaskToBackground(new Runnable() {
            @Override
            public void run() {
                tryCopyFontFile();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        initWorkList();
        mWorkListAdapter.refresh(mWorkList);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap image = BitmapFactory.decodeStream(inputStream);
                        if (image != null) {
                            SimpleModel.getInstance().putImage(image);
                            ImageEditActivity.start(MainActivity.this);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap image = BitmapFactory.decodeFile(mImageFromCamera.getAbsolutePath());
                    if (image != null) {
                        SimpleModel.getInstance().putImage(image);
                        ImageEditActivity.start(MainActivity.this);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void checkBaseDir() {
        if (!Constants.mBaseDir.exists()) {
            Constants.mBaseDir.mkdir();
        }
    }

    private void initWorkList() {
        if (!Constants.mBaseDir.exists()) {
            Log.e(TAG, "initWorkList, but base dir not existed!!");
            return;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        mWorkList = new ArrayList<>();
        File[] works = Constants.mBaseDir.listFiles();
        for (File work : works) {
            WorkListAdapter.WorkItem workItem = new WorkListAdapter.WorkItem();
            workItem.mImage = work;
            workItem.mSize = work.length();
            workItem.mTimeStamp = work.lastModified();
            BitmapFactory.decodeFile(work.getAbsolutePath(), options);
            workItem.mWidth = options.outWidth;
            workItem.mHeight = options.outHeight;
            mWorkList.add(workItem);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_photo:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
                break;
            case R.id.tv_take_photo:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this,
                                    "com.createchance.imageeditordemo.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        }
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                }
                break;
            default:
                break;
        }
    }

    private void tryCopyFontFile() {
        File fontDir = new File(getFilesDir(), "fonts");
        if (fontDir.exists()) {
            return;
        }
        fontDir.mkdir();

        InputStream is = null;
        OutputStream os = null;
        List<String> fontList = new ArrayList<>();
        fontList.add("Hanyi_Senty_Yongle_Encyclopedia.ttf");
        fontList.add("HanyiSentyDiary.ttf");
        fontList.add("HanyiSentyJournal.ttf");
        fontList.add("HanyiSentyLingfeiScroll.ttf");
        fontList.add("KaBuQiNuo.otf");
        fontList.add("MFYanSong-Regular.ttf");
        fontList.add("SentyChalk.ttf");
        fontList.add("SentyTEA.ttf");
        fontList.add("SentyWEN2017.ttf");
        fontList.add("SentyZHAO-20180827.ttf");
        fontList.add("YouLangRuanBi.ttf");
        try {
            for (String font : fontList) {
                is = getAssets().open("fonts/" + font);
                os = new FileOutputStream(new File(fontDir, font));
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                is.close();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mImageFromCamera = image;
        return image;
    }
}
