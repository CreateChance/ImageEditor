package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.filters.GPUImageTransformFilter;
import com.createchance.imageeditor.ops.BaseImageOperator;
import com.createchance.imageeditor.ops.FilterOperator;
import com.createchance.imageeditor.utils.UiThreadUtil;
import com.createchance.imageeditordemo.Constants;
import com.createchance.imageeditordemo.R;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Rotate edit panel.
 *
 * @author gaochao1-iri
 * @date 2018/11/5
 */
public class EditRotatePanel extends AbstractPanel implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "EditRotatePanel";

    private View mRotatePanel;

    private TextView mTvFlipX, mTvFlipY, mTvFlipZ, mTranslateX, mTranslateY, mTranslateZ;

    private FilterOperator mTransformOp;

    private final int TYPE_ROTATE_INVALID = -1;
    private final int TYPE_ROTATE_LEFT = 0;
    private final int TYPE_ROTATE_RIGHT = 1;
    private final int TYPE_ROTATE_FLIP_X = 2;
    private final int TYPE_ROTATE_FLIP_Y = 3;

    private float mCurRotate, mTargetRotate;
    private int mType = TYPE_ROTATE_INVALID;
    private float[] projectionMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] scaleMatrix = new float[16];

    private final float mBigScaleFactor = 1.0f;
    private final float mSmallScaleFactor = 0.6f;
    private Runnable mScaleSmallTasks = new Runnable() {
        float currentFactor = mBigScaleFactor;

        @Override
        public void run() {
            Matrix.scaleM(scaleMatrix, 0, currentFactor, currentFactor, 1);
            currentFactor -= 0.1f;
            ((GPUImageTransformFilter) mTransformOp.getFilter()).setTransform3D(scaleMatrix);
            IEManager.getInstance().updateOperator(mTransformOp);

            if (currentFactor > mSmallScaleFactor) {
                UiThreadUtil.postDelayed(this, 50);
            } else {
                currentFactor = mBigScaleFactor;
                UiThreadUtil.post(mRotateTask);
            }
        }
    };
    private Runnable mScaleBigTask = new Runnable() {
        float currentFactor = mSmallScaleFactor;

        @Override
        public void run() {
            Matrix.scaleM(scaleMatrix, 0, currentFactor, currentFactor, 1);
            currentFactor += 0.1f;
            ((GPUImageTransformFilter) mTransformOp.getFilter()).setTransform3D(scaleMatrix);
            IEManager.getInstance().updateOperator(mTransformOp);

            if (currentFactor < mBigScaleFactor) {
                UiThreadUtil.postDelayed(this, 50);
            } else {
                currentFactor = mSmallScaleFactor;
            }
        }
    };
    private Runnable mRotateTask = new Runnable() {
        @Override
        public void run() {
            switch (mType) {
                case TYPE_ROTATE_LEFT:
                    mCurRotate += 90 * 1.0f / 90;
                    Matrix.setRotateM(projectionMatrix, 0, mCurRotate, 0, 0, 1.0f);
                    break;
                case TYPE_ROTATE_RIGHT:
                    mCurRotate -= 90 * 1.0f / 90;
                    Matrix.setRotateM(projectionMatrix, 0, mCurRotate, 0, 0, 1.0f);
                    break;
                case TYPE_ROTATE_FLIP_X:
                    mCurRotate += 180 * 1.0f / 90;
                    perspectiveM(projectionMatrix,
                            45,
                            Constants.mSurfaceWidth * 1.0f / Constants.mSurfaceHeight,
                            0.1f,
                            100f);
                    setIdentityM(modelMatrix, 0);
                    translateM(modelMatrix, 0, 0, 0, -2.5f);
                    rotateM(modelMatrix, 0, mCurRotate, 0f, 1f, 0f);

                    final float[] temp1 = new float[16];
                    multiplyMM(temp1, 0, projectionMatrix, 0, modelMatrix, 0);
                    System.arraycopy(temp1, 0, projectionMatrix, 0, temp1.length);
                    break;
                case TYPE_ROTATE_FLIP_Y:
                    mCurRotate += 180 * 1.0f / 90;
                    perspectiveM(projectionMatrix,
                            45,
                            Constants.mSurfaceWidth * 1.0f / Constants.mSurfaceHeight,
                            0.1f,
                            100f);
                    setIdentityM(modelMatrix, 0);
                    translateM(modelMatrix, 0, 0, 0, -2.5f);
                    rotateM(modelMatrix, 0, mCurRotate, 1f, 0f, 0f);

                    final float[] temp2 = new float[16];
                    multiplyMM(temp2, 0, projectionMatrix, 0, modelMatrix, 0);
                    System.arraycopy(temp2, 0, projectionMatrix, 0, temp2.length);
                    break;
                default:
                    break;
            }

            ((GPUImageTransformFilter) mTransformOp.getFilter()).setTransform3D(projectionMatrix);
            IEManager.getInstance().updateOperator(mTransformOp);

            if (mCurRotate != mTargetRotate) {
                UiThreadUtil.postDelayed(this, 1);
            } else {
                mType = TYPE_ROTATE_INVALID;
//                UiThreadUtil.post(mScaleBigTask);
            }
        }
    };

    public EditRotatePanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_ROTATE);

        mRotatePanel = LayoutInflater.from(mContext).inflate(R.layout.edit_panel_rotate, mParent, false);
        mRotatePanel.findViewById(R.id.iv_cancel).setOnClickListener(this);
        mRotatePanel.findViewById(R.id.iv_apply).setOnClickListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_rotate_x)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_rotate_y)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_rotate_z)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_translate_x)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_translate_y)).setOnSeekBarChangeListener(this);
        ((SeekBar) mRotatePanel.findViewById(R.id.sb_translate_z)).setOnSeekBarChangeListener(this);
        mTvFlipX = mRotatePanel.findViewById(R.id.tv_rotate_x);
        mTvFlipY = mRotatePanel.findViewById(R.id.tv_rotate_y);
        mTvFlipZ = mRotatePanel.findViewById(R.id.tv_rotate_z);
        mTranslateX = mRotatePanel.findViewById(R.id.tv_translate_x);
        mTranslateY = mRotatePanel.findViewById(R.id.tv_translate_y);
        mTranslateZ = mRotatePanel.findViewById(R.id.tv_translate_z);
        mTvFlipX.setText(String.format(mContext.getString(R.string.edit_rotate_x), 180));
        mTvFlipY.setText(String.format(mContext.getString(R.string.edit_rotate_y), 0));
        mTvFlipZ.setText(String.format(mContext.getString(R.string.edit_rotate_z), 0));

        mRotatePanel.findViewById(R.id.vw_rotate_left).setOnClickListener(this);
        mRotatePanel.findViewById(R.id.vw_rotate_right).setOnClickListener(this);
        mRotatePanel.findViewById(R.id.vw_rotate_flip_x).setOnClickListener(this);
        mRotatePanel.findViewById(R.id.vw_rotate_flip_y).setOnClickListener(this);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        super.show(parent, surfaceWidth, surfaceHeight);

        mParent.addView(mRotatePanel);
    }

    @Override
    public void close(boolean discard) {
        super.close(discard);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_cancel:
                close(true);
                break;
            case R.id.iv_apply:
                close(false);
                break;
            case R.id.vw_rotate_left:
                if (mTransformOp == null) {
                    mTransformOp = new FilterOperator.Builder()
                            .filter(new GPUImageTransformFilter())
                            .build();
                    IEManager.getInstance().addOperator(mTransformOp);
                }
                if (mType == TYPE_ROTATE_INVALID) {
                    mType = TYPE_ROTATE_LEFT;
                    mTargetRotate = mCurRotate + 90;
                    UiThreadUtil.post(mRotateTask);
                }
                break;
            case R.id.vw_rotate_right:
                if (mTransformOp == null) {
                    mTransformOp = new FilterOperator.Builder()
                            .filter(new GPUImageTransformFilter())
                            .build();
                    IEManager.getInstance().addOperator(mTransformOp);
                }
                if (mType == TYPE_ROTATE_INVALID) {
                    mType = TYPE_ROTATE_RIGHT;
                    mTargetRotate = mCurRotate - 90;
                    UiThreadUtil.post(mRotateTask);
                }
                break;
            case R.id.vw_rotate_flip_x:
                if (mTransformOp == null) {
                    mTransformOp = new FilterOperator.Builder()
                            .filter(new GPUImageTransformFilter())
                            .build();
                    IEManager.getInstance().addOperator(mTransformOp);
                }
                if (mType == TYPE_ROTATE_INVALID) {
                    mType = TYPE_ROTATE_FLIP_X;
                    mTargetRotate = mCurRotate + 180;
                    UiThreadUtil.post(mRotateTask);
                }
                break;
            case R.id.vw_rotate_flip_y:
                if (mTransformOp == null) {
                    mTransformOp = new FilterOperator.Builder()
                            .filter(new GPUImageTransformFilter())
                            .build();
                    IEManager.getInstance().addOperator(mTransformOp);
                }
                if (mType == TYPE_ROTATE_INVALID) {
                    mType = TYPE_ROTATE_FLIP_Y;
                    mTargetRotate = mCurRotate + 180;
                    UiThreadUtil.post(mRotateTask);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        BaseImageOperator baseImageOperator = (BaseImageOperator) Constants.mOpList.get(0);
        switch (seekBar.getId()) {
            case R.id.sb_rotate_x:
                mTvFlipX.setText(String.format(mContext.getString(R.string.edit_rotate_x), progress));
                baseImageOperator.setFlipX(progress);
                break;
            case R.id.sb_rotate_y:
                mTvFlipY.setText(String.format(mContext.getString(R.string.edit_rotate_y), progress));
                baseImageOperator.setFlipY(progress);
                break;
            case R.id.sb_rotate_z:
                mTvFlipZ.setText(String.format(mContext.getString(R.string.edit_rotate_z), progress));
//                baseImageOperator.setFlipZ(progress);
                break;
            case R.id.sb_translate_x:
                mTranslateX.setText(String.format(mContext.getString(R.string.edit_translate_x), progress));
                baseImageOperator.setTranslateX(progress);
                break;
            case R.id.sb_translate_y:
                mTranslateY.setText(String.format(mContext.getString(R.string.edit_translate_y), progress));
                baseImageOperator.setTranslateY(progress);
                break;
            case R.id.sb_translate_z:
                float transZ = baseImageOperator.getNear() +
                        (progress * (baseImageOperator.getFar() - baseImageOperator.getNear()) / seekBar.getMax());
                mTranslateZ.setText(String.format(mContext.getString(R.string.edit_translate_z), transZ));
                baseImageOperator.setTranslateZ(transZ);
                break;
            default:
                break;
        }
        IEManager.getInstance().updateOperator(baseImageOperator);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void perspectiveM(float[] m, float yFovInDegrees, float aspect,
                              float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);

        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }
}
