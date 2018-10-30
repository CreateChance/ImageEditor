package com.createchance.imageeditordemo;

import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.SaveListener;
import com.createchance.imageeditor.filters.GPUImageFilterGroup;
import com.createchance.imageeditor.filters.GPUImageGaussianBlurFilter;
import com.createchance.imageeditor.filters.GPUImageGlassSphereFilter;
import com.createchance.imageeditor.filters.GPUImageLookupFilter;
import com.createchance.imageeditor.filters.GPUImageSobelEdgeDetection;
import com.createchance.imageeditor.filters.GPUImageSwirlFilter;
import com.createchance.imageeditor.ops.BaseImageOperator;
import com.createchance.imageeditor.ops.FilterOperator;
import com.createchance.imageeditor.ops.TextOperator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private int mCurPosX = 0, mCurPosY = 1000;

    private GestureDetector mGestureDetector;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextureView textureView = findViewById(R.id.vw_texture);
        textureView.setSurfaceTextureListener(this);
        textureView.setOnTouchListener(mTouchListener);
        findViewById(R.id.btn_test).setOnClickListener(this);
        findViewById(R.id.btn_undo).setOnClickListener(this);
        findViewById(R.id.btn_redo).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);

        WorkRunner.addTaskToBackground(new Runnable() {
            @Override
            public void run() {
                tryCopyFontFile();
            }
        });

        mGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(TAG, "onSingleTapConfirmed: ");
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d(TAG, "onDoubleTap: ");

                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.d(TAG, "onDoubleTapEvent: ");
                return false;
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface holdSurface = new Surface(surface);
        IEManager.getInstance().prepare(holdSurface, width, height);

        BaseImageOperator baseImageOperator = new BaseImageOperator.Builder()
                .image(BitmapFactory.decodeResource(getResources(), R.drawable.test))
                .build();

        IEManager.getInstance().addOperator(baseImageOperator);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        IEManager.getInstance().stop();

        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onClick(View v) {
        GPUImageLookupFilter lookupFilter;
        switch (v.getId()) {
            case R.id.btn_test:
                lookupFilter = new GPUImageLookupFilter();
                try {
                    lookupFilter.setBitmap(BitmapFactory.decodeStream(getAssets().open("filters/nt-9-B1.png")));
                    lookupFilter.setIntensity(1.0f);

                    GPUImageSwirlFilter swirlFilter = new GPUImageSwirlFilter();
                    swirlFilter.setAngle(0.5f);

                    GPUImageGlassSphereFilter glassSphereFilter = new GPUImageGlassSphereFilter();
                    glassSphereFilter.setRadius(0.3f);

                    GPUImageGaussianBlurFilter gaussianBlurFilter = new GPUImageGaussianBlurFilter();
                    gaussianBlurFilter.setBlurSize(0.9f);

                    GPUImageSobelEdgeDetection sobelEdgeDetection = new GPUImageSobelEdgeDetection();

                    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
                    filterGroup.addFilter(lookupFilter);
                    filterGroup.addFilter(swirlFilter);

                    FilterOperator filterOperator = new FilterOperator.Builder()
                            .filter(gaussianBlurFilter)
                            .build();
                    IEManager.getInstance().addOperator(filterOperator);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_undo:
                TextOperator textOperator = new TextOperator.Builder()
                        .text("你好你好")
                        .position(mCurPosX, mCurPosY)
                        .size(100)
                        .font(new File(getFilesDir(), "fonts/SentyWEN2017.ttf").getAbsolutePath())
                        .color(0.2f, 0.3f, 0.4f)
                        .build();
                IEManager.getInstance().addOperator(textOperator);
                mCurPosX += 50;
                mCurPosY -= 50;
                break;
            case R.id.btn_redo:
                lookupFilter = new GPUImageLookupFilter();
                try {
                    lookupFilter.setBitmap(BitmapFactory.decodeStream(getAssets().open("filters/nt-9-B1.png")));
                    lookupFilter.setIntensity(1.0f);
                    FilterOperator filterOperator = new FilterOperator.Builder()
                            .filter(lookupFilter)
                            .build();
                    IEManager.getInstance().addOperator(filterOperator);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_save:
                IEManager.getInstance().save(new File(Environment.getExternalStorageDirectory(), "avflow/output.png"), new SaveListener() {
                    @Override
                    public void onSaveFailed() {
                        Toast.makeText(MainActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSaved(File target) {
                        Log.d(TAG, "onSaved: " + target);
                        Toast.makeText(MainActivity.this, "Save succeed!", Toast.LENGTH_SHORT).show();
                    }
                });
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
        fontList.add("KaBuQiNuo.otf");
        fontList.add("MFYanSong-Regular.ttf");
        fontList.add("SentyWEN2017.ttf");
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
}
