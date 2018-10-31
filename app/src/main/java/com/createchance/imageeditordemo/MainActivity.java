package com.createchance.imageeditordemo;

import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.SaveListener;
import com.createchance.imageeditor.ops.BaseImageOperator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private RecyclerView mEditListView;
    private EditListAdapter mEditListAdapter;

    private GestureDetector mGestureDetector;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);

            return true;
        }
    };

    private ViewGroup mEditPanelContainer;
    private EditTextPanel mTextPanel;

    private int mTextureHeight;

    private int mCurrentPanel = -1;

    private int mLastX, mLastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditPanelContainer = findViewById(R.id.vw_edit_panel_container);
        findViewById(R.id.tv_undo).setOnClickListener(this);
        findViewById(R.id.tv_redo).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        TextureView textureView = findViewById(R.id.vw_texture);
        textureView.setSurfaceTextureListener(this);
        textureView.setOnTouchListener(mTouchListener);
        mEditListView = findViewById(R.id.rcv_edit_list);
        mEditListView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mEditListAdapter = new EditListAdapter(this, new EditListAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d(TAG, "onItemClicked: " + position);
                mCurrentPanel = position;
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:
                        mTextPanel.show();
                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    default:
                        break;
                }
            }
        });
        mEditListView.setAdapter(mEditListAdapter);

        WorkRunner.addTaskToBackground(new Runnable() {
            @Override
            public void run() {
                tryCopyFontFile();
            }
        });

        mGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                mLastX = (int) e.getX();
                mLastY = (int) e.getY();
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                switch (mCurrentPanel) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:
                        mTextPanel.moveText((int) (e2.getX() - mLastX), (int) (e2.getY() - mLastY));
                        mLastX = (int) e2.getX();
                        mLastY = (int) e2.getY();
                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    default:
                        break;
                }

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }
        });
        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(TAG, "onSingleTapConfirmed: ");
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d(TAG, "onDoubleTap: ");

                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.d(TAG, "onDoubleTapEvent: ");
                return true;
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mTextureHeight = height;
        mTextPanel = new EditTextPanel(this, mEditPanelContainer, mTextureHeight);
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
            case R.id.tv_undo:
                IEManager.getInstance().undo();
                break;
            case R.id.tv_redo:
                IEManager.getInstance().redo();
                break;
            case R.id.tv_save:
                IEManager.getInstance().save(new File(Environment.getExternalStorageDirectory(), "avflow/output.png"),
                        new SaveListener() {
                            @Override
                            public void onSaveFailed() {
                                Toast.makeText(MainActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSaved(File target) {
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
}
