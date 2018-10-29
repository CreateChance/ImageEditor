package com.createchance.imageeditordemo;

import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.SaveListener;
import com.createchance.imageeditor.ops.BaseImageOperator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextureView textureView = findViewById(R.id.vw_texture);
        textureView.setSurfaceTextureListener(this);
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
        switch (v.getId()) {
            case R.id.btn_test:
                TextOperator textOperator = new TextOperator.Builder()
                        .text("你好你好")
                        .position(mCurPosX, mCurPosY)
                        .size(100)
                        .font(new File(getFilesDir(), "fonts/KaBuQiNuo.otf").getAbsolutePath())
                        .color(0.2f, 0.3f, 0.5f)
                        .build();
                IEManager.getInstance().addOperator(textOperator);
                mCurPosX += 50;
                mCurPosY -= 50;
                break;
            case R.id.btn_undo:

                break;
            case R.id.btn_redo:

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
