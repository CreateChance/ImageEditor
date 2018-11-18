package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.createchance.imageeditor.IEManager;
import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.ops.BrightnessAdjustOperator;
import com.createchance.imageeditor.ops.ContrastAdjustOperator;
import com.createchance.imageeditor.ops.DenoiseOperator;
import com.createchance.imageeditor.ops.ExposureAdjustOperator;
import com.createchance.imageeditor.ops.GammaAdjustOperator;
import com.createchance.imageeditor.ops.HighlightAdjustOperator;
import com.createchance.imageeditor.ops.SaturationAdjustOperator;
import com.createchance.imageeditor.ops.ShadowAdjustOperator;
import com.createchance.imageeditor.ops.SharpenAdjustOperator;
import com.createchance.imageeditor.ops.TempAdjustOperator;
import com.createchance.imageeditor.ops.TintAdjustOperator;
import com.createchance.imageeditor.ops.VignetteOperator;
import com.createchance.imageeditordemo.AdjustListAdapter;
import com.createchance.imageeditordemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/2
 */
public class EditAdjustPanel extends AbstractPanel implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        AdjustListAdapter.OnAdjustSelectListener {

    private static final String TAG = "EditAdjustPanel";

    private View mAdjustPanel;

    private int mCurType = -1;

    private BrightnessAdjustOperator mBrightnessOp;
    private ExposureAdjustOperator mExposureOp;
    private GammaAdjustOperator mGammaOp;
    private ContrastAdjustOperator mContrastOp;
    private SaturationAdjustOperator mSaturationOp;
    private SharpenAdjustOperator mSharpenOp;
    private VignetteOperator mVignetteOp;
    private ShadowAdjustOperator mShadowOp;
    private HighlightAdjustOperator mHighlightOp;
    private TempAdjustOperator mTempOp;
    private TintAdjustOperator mTintOp;
    private DenoiseOperator mDenoiseOp;

    private TextView mAdjustName, mAdjustValue;

    private SeekBar mAdjustBar;

