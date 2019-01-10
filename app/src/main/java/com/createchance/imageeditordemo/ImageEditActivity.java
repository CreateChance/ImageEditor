package com.createchance.imageeditordemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.createchance.imageeditor.HistogramData;
import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.IEPreviewView;
import com.createchance.imageeditor.IHistogramGenerateListener;
import com.createchance.imageeditor.SaveListener;
import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditordemo.panels.AbstractPanel;
import com.createchance.imageeditordemo.utils.DensityUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageEditActivity extends AppCompatActivity implements
        View.OnClickListener,
        AbstractPanel.PanelListener,
        ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = "ImageEditActivity";

    private static final String EXTRA_IMAGE_PATH = "image path";

    private RecyclerView mEditListView;
    private EditListAdapter mEditListAdapter;

    // chart view.
    private View mVwHistogramContainer;
    private LineChart mVwHistogramChartAll, mVwHistogramChartRed, mVwHistogramChartGreen, mVwHistogramChartBlue;

    private String mImagePath;

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private float mCurScale = 1.0f;

    private EditSaveDoneWindow mVwSaveDone;

    private File mOutputFile;

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
            } else {
                if (mVwSaveDone == null || !mVwSaveDone.isShowing()) {
                    mScaleGestureDetector.onTouchEvent(event);
                    mGestureDetector.onTouchEvent(event);
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

    private IEPreviewView mVwPreview;

    public static void start(Context context, String imagePath) {
        Intent intent = new Intent(context, ImageEditActivity.class);
        intent.putExtra(EXTRA_IMAGE_PATH, imagePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

        Intent intent = getIntent();
        if (intent != null) {
            mImagePath = intent.getStringExtra(EXTRA_IMAGE_PATH);
        }

        if (TextUtils.isEmpty(mImagePath)) {
            Logger.e(TAG, "We can not get image!");
            finish();
        }

        mScaleGestureDetector = new ScaleGestureDetector(this, this);
        mGestureDetector = new GestureDetector(ImageEditActivity.this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
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
                Logger.d(TAG, "Scroll, dis x: " + distanceX + ", dis y: " + distanceY);
                int surfaceWidth = IEManager.getInstance().getSurfaceWidth(0);
                int surfaceHeight = IEManager.getInstance().getSurfaceHeight(0);
                IEManager.getInstance().setTranslateX(0,
                        (-distanceX * 2.0f / surfaceWidth) + IEManager.getInstance().getTranslateX(0), false);
                IEManager.getInstance().setTranslateY(0,
                        (distanceY * 2.0f / surfaceHeight) + IEManager.getInstance().getTranslateY(0), true);
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
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (IEManager.getInstance().getScaleX(0) != 1.0f) {
                    IEManager.getInstance().setScaleX(0, 1.0f, false);
                    IEManager.getInstance().setScaleY(0, 1.0f, false);
                    mCurScale = 1.0f;
                } else {
                    IEManager.getInstance().setScaleX(0, 2.0f, false);
                    IEManager.getInstance().setScaleY(0, 2.0f, false);
                    mCurScale = 2.0f;
                }
                IEManager.getInstance().setTranslateX(0, 0, false);
                IEManager.getInstance().setTranslateY(0, 0, true);
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }
        });
        mEditPanelContainer = findViewById(R.id.vw_edit_panel_container);
        mVwLeftScissor = findViewById(R.id.vw_scissor_left_mask);
        mVwTopScissor = findViewById(R.id.vw_scissor_top_mask);
        mVwRightScissor = findViewById(R.id.vw_scissor_right_mask);
        mVwBottomScissor = findViewById(R.id.vw_scissor_bottom_mask);
        mVwHistogramContainer = findViewById(R.id.vw_histogram_container);
        mVwHistogramChartAll = findViewById(R.id.vw_histogram_chart_all);
        mVwHistogramChartRed = findViewById(R.id.vw_histogram_chart_red);
        mVwHistogramChartGreen = findViewById(R.id.vw_histogram_chart_green);
        mVwHistogramChartBlue = findViewById(R.id.vw_histogram_chart_blue);
        findViewById(R.id.vw_back).setOnClickListener(this);
        findViewById(R.id.tv_undo).setOnClickListener(this);
        findViewById(R.id.tv_redo).setOnClickListener(this);
        findViewById(R.id.tv_histogram).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        mVwPreview = findViewById(R.id.vw_texture);
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

        // init IE
        IEManager.getInstance().startEngine();
        IEManager.getInstance().attachPreview(mVwPreview);
        IEManager.getInstance().addClip(mImagePath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // release IE
        IEManager.getInstance().detachPreview(mVwPreview);
        IEManager.getInstance().stopEngine();
    }

    @Override
    public void onBackPressed() {
        if (mVwSaveDone != null && mVwSaveDone.isShowing()) {
            return;
        }

        if (mVwHistogramContainer.getVisibility() == View.VISIBLE) {
            mVwHistogramContainer.setVisibility(View.GONE);
        } else if (mCurrentPanel != null) {
            mCurrentPanel.close(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vw_back:
                onBackPressed();
                break;
            case R.id.tv_undo:
                IEManager.getInstance().undo(0, true);
                break;
            case R.id.tv_redo:
                IEManager.getInstance().redo(0, true);
                break;
            case R.id.tv_histogram:
                IEManager.getInstance().generatorHistogram(0, new IHistogramGenerateListener() {
                    @Override
                    public void onHistogramGenerated(List<HistogramData> data, long totalPixelSize) {
                        mVwHistogramContainer.setVisibility(View.VISIBLE);

                        // all
                        List<Entry> allEntries = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            Logger.d(TAG, "histogram value: " + data.get(i) + ", total size: " + totalPixelSize);
                            allEntries.add(new Entry(i, data.get(i).mAll * 1.0f / totalPixelSize));
                        }

                        LineDataSet allDataSet = new LineDataSet(allEntries, "All histogram");
                        allDataSet.setColor(Color.WHITE);
                        allDataSet.setValueTextColor(Color.WHITE);
                        allDataSet.setDrawCircles(false);
                        allDataSet.setFillColor(Color.WHITE);
                        allDataSet.setDrawFilled(true);
                        allDataSet.setHighlightEnabled(true);
                        allDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        mVwHistogramChartAll.animateXY(1000, 1000);
                        mVwHistogramChartAll.getAxisLeft().setTextColor(Color.WHITE);
                        mVwHistogramChartAll.getAxisRight().setTextColor(Color.WHITE);
                        mVwHistogramChartAll.getXAxis().setTextColor(Color.WHITE);
                        mVwHistogramChartAll.setData(new LineData(allDataSet));

                        // red
                        List<Entry> redEntries = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            redEntries.add(new Entry(i, data.get(i).mRed * 1.0f / totalPixelSize));
                        }

                        LineDataSet redDataSet = new LineDataSet(redEntries, "Red histogram");
                        redDataSet.setColor(Color.RED);
                        redDataSet.setValueTextColor(Color.RED);
                        redDataSet.setDrawCircles(false);
                        redDataSet.setFillColor(Color.RED);
                        redDataSet.setDrawFilled(true);
                        redDataSet.setHighlightEnabled(true);
                        redDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        mVwHistogramChartRed.animateXY(1000, 1000);
                        mVwHistogramChartRed.getAxisLeft().setTextColor(Color.RED);
                        mVwHistogramChartRed.getAxisRight().setTextColor(Color.RED);
                        mVwHistogramChartRed.getXAxis().setTextColor(Color.RED);
                        mVwHistogramChartRed.setData(new LineData(redDataSet));

                        // green
                        List<Entry> greenEntries = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            greenEntries.add(new Entry(i, data.get(i).mGreen * 1.0f / totalPixelSize));
                        }

                        LineDataSet greenDataSet = new LineDataSet(greenEntries, "Green histogram");
                        greenDataSet.setColor(Color.GREEN);
                        greenDataSet.setValueTextColor(Color.GREEN);
                        greenDataSet.setDrawCircles(false);
                        greenDataSet.setFillColor(Color.GREEN);
                        greenDataSet.setDrawFilled(true);
                        greenDataSet.setHighlightEnabled(true);
                        greenDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        mVwHistogramChartGreen.animateXY(1000, 1000);
                        mVwHistogramChartGreen.getAxisLeft().setTextColor(Color.GREEN);
                        mVwHistogramChartGreen.getAxisRight().setTextColor(Color.GREEN);
                        mVwHistogramChartGreen.getXAxis().setTextColor(Color.GREEN);
                        mVwHistogramChartGreen.setData(new LineData(greenDataSet));

                        // blue
                        List<Entry> blueEntries = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            blueEntries.add(new Entry(i, data.get(i).mBlue * 1.0f / totalPixelSize));
                        }

                        LineDataSet blueDataSet = new LineDataSet(blueEntries, "Blue histogram");
                        blueDataSet.setColor(Color.BLUE);
                        blueDataSet.setValueTextColor(Color.BLUE);
                        blueDataSet.setDrawCircles(false);
                        blueDataSet.setFillColor(Color.BLUE);
                        blueDataSet.setDrawFilled(true);
                        blueDataSet.setHighlightEnabled(true);
                        blueDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        mVwHistogramChartBlue.animateXY(1000, 1000);
                        mVwHistogramChartBlue.getAxisLeft().setTextColor(Color.BLUE);
                        mVwHistogramChartBlue.getAxisRight().setTextColor(Color.BLUE);
                        mVwHistogramChartBlue.getXAxis().setTextColor(Color.BLUE);
                        mVwHistogramChartBlue.setData(new LineData(blueDataSet));

                        mVwHistogramChartAll.invalidate();
                        mVwHistogramChartRed.invalidate();
                        mVwHistogramChartGreen.invalidate();
                        mVwHistogramChartBlue.invalidate();
                    }
                });
                break;
            case R.id.tv_save:
                final OutputDialog dialog = OutputDialog.start(this);
                mOutputFile = new File(Constants.mBaseDir, System.currentTimeMillis() + ".jpg");
                IEManager.getInstance().saveAsImage(0,
//                        IEManager.getInstance().getOriginWidth(0),
//                        IEManager.getInstance().getOriginHeight(0),
                        mOutputFile,
                        new SaveListener() {
                            @Override
                            public void onSaveFailed() {
                                Toast.makeText(ImageEditActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Log.d(TAG, "onSaveFailed: " + Thread.currentThread().getName());
                            }

                            @Override
                            public void onSaved(File target) {
                                Toast.makeText(ImageEditActivity.this, "Save succeed!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Log.d(TAG, "onSaved: " + Thread.currentThread().getName() + ", file: " + target.getAbsolutePath());
                                showSaveDoneWindow();
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
                mVwLeftScissor.setVisibility(View.VISIBLE);
                mVwRightScissor.setVisibility(View.VISIBLE);
                mVwTopScissor.setVisibility(View.VISIBLE);
                mVwBottomScissor.setVisibility(View.VISIBLE);
                int renderLeft = IEManager.getInstance().getRenderLeft(0);
                int renderBottom = IEManager.getInstance().getRenderBottom(0);
                int renderWidth = IEManager.getInstance().getRenderWidth(0);
                int renderHeight = IEManager.getInstance().getRenderHeight(0);
                RelativeLayout.LayoutParams leftParams = (RelativeLayout.LayoutParams) mVwLeftScissor.getLayoutParams();
                leftParams.width = (int) (IEManager.getInstance().getScissorX(0) * renderWidth) + renderLeft;
                mVwLeftScissor.setLayoutParams(leftParams);

                RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) mVwTopScissor.getLayoutParams();
                topParams.height = (int) (mVwPreview.getHeight() -
                        IEManager.getInstance().getScissorHeight(0) * renderHeight - renderBottom);
                mVwTopScissor.setLayoutParams(topParams);

                RelativeLayout.LayoutParams rightParams = (RelativeLayout.LayoutParams) mVwRightScissor.getLayoutParams();
                rightParams.width = (int) (mVwPreview.getWidth() -
                        IEManager.getInstance().getScissorWidth(0) * renderWidth - renderLeft);
                mVwRightScissor.setLayoutParams(rightParams);

                RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) mVwBottomScissor.getLayoutParams();
                bottomParams.height = (int) (IEManager.getInstance().getScissorY(0) * renderHeight) + renderBottom;
                mVwBottomScissor.setLayoutParams(bottomParams);

                IEManager.getInstance().setScissorX(0, 0, false);
                IEManager.getInstance().setScissorY(0, 0, false);
                IEManager.getInstance().setScissorWidth(0, 1, false);
                IEManager.getInstance().setScissorHeight(0, 1, true);
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

        // reset image translate and scale.
        IEManager.getInstance().setTranslateX(0, 0, false);
        IEManager.getInstance().setTranslateY(0, 0, false);
        IEManager.getInstance().setScaleX(0, 1.0f, false);
        IEManager.getInstance().setScaleY(0, 1.0f, true);
        mCurScale = 1.0f;
    }

    @Override
    public void onPanelClosed(int type, boolean discard) {
        mCurrentPanel = null;
        if (type == AbstractPanel.TYPE_CUT) {
            if (!discard) {
                int renderLeft = IEManager.getInstance().getRenderLeft(0);
                int renderBottom = IEManager.getInstance().getRenderBottom(0);
                int renderWidth = IEManager.getInstance().getRenderWidth(0);
                int renderHeight = IEManager.getInstance().getRenderHeight(0);
                IEManager.getInstance().setScissorX(0, (mVwLeftScissor.getWidth() - renderLeft) * 1.0f / renderWidth, false);
                IEManager.getInstance().setScissorY(0, (mVwBottomScissor.getHeight() - renderBottom) * 1.0f / renderHeight, false);
                IEManager.getInstance().setScissorWidth(0,
                        (mVwPreview.getWidth() - mVwRightScissor.getWidth() - mVwLeftScissor.getWidth()) * 1.0f / renderWidth,
                        false);
                IEManager.getInstance().setScissorHeight(0,
                        (mVwPreview.getHeight() - mVwBottomScissor.getHeight() - mVwTopScissor.getHeight()) * 1.0f / renderHeight,
                        true);
            }

            mVwLeftScissor.setVisibility(View.GONE);
            mVwRightScissor.setVisibility(View.GONE);
            mVwTopScissor.setVisibility(View.GONE);
            mVwBottomScissor.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mCurScale *= detector.getScaleFactor();
        IEManager.getInstance().setScaleX(0, mCurScale, false);
        IEManager.getInstance().setScaleY(0, mCurScale, true);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    private void showSaveDoneWindow() {
        if (mVwSaveDone == null) {
            mVwSaveDone = new EditSaveDoneWindow(this, new EditSaveDoneWindow.EditSaveDoneListener() {
                @Override
                public void onShare() {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ImageEditActivity.this,
                                "com.createchance.imageeditordemo.fileprovider",
                                mOutputFile));
                    } else {
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mOutputFile));
                    }
                    intent.setType("image/*");
                    startActivity(intent);
                }

                @Override
                public void onExit() {
                    finish();
                }
            });
            mVwSaveDone.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                }
            });
        }
        mVwSaveDone.showAtLocation(findViewById(R.id.vw_root),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        // set dark background color.
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
    }
}
