package com.createchance.imageeditordemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.SaveListener;
import com.createchance.imageeditor.ops.BaseImageOperator;
import com.createchance.imageeditordemo.model.SimpleModel;
import com.createchance.imageeditordemo.panels.AbstractPanel;
import com.createchance.imageeditordemo.utils.DensityUtil;

import java.io.File;

public class ImageEditActivity extends AppCompatActivity implements
        TextureView.SurfaceTextureListener,
        View.OnClickListener,
        AbstractPanel.PanelListener {

    private static final String TAG = "ImageEditActivity";

    private RecyclerView mEditListView;
    private EditListAdapter mEditListAdapter;

    private BaseImageOperator mBaseOp;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        private int mLastX, mLastY;
        private int mDownX, mDownY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mCurrentPanel != null) {
                if (mCurrentPanel.getType() == AbstractPanel.TYPE_CUT) {
                    handleScissor(event);
                } else {
                    mCurrentPanel.onTouchEvent(event);
                }
            }

            return true;
        }

        private void handleScissor(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastX = (int) event.getX();
                    mLastY = (int) event.getY();
                    mDownX = mLastX;
                    mDownY = mLastY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mDownX < mVwPreview.getWidth() / 2 && mDownY < mVwPreview.getHeight() / 2) {
                        Log.d(TAG, "handleScissor, top left");
                        RelativeLayout.LayoutParams leftParams = (RelativeLayout.LayoutParams) mVwLeftScissor.getLayoutParams();
                        leftParams.width += event.getX() - mLastX;
                        if (leftParams.width + mVwRightScissor.getWidth() > mVwPreview.getWidth()) {
                            leftParams.width = mVwPreview.getWidth() - mVwRightScissor.getWidth();
                        } else if (leftParams.width < 0) {
                            leftParams.width = 0;
                        }
                        mVwLeftScissor.setLayoutParams(leftParams);

                        RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) mVwTopScissor.getLayoutParams();
                        topParams.height += event.getY() - mLastY;
                        if (topParams.height + mVwBottomScissor.getHeight() > mVwPreview.getHeight()) {
                            topParams.height = mVwPreview.getHeight() - mVwBottomScissor.getHeight();
                        } else if (topParams.height < 0) {
                            topParams.height = 0;
                        }
                        mVwTopScissor.setLayoutParams(topParams);
                    } else if (mDownX < mVwPreview.getWidth() / 2 && mDownY > mVwPreview.getHeight() / 2) {
                        Log.d(TAG, "handleScissor, bottom left");
                        RelativeLayout.LayoutParams leftParams = (RelativeLayout.LayoutParams) mVwLeftScissor.getLayoutParams();
                        leftParams.width += event.getX() - mLastX;
                        if (leftParams.width + mVwRightScissor.getWidth() > mVwPreview.getWidth()) {
                            leftParams.width = mVwPreview.getWidth() - mVwRightScissor.getWidth();
                        } else if (leftParams.width < 0) {
                            leftParams.width = 0;
                        }
                        mVwLeftScissor.setLayoutParams(leftParams);

                        RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) mVwBottomScissor.getLayoutParams();
                        bottomParams.height += mLastY - event.getY();
                        if (bottomParams.height + mVwTopScissor.getHeight() > mVwPreview.getHeight()) {
                            bottomParams.height = mVwPreview.getHeight() - mVwTopScissor.getHeight();
                        } else if (bottomParams.height < 0) {
                            bottomParams.height = 0;
                        }
                        mVwBottomScissor.setLayoutParams(bottomParams);
                    } else if (mDownX > mVwPreview.getWidth() / 2 && mDownY < mVwPreview.getHeight() / 2) {
                        Log.d(TAG, "handleScissor, top right");
                        RelativeLayout.LayoutParams rightParams = (RelativeLayout.LayoutParams) mVwRightScissor.getLayoutParams();
                        rightParams.width += mLastX - event.getX();
                        if (rightParams.width + mVwLeftScissor.getWidth() > mVwPreview.getWidth()) {
                            rightParams.width = mVwPreview.getWidth() - mVwLeftScissor.getWidth();
                        } else if (rightParams.width < 0) {
                            rightParams.width = 0;
                        }
                        mVwRightScissor.setLayoutParams(rightParams);

                        RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) mVwTopScissor.getLayoutParams();
                        topParams.height += event.getY() - mLastY;
                        if (topParams.height + mVwBottomScissor.getHeight() > mVwPreview.getHeight()) {
                            topParams.height = mVwPreview.getHeight() - mVwBottomScissor.getHeight();
                        } else if (topParams.height < 0) {
                            topParams.height = 0;
                        }
                        mVwTopScissor.setLayoutParams(topParams);
                    } else if (mDownX > mVwPreview.getWidth() / 2 && mDownY > mVwPreview.getHeight() / 2) {
                        Log.d(TAG, "handleScissor, bottom right");
                        RelativeLayout.LayoutParams rightParams = (RelativeLayout.LayoutParams) mVwRightScissor.getLayoutParams();
                        rightParams.width += mLastX - event.getX();
                        if (rightParams.width + mVwLeftScissor.getWidth() > mVwPreview.getWidth()) {
                            rightParams.width = mVwPreview.getWidth() - mVwLeftScissor.getWidth();
                        } else if (rightParams.width < 0) {
                            rightParams.width = 0;
                        }
                        mVwRightScissor.setLayoutParams(rightParams);

                        RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) mVwBottomScissor.getLayoutParams();
                        bottomParams.height += mLastY - event.getY();
                        if (bottomParams.height + mVwTopScissor.getHeight() > mVwPreview.getHeight()) {
                            bottomParams.height = mVwPreview.getHeight() - mVwTopScissor.getHeight();
                        } else if (bottomParams.height < 0) {
                            bottomParams.height = 0;
                        }
                        mVwBottomScissor.setLayoutParams(bottomParams);
                    }
                    mLastX = (int) event.getX();
                    mLastY = (int) event.getY();
                    break;
                default:
                    break;
            }
        }
    };

    private ViewGroup mEditPanelContainer;

    private int mTextureWidth, mTextureHeight;

    private AbstractPanel mCurrentPanel;

    private View mVwLeftScissor, mVwTopScissor, mVwRightScissor, mVwBottomScissor;

    private TextureView mVwPreview;

    public static void start(Context context) {
        Intent intent = new Intent(context, ImageEditActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

        if (SimpleModel.getInstance().getImage() == null) {
            finish();
        }

        mEditPanelContainer = findViewById(R.id.vw_edit_panel_container);
        mVwLeftScissor = findViewById(R.id.vw_scissor_left_mask);
        mVwTopScissor = findViewById(R.id.vw_scissor_top_mask);
        mVwRightScissor = findViewById(R.id.vw_scissor_right_mask);
        mVwBottomScissor = findViewById(R.id.vw_scissor_bottom_mask);
        findViewById(R.id.vw_back).setOnClickListener(this);
        findViewById(R.id.tv_undo).setOnClickListener(this);
        findViewById(R.id.tv_redo).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        mVwPreview = findViewById(R.id.vw_texture);
        mVwPreview.setSurfaceTextureListener(this);
        mVwPreview.setOnTouchListener(mTouchListener);
        mEditListView = findViewById(R.id.rcv_edit_list);
        mEditListView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mEditListAdapter = new EditListAdapter(this, new EditListAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(EditListAdapter.EditItem editItem) {
                if (mCurrentPanel != null) {
                    mCurrentPanel.close(true);
                }
                mCurrentPanel = editItem.editPanel;
                if (editItem.editPanel != null) {
                    editItem.editPanel.show(mEditPanelContainer, mTextureWidth, mTextureHeight);
                } else {
                    Toast.makeText(ImageEditActivity.this, "Panel not impl!", Toast.LENGTH_SHORT).show();
                }
            }
        }, this);
        mEditListView.setAdapter(mEditListAdapter);

        // compute height by screen height
        int totalHeight = getWindowManager().getDefaultDisplay().getHeight() - DensityUtil.dip2px(this, 118);
        RelativeLayout.LayoutParams textureParams = (RelativeLayout.LayoutParams) mVwPreview.getLayoutParams();
        textureParams.height = (int) (totalHeight * 0.7f);
        mVwPreview.setLayoutParams(textureParams);
        RelativeLayout.LayoutParams containerParams = (RelativeLayout.LayoutParams) mEditPanelContainer.getLayoutParams();
        containerParams.height = (int) (totalHeight * 0.3f);
        mEditPanelContainer.setLayoutParams(containerParams);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentPanel != null) {
            mCurrentPanel.close(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mTextureWidth = width;
        mTextureHeight = height;
        Constants.mSurfaceWidth = width;
        Constants.mSurfaceHeight = height;
        Surface holdSurface = new Surface(surface);
        IEManager.getInstance().prepare(holdSurface, width, height);
        mBaseOp = new BaseImageOperator.Builder()
                .image(SimpleModel.getInstance().getImage())
                .build();

        IEManager.getInstance().addOperator(mBaseOp);
        Constants.mOpList.add(mBaseOp);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        IEManager.getInstance().stop();
        mBaseOp = null;

        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vw_back:
                onBackPressed();
                break;
            case R.id.tv_undo:
                IEManager.getInstance().undo();
                break;
            case R.id.tv_redo:
                IEManager.getInstance().redo();
                break;
            case R.id.tv_save:
                IEManager.getInstance().save(new File(Constants.mBaseDir, System.currentTimeMillis() + ".png"),
                        new SaveListener() {
                            @Override
                            public void onSaveFailed() {
                                Toast.makeText(ImageEditActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSaved(File target) {
                                Toast.makeText(ImageEditActivity.this, "Save succeed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            default:
                break;
        }
    }

    @Override
    public void onPanelShow(int type) {
        switch (type) {
            case AbstractPanel.TYPE_EFFECT:

                break;
            case AbstractPanel.TYPE_ADJUST:

                break;
            case AbstractPanel.TYPE_CUT:
                RelativeLayout.LayoutParams leftParams = (RelativeLayout.LayoutParams) mVwLeftScissor.getLayoutParams();
                leftParams.width = IEManager.getInstance().getImgShowLeft();
                mVwLeftScissor.setLayoutParams(leftParams);

                RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) mVwTopScissor.getLayoutParams();
                topParams.height = mVwPreview.getHeight() - IEManager.getInstance().getImgShowTop();
                mVwTopScissor.setLayoutParams(topParams);

                RelativeLayout.LayoutParams rightParams = (RelativeLayout.LayoutParams) mVwRightScissor.getLayoutParams();
                rightParams.width = mVwPreview.getWidth() - IEManager.getInstance().getImgShowRight();
                mVwRightScissor.setLayoutParams(rightParams);

                RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) mVwBottomScissor.getLayoutParams();
                bottomParams.height = IEManager.getInstance().getImgShowBottom();
                mVwBottomScissor.setLayoutParams(bottomParams);

                mBaseOp.setScissor(0, 0, mVwPreview.getWidth(), mVwPreview.getHeight());
                IEManager.getInstance().updateOperator(mBaseOp);
                break;
            case AbstractPanel.TYPE_ROTATE:

                break;
            case AbstractPanel.TYPE_TEXT:

                break;
            case AbstractPanel.TYPE_FOCUS:

                break;
            case AbstractPanel.TYPE_STICKER:

                break;
            case AbstractPanel.TYPE_MOSAIC:

                break;
            default:
                break;
        }
    }

    @Override
    public void onPanelClosed(int type, boolean discard) {
        mCurrentPanel = null;
        if (type == AbstractPanel.TYPE_CUT) {
            if (!discard) {
                if (mBaseOp != null) {
                    mBaseOp.setScissor(
                            mVwLeftScissor.getWidth(),
                            mVwBottomScissor.getHeight(),
                            mVwPreview.getWidth() - mVwRightScissor.getWidth() - mVwLeftScissor.getWidth(),
                            mVwPreview.getHeight() - mVwBottomScissor.getHeight() - mVwTopScissor.getHeight());
                    IEManager.getInstance().updateOperator(mBaseOp);
                }
            }

            RelativeLayout.LayoutParams leftParams = (RelativeLayout.LayoutParams) mVwLeftScissor.getLayoutParams();
            leftParams.width = 0;
            mVwLeftScissor.setLayoutParams(leftParams);

            RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) mVwTopScissor.getLayoutParams();
            topParams.height = 0;
            mVwTopScissor.setLayoutParams(topParams);

            RelativeLayout.LayoutParams rightParams = (RelativeLayout.LayoutParams) mVwRightScissor.getLayoutParams();
            rightParams.width = 0;
            mVwRightScissor.setLayoutParams(rightParams);

            RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) mVwBottomScissor.getLayoutParams();
            bottomParams.height = 0;
            mVwBottomScissor.setLayoutParams(bottomParams);
        }
    }
}