    public EditAdjustPanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_ADJUST);

        mAdjustPanel = LayoutInflater.from(mContext).inflate(R.layout.edit_panel_adjust, mParent, false);
        mAdjustName = mAdjustPanel.findViewById(R.id.tv_adjust_name);
        mAdjustValue = mAdjustPanel.findViewById(R.id.tv_adjust_value);
        mAdjustBar = mAdjustPanel.findViewById(R.id.sb_adjust_bar);
        mAdjustBar.setEnabled(false);
        mAdjustBar.setOnSeekBarChangeListener(this);
        RecyclerView adjustListView = mAdjustPanel.findViewById(R.id.rcv_adjust_list);
        adjustListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        AdjustListAdapter adjustListAdapter = new AdjustListAdapter(mContext, this);
        adjustListView.setAdapter(adjustListAdapter);
        mAdjustPanel.findViewById(R.id.iv_cancel).setOnClickListener(this);
        mAdjustPanel.findViewById(R.id.iv_apply).setOnClickListener(this);
    }

    @Override
    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        super.show(parent, surfaceWidth, surfaceHeight);

        mParent.addView(mAdjustPanel);
    }

    @Override
    public void close(boolean discard) {
        super.close(discard);

        if (discard) {
            List<AbstractOperator> operatorList = new ArrayList<>();
            if (mBrightnessOp != null) {
                operatorList.add(mBrightnessOp);
            }
            if (mExposureOp != null) {
                operatorList.add(mExposureOp);
            }
            if (mGammaOp != null) {
                operatorList.add(mGammaOp);
            }
            if (mContrastOp != null) {
                operatorList.add(mContrastOp);
            }
            if (mSaturationOp != null) {
                operatorList.add(mSaturationOp);
            }
            if (mSharpenOp != null) {
                operatorList.add(mSharpenOp);
            }
            if (mVignetteOp != null) {
                operatorList.add(mVignetteOp);
            }
            if (mShadowOp != null) {
                operatorList.add(mShadowOp);
            }
            if (mHighlightOp != null) {
                operatorList.add(mHighlightOp);
            }
            if (mTempOp != null) {
                operatorList.add(mTempOp);
            }
            if (mTintOp != null) {
                operatorList.add(mTintOp);
            }
            if (mDenoiseOp != null) {
                operatorList.add(mDenoiseOp);
            }
            IEManager.getInstance().removeOperator(operatorList);
            mBrightnessOp = null;
            mExposureOp = null;
            mGammaOp = null;
            mContrastOp = null;
            mSaturationOp = null;
            mSharpenOp = null;
            mVignetteOp = null;
            mShadowOp = null;
            mHighlightOp = null;
            mTempOp = null;
            mTintOp = null;
            mDenoiseOp = null;
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        switch (mCurType) {
            case AdjustListAdapter.AdjustItem.TYPE_BRIGHTNESS:
                if (mBrightnessOp == null) {
                    mBrightnessOp = new BrightnessAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mBrightnessOp);
                }

                mAdjustValue.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 2.0f) / seekBar.getMax()));
                mBrightnessOp.setBrightness(((progress - seekBar.getMax() / 2) * 2.0f) / seekBar.getMax());
                IEManager.getInstance().updateOperator(mBrightnessOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_EXPOSURE:
                if (mExposureOp == null) {
                    mExposureOp = new ExposureAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mExposureOp);
                }

                mAdjustValue.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax()));
                mExposureOp.setExposure(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax());
                IEManager.getInstance().updateOperator(mExposureOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_GAMMA:
                if (mGammaOp == null) {
                    mGammaOp = new GammaAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mGammaOp);
                }

                mAdjustValue.setText(String.valueOf(progress * 4.0f / seekBar.getMax()));
                mGammaOp.setGamma(progress * 4.0f / seekBar.getMax());
                IEManager.getInstance().updateOperator(mGammaOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_CONTRAST:
                if (mContrastOp == null) {
                    mContrastOp = new ContrastAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mContrastOp);
                }

                mAdjustValue.setText(String.valueOf(progress * 2.0f / seekBar.getMax()));
                mContrastOp.setContrast(progress * 2.0f / seekBar.getMax());
                IEManager.getInstance().updateOperator(mContrastOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SATURATION:
                if (mSaturationOp == null) {
                    mSaturationOp = new SaturationAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mSaturationOp);
                }
                mAdjustValue.setText(String.valueOf(progress * 2.0f / seekBar.getMax()));
                mSaturationOp.setSaturation(progress * 2.0f / seekBar.getMax());
                IEManager.getInstance().updateOperator(mSaturationOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHARPEN:
                if (mSharpenOp == null) {
                    mSharpenOp = new SharpenAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mSharpenOp);
                }
                mAdjustValue.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 8.0f) / seekBar.getMax()));
                mSharpenOp.setSharpness(((progress - seekBar.getMax() / 2) * 8.0f) / seekBar.getMax());
                IEManager.getInstance().updateOperator(mSharpenOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DARK_CORNER:
                if (mVignetteOp == null) {
                    mVignetteOp = new VignetteOperator.Builder().build();
                    IEManager.getInstance().addOperator(mVignetteOp);
                }
                mAdjustValue.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                mVignetteOp.setStart(progress * 1.0f / seekBar.getMax());
                IEManager.getInstance().updateOperator(mVignetteOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHADOW:
                if (mShadowOp == null) {
                    mShadowOp = new ShadowAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mShadowOp);
                }
                mAdjustValue.setText(String.valueOf(progress * 1.0f * 100 / seekBar.getMax()));
                mShadowOp.setShadow(progress * 1.0f * 100 / seekBar.getMax());
                IEManager.getInstance().updateOperator(mShadowOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_HIGHLIGHT:
                if (mHighlightOp == null) {
                    mHighlightOp = new HighlightAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mHighlightOp);
                }
                mAdjustValue.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                mHighlightOp.setHighlight(progress * 1.0f / seekBar.getMax());
                IEManager.getInstance().updateOperator(mHighlightOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_COLOR_TEMP:
                if (mTempOp == null) {
                    mTempOp = new TempAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mTempOp);
                }
                mAdjustValue.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax()));
                mTempOp.setTemperature(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax());
                IEManager.getInstance().updateOperator(mTempOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_TONE:
                if (mTintOp == null) {
                    mTintOp = new TintAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(mTintOp);
                }
                mAdjustValue.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax()));
                mTintOp.setTint(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax());
                IEManager.getInstance().updateOperator(mTintOp);
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DENOISE:
                if (mDenoiseOp == null) {
                    mDenoiseOp = new DenoiseOperator();
                    IEManager.getInstance().addOperator(mDenoiseOp);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onAdjustSelected(AdjustListAdapter.AdjustItem adjustItem) {
        mCurType = adjustItem.mType;
        mAdjustName.setText(adjustItem.mNameStrId);
        switch (mCurType) {
            case AdjustListAdapter.AdjustItem.TYPE_BRIGHTNESS:
                mAdjustBar.setEnabled(true);
                if (mBrightnessOp == null) {
                    mAdjustValue.setText(String.valueOf(0.0f));
                    mAdjustBar.setProgress(mAdjustBar.getMax() / 2);
                } else {
                    mAdjustValue.setText(String.valueOf(mBrightnessOp.getBrightness()));
                    mAdjustBar.setProgress(
                            (int) (mBrightnessOp.getBrightness() * mAdjustBar.getMax() / 2 + mAdjustBar.getMax() / 2));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_EXPOSURE:
                mAdjustBar.setEnabled(true);
                if (mExposureOp == null) {
                    mAdjustValue.setText(String.valueOf(0.0));
                    mAdjustBar.setProgress(mAdjustBar.getMax() / 2);
                } else {
                    mAdjustValue.setText(String.valueOf(mExposureOp.getExposure()));
                    mAdjustBar.setProgress((int) ((mExposureOp.getExposure() + 2) * 0.25f * mAdjustBar.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_GAMMA:
                mAdjustBar.setEnabled(true);
                if (mGammaOp == null) {
                    mAdjustValue.setText(String.valueOf(1.0));
                    mAdjustBar.setProgress((int) (mAdjustBar.getMax() * 0.25f));
                } else {
                    mAdjustValue.setText(String.valueOf(mGammaOp.getGamma()));
                    mAdjustBar.setProgress((int) (mGammaOp.getGamma() * 0.25f * mAdjustBar.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_CONTRAST:
                mAdjustBar.setEnabled(true);
                if (mContrastOp == null) {
                    mAdjustValue.setText(String.valueOf(1.0f));
                    mAdjustBar.setProgress(mAdjustBar.getMax() / 2);
                } else {
                    mAdjustValue.setText(String.valueOf(mContrastOp.getContrast()));
                    mAdjustBar.setProgress((int) (mContrastOp.getContrast() * 0.5f * mAdjustBar.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SATURATION:
                mAdjustBar.setEnabled(true);
                if (mSaturationOp == null) {
                    mAdjustValue.setText(String.valueOf(1.0f));
                    mAdjustBar.setProgress(mAdjustBar.getMax() / 2);
                } else {
                    mAdjustValue.setText(String.valueOf(mSaturationOp.getSaturation()));
                    mAdjustBar.setProgress((int) (mSaturationOp.getSaturation() * 0.5f * mAdjustBar.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHARPEN:
                mAdjustBar.setEnabled(true);
                if (mSharpenOp == null) {
                    mAdjustValue.setText(String.valueOf(0.0f));
                    mAdjustBar.setProgress(mAdjustBar.getMax() / 2);
                } else {
                    mAdjustValue.setText(String.valueOf(mSharpenOp.getSharpness()));
                    mAdjustBar.setProgress((int) ((mSharpenOp.getSharpness() + 4) * 0.125f * mAdjustBar.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DARK_CORNER:
                mAdjustBar.setEnabled(true);
                if (mVignetteOp == null) {
                    mAdjustValue.setText(String.valueOf(0.3f));
                    mAdjustBar.setProgress(mAdjustBar.getMax() / 2);
                } else {
                    mAdjustValue.setText(String.valueOf(mVignetteOp.getStart()));
                    mAdjustBar.setProgress((int) (mVignetteOp.getStart() * mAdjustBar.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHADOW:
                mAdjustBar.setEnabled(true);
                if (mShadowOp == null) {
                    mAdjustValue.setText(String.valueOf(0));
                    mAdjustBar.setProgress(0);
                } else {
                    mAdjustValue.setText(String.valueOf(mShadowOp.getShadow()));
                    mAdjustBar.setProgress((int) (mShadowOp.getShadow() * mAdjustBar.getMax() / 100f));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_HIGHLIGHT:
                mAdjustBar.setEnabled(true);
                if (mHighlightOp == null) {
                    mAdjustValue.setText(String.valueOf(0));
                    mAdjustBar.setProgress(0);
                } else {
                    mAdjustValue.setText(String.valueOf(mHighlightOp.getHighlight()));
                    mAdjustBar.setProgress((int) (mHighlightOp.getHighlight() * mAdjustBar.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_COLOR_TEMP:
                mAdjustBar.setEnabled(true);
                if (mTempOp == null) {
                    mAdjustValue.setText(String.valueOf(0.0));
                    mAdjustBar.setProgress(mAdjustBar.getMax() / 2);
                } else {
                    mAdjustValue.setText(String.valueOf(mTempOp.getTemperature()));
                    mAdjustBar.setProgress((int) ((mTempOp.getTemperature() + 2) * 0.25f * mAdjustBar.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_TONE:
                mAdjustBar.setEnabled(true);
                if (mTintOp == null) {
                    mAdjustValue.setText(String.valueOf(0.0));
                    mAdjustBar.setProgress(mAdjustBar.getMax() / 2);
                } else {
                    mAdjustValue.setText(String.valueOf(mTintOp.getTint()));
                    mAdjustBar.setProgress((int) ((mTintOp.getTint() + 2) * 0.25f * mAdjustBar.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DENOISE:
                if (mDenoiseOp == null) {
                    mDenoiseOp = new DenoiseOperator();
                    IEManager.getInstance().addOperator(mDenoiseOp);
                }
                break;
            default:
                mAdjustValue.setText("0");
                mAdjustBar.setEnabled(false);
                break;
        }
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
            default:
                break;
        }
    }
}
