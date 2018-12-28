package com.createchance.imageeditordemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.createchance.imageeditor.HistogramData;
import com.createchance.imageeditor.IEClip;
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

    private IEClip mClip;

    private String mImagePath;

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private float mCurScale = 1.0f;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        private int mLastX, mLastY;
        private int mDownX, mDownY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mScaleGestureDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
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
                if (mCurrentPanel == null) {
                    mClip.setTranslateX((-distanceX * 2.0f / mClip.getSurfaceWidth()) + mClip.getTranslateX());
                    mClip.setTranslateY((distanceY * 2.0f / mClip.getSurfaceHeight()) + mClip.getTranslateY());
                    IEManager.getInstance().renderClip(0);
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
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mClip.getScaleX() != 1.0f) {
                    mClip.setScaleX(1.0f);
                    mClip.setScaleY(1.0f);
                    mCurScale = 1.0f;
                } else {
                    mClip.setScaleX(2.0f);
                    mClip.setScaleY(2.0f);
                    mCurScale = 2.0f;
                }
                mClip.setTranslateX(0);
                mClip.setTranslateY(0);
                IEManager.getInstance().renderClip(0);
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
        mClip = IEManager.getInstance().addClip(mImagePath);
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
                IEManager.getInstance().undo(0);
                break;
            case R.id.tv_redo:
                IEManager.getInstance().redo(0);
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
                IEManager.getInstance().saveAsImage(mClip.getOriginWidth(),
                        mClip.getOriginHeight(),
                        new File(Constants.mBaseDir, System.currentTimeMillis() + ".jpg"),
                        new SaveListener() {
                            @Override
                            public void onSaveFailed() {
                                Toast.makeText(ImageEditActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onSaveFailed: " + Thread.currentThread().getName());
                            }

                            @Override
                            public void onSaved(File target) {
                                Toast.makeText(ImageEditActivity.this, "Save succeed!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onSaved: " + Thread.currentThread().getName() + ", file: " + target.getAbsolutePath());
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
                RelativeLayout.LayoutParams leftParams = (RelativeLayout.LayoutParams) mVwLeftScissor.getLayoutParams();
                leftParams.width = mClip.getScissorX();
                mVwLeftScissor.setLayoutParams(leftParams);

                RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) mVwTopScissor.getLayoutParams();
                topParams.height = mVwPreview.getHeight() - (mClip.getScissorY() + mClip.getScissorHeight());
                mVwTopScissor.setLayoutParams(topParams);

                RelativeLayout.LayoutParams rightParams = (RelativeLayout.LayoutParams) mVwRightScissor.getLayoutParams();
                rightParams.width = mVwPreview.getWidth() - (mClip.getScissorX() + mClip.getScissorWidth());
                mVwRightScissor.setLayoutParams(rightParams);

                RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) mVwBottomScissor.getLayoutParams();
                bottomParams.height = mClip.getScissorY();
                mVwBottomScissor.setLayoutParams(bottomParams);

                mClip.setScissorX(mClip.getRenderLeft());
                mClip.setScissorY(mClip.getRenderBottom());
                mClip.setScissorWidth(mClip.getRenderWidth());
                mClip.setScissorHeight(mClip.getRenderHeight());
                IEManager.getInstance().renderClip(0);
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
                IEManager.getInstance().getClip(0).setScissorX(mVwLeftScissor.getWidth());
                IEManager.getInstance().getClip(0).setScissorY(mVwBottomScissor.getHeight());
                IEManager.getInstance().getClip(0).setScissorWidth(
                        mVwPreview.getWidth() - mVwRightScissor.getWidth() - mVwLeftScissor.getWidth());
                IEManager.getInstance().getClip(0).setScissorHeight
                        (mVwPreview.getHeight() - mVwBottomScissor.getHeight() - mVwTopScissor.getHeight());
                IEManager.getInstance().renderClip(0);
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
        mClip.setScaleX(mCurScale);
        mClip.setScaleY(mCurScale);
        IEManager.getInstance().renderClip(0);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
