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
import com.createchance.imageeditor.ops.ColorBalanceOperator;
import com.createchance.imageeditor.ops.ContrastAdjustOperator;
import com.createchance.imageeditor.ops.DenoiseOperator;
import com.createchance.imageeditor.ops.ExposureAdjustOperator;
import com.createchance.imageeditor.ops.GammaAdjustOperator;
import com.createchance.imageeditor.ops.HighlightAdjustOperator;
import com.createchance.imageeditor.ops.RGBAdjustOperator;
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
 * Image params adjust panel.
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
    private RGBAdjustOperator mRGBOp;
    private ColorBalanceOperator mColorBalanceOp;

    private TextView mAdjustName1, mAdjustValue1, mAdjustName2, mAdjustValue2, mAdjustName3, mAdjustValue3;

    private SeekBar mAdjustBar1, mAdjustBar2, mAdjustBar3;

    public EditAdjustPanel(Context context, PanelListener listener) {
        super(context, listener, TYPE_ADJUST);

        mAdjustPanel = LayoutInflater.from(mContext).inflate(R.layout.edit_panel_adjust, mParent, false);
        mAdjustName1 = mAdjustPanel.findViewById(R.id.tv_adjust_name_1);
        mAdjustValue1 = mAdjustPanel.findViewById(R.id.tv_adjust_value_1);
        mAdjustName2 = mAdjustPanel.findViewById(R.id.tv_adjust_name_2);
        mAdjustValue2 = mAdjustPanel.findViewById(R.id.tv_adjust_value_2);
        mAdjustName3 = mAdjustPanel.findViewById(R.id.tv_adjust_name_3);
        mAdjustValue3 = mAdjustPanel.findViewById(R.id.tv_adjust_value_3);
        mAdjustBar1 = mAdjustPanel.findViewById(R.id.sb_adjust_bar_1);
        mAdjustBar1.setOnSeekBarChangeListener(this);
        mAdjustBar1.setVisibility(View.GONE);
        mAdjustBar2 = mAdjustPanel.findViewById(R.id.sb_adjust_bar_2);
        mAdjustBar2.setOnSeekBarChangeListener(this);
        mAdjustBar2.setVisibility(View.GONE);
        mAdjustBar3 = mAdjustPanel.findViewById(R.id.sb_adjust_bar_3);
        mAdjustBar3.setOnSeekBarChangeListener(this);
        mAdjustBar3.setVisibility(View.GONE);
        mAdjustPanel.findViewById(R.id.vw_control_params).setVisibility(View.GONE);
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

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mParent.addView(mAdjustPanel, params);
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
            if (mRGBOp != null) {
                operatorList.add(mRGBOp);
            }
            if (mColorBalanceOp != null) {
                operatorList.add(mColorBalanceOp);
            }
            IEManager.getInstance().removeOperator(0, operatorList);
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
            mRGBOp = null;
            mColorBalanceOp = null;
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
                    IEManager.getInstance().addOperator(0, mBrightnessOp);
                }

                mAdjustValue1.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 2.0f) / seekBar.getMax()));
                mBrightnessOp.setBrightness(((progress - seekBar.getMax() / 2) * 2.0f) / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_EXPOSURE:
                if (mExposureOp == null) {
                    mExposureOp = new ExposureAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mExposureOp);
                }

                mAdjustValue1.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax()));
                mExposureOp.setExposure(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_GAMMA:
                if (mGammaOp == null) {
                    mGammaOp = new GammaAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mGammaOp);
                }

                mAdjustValue1.setText(String.valueOf(progress * 4.0f / seekBar.getMax()));
                mGammaOp.setGamma(progress * 4.0f / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_CONTRAST:
                if (mContrastOp == null) {
                    mContrastOp = new ContrastAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mContrastOp);
                }

                mAdjustValue1.setText(String.valueOf(progress * 2.0f / seekBar.getMax()));
                mContrastOp.setContrast(progress * 2.0f / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SATURATION:
                if (mSaturationOp == null) {
                    mSaturationOp = new SaturationAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mSaturationOp);
                }
                mAdjustValue1.setText(String.valueOf(progress * 2.0f / seekBar.getMax()));
                mSaturationOp.setSaturation(progress * 2.0f / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHARPEN:
                if (mSharpenOp == null) {
                    mSharpenOp = new SharpenAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mSharpenOp);
                }
                mAdjustValue1.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 8.0f) / seekBar.getMax()));
                mSharpenOp.setSharpness(((progress - seekBar.getMax() / 2) * 8.0f) / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DARK_CORNER:
                if (mVignetteOp == null) {
                    mVignetteOp = new VignetteOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mVignetteOp);
                }
                mAdjustValue1.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                mVignetteOp.setStart(progress * 1.0f / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHADOW:
                if (mShadowOp == null) {
                    mShadowOp = new ShadowAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mShadowOp);
                }
                mAdjustValue1.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                mShadowOp.setShadow(progress * 1.0f / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_HIGHLIGHT:
                if (mHighlightOp == null) {
                    mHighlightOp = new HighlightAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mHighlightOp);
                }
                mAdjustValue1.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                mHighlightOp.setHighlight(progress * 1.0f / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_COLOR_TEMP:
                if (mTempOp == null) {
                    mTempOp = new TempAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mTempOp);
                }
                mAdjustValue1.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax()));
                mTempOp.setTemperature(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_TONE:
                if (mTintOp == null) {
                    mTintOp = new TintAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mTintOp);
                }
                mAdjustValue1.setText(String.valueOf(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax()));
                mTintOp.setTint(((progress - seekBar.getMax() / 2) * 4.0f) / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DENOISE:
                if (mDenoiseOp == null) {
                    mDenoiseOp = new DenoiseOperator();
                    IEManager.getInstance().addOperator(0, mDenoiseOp);
                }
                mAdjustValue1.setText(String.valueOf(progress * 100.0f / seekBar.getMax()));
                mDenoiseOp.setExponent(progress * 100.0f / seekBar.getMax());
                break;
            case AdjustListAdapter.AdjustItem.TYPE_RGB:
                if (mRGBOp == null) {
                    mRGBOp = new RGBAdjustOperator.Builder().build();
                    IEManager.getInstance().addOperator(0, mRGBOp);
                }
                switch (seekBar.getId()) {
                    case R.id.sb_adjust_bar_1:
                        mAdjustValue1.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                        mRGBOp.setRed(progress * 1.0f / seekBar.getMax());
                        break;
                    case R.id.sb_adjust_bar_2:
                        mAdjustValue2.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                        mRGBOp.setGreen(progress * 1.0f / seekBar.getMax());
                        break;
                    case R.id.sb_adjust_bar_3:
                        mAdjustValue3.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                        mRGBOp.setBlue(progress * 1.0f / seekBar.getMax());
                        break;
                    default:
                        break;
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_COLOR_BALANCE:
                if (mColorBalanceOp == null) {
                    mColorBalanceOp = new ColorBalanceOperator();
                    IEManager.getInstance().addOperator(0, mColorBalanceOp);
                }
                switch (seekBar.getId()) {
                    case R.id.sb_adjust_bar_1:
                        mAdjustValue1.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                        mColorBalanceOp.setRedShift(progress * 1.0f / seekBar.getMax());
                        break;
                    case R.id.sb_adjust_bar_2:
                        mAdjustValue2.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                        mColorBalanceOp.setGreenShift(progress * 1.0f / seekBar.getMax());
                        break;
                    case R.id.sb_adjust_bar_3:
                        mAdjustValue3.setText(String.valueOf(progress * 1.0f / seekBar.getMax()));
                        mColorBalanceOp.setBlueShift(progress * 1.0f / seekBar.getMax());
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        IEManager.getInstance().renderClip(0);
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
        mAdjustPanel.findViewById(R.id.vw_control_params).setVisibility(View.VISIBLE);
        mAdjustPanel.findViewById(R.id.vw_adjust_value_1).setVisibility(View.VISIBLE);
        mAdjustPanel.findViewById(R.id.vw_adjust_value_2).setVisibility(View.GONE);
        mAdjustPanel.findViewById(R.id.vw_adjust_value_3).setVisibility(View.GONE);
        mAdjustBar1.setVisibility(View.VISIBLE);
        mAdjustBar2.setVisibility(View.GONE);
        mAdjustBar3.setVisibility(View.GONE);
        mAdjustName1.setText(adjustItem.mNameStrId);
        switch (mCurType) {
            case AdjustListAdapter.AdjustItem.TYPE_BRIGHTNESS:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mBrightnessOp == null) {
                    mAdjustValue1.setText(String.valueOf(0.0f));
                    mAdjustBar1.setProgress(mAdjustBar1.getMax() / 2);
                } else {
                    mAdjustValue1.setText(String.valueOf(mBrightnessOp.getBrightness()));
                    mAdjustBar1.setProgress(
                            (int) (mBrightnessOp.getBrightness() * mAdjustBar1.getMax() / 2 + mAdjustBar1.getMax() / 2));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_EXPOSURE:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mExposureOp == null) {
                    mAdjustValue1.setText(String.valueOf(0.0));
                    mAdjustBar1.setProgress(mAdjustBar1.getMax() / 2);
                } else {
                    mAdjustValue1.setText(String.valueOf(mExposureOp.getExposure()));
                    mAdjustBar1.setProgress((int) ((mExposureOp.getExposure() + 2) * 0.25f * mAdjustBar1.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_GAMMA:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mGammaOp == null) {
                    mAdjustValue1.setText(String.valueOf(1.0));
                    mAdjustBar1.setProgress((int) (mAdjustBar1.getMax() * 0.25f));
                } else {
                    mAdjustValue1.setText(String.valueOf(mGammaOp.getGamma()));
                    mAdjustBar1.setProgress((int) (mGammaOp.getGamma() * 0.25f * mAdjustBar1.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_CONTRAST:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mContrastOp == null) {
                    mAdjustValue1.setText(String.valueOf(1.0f));
                    mAdjustBar1.setProgress(mAdjustBar1.getMax() / 2);
                } else {
                    mAdjustValue1.setText(String.valueOf(mContrastOp.getContrast()));
                    mAdjustBar1.setProgress((int) (mContrastOp.getContrast() * 0.5f * mAdjustBar1.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SATURATION:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mSaturationOp == null) {
                    mAdjustValue1.setText(String.valueOf(1.0f));
                    mAdjustBar1.setProgress(mAdjustBar1.getMax() / 2);
                } else {
                    mAdjustValue1.setText(String.valueOf(mSaturationOp.getSaturation()));
                    mAdjustBar1.setProgress((int) (mSaturationOp.getSaturation() * 0.5f * mAdjustBar1.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHARPEN:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mSharpenOp == null) {
                    mAdjustValue1.setText(String.valueOf(0.0f));
                    mAdjustBar1.setProgress(mAdjustBar1.getMax() / 2);
                } else {
                    mAdjustValue1.setText(String.valueOf(mSharpenOp.getSharpness()));
                    mAdjustBar1.setProgress((int) ((mSharpenOp.getSharpness() + 4) * 0.125f * mAdjustBar1.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DARK_CORNER:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mVignetteOp == null) {
                    mAdjustValue1.setText(String.valueOf(0.3f));
                    mAdjustBar1.setProgress(mAdjustBar1.getMax() / 2);
                } else {
                    mAdjustValue1.setText(String.valueOf(mVignetteOp.getStart()));
                    mAdjustBar1.setProgress((int) (mVignetteOp.getStart() * mAdjustBar1.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_SHADOW:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mShadowOp == null) {
                    mAdjustValue1.setText(String.valueOf(0));
                    mAdjustBar1.setProgress(0);
                } else {
                    mAdjustValue1.setText(String.valueOf(mShadowOp.getShadow()));
                    mAdjustBar1.setProgress((int) (mShadowOp.getShadow() * mAdjustBar1.getMax() / 100f));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_HIGHLIGHT:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mHighlightOp == null) {
                    mAdjustValue1.setText(String.valueOf(0));
                    mAdjustBar1.setProgress(0);
                } else {
                    mAdjustValue1.setText(String.valueOf(mHighlightOp.getHighlight()));
                    mAdjustBar1.setProgress((int) (mHighlightOp.getHighlight() * mAdjustBar1.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_COLOR_TEMP:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mTempOp == null) {
                    mAdjustValue1.setText(String.valueOf(0.0));
                    mAdjustBar1.setProgress(mAdjustBar1.getMax() / 2);
                } else {
                    mAdjustValue1.setText(String.valueOf(mTempOp.getTemperature()));
                    mAdjustBar1.setProgress((int) ((mTempOp.getTemperature() + 2) * 0.25f * mAdjustBar1.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_TONE:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mTintOp == null) {
                    mAdjustValue1.setText(String.valueOf(0.0));
                    mAdjustBar1.setProgress(mAdjustBar1.getMax() / 2);
                } else {
                    mAdjustValue1.setText(String.valueOf(mTintOp.getTint()));
                    mAdjustBar1.setProgress((int) ((mTintOp.getTint() + 2) * 0.25f * mAdjustBar1.getMax()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_DENOISE:
                mAdjustBar1.setVisibility(View.VISIBLE);
                if (mDenoiseOp == null) {
                    mAdjustValue1.setText(String.valueOf(5.0));
                    mAdjustBar1.setProgress((int) (mAdjustBar1.getMax() * 0.05f));
                } else {
                    mAdjustValue1.setText(String.valueOf(mDenoiseOp.getExponent()));
                    mAdjustBar1.setProgress((int) (mAdjustBar1.getMax() * mDenoiseOp.getExponent() / 100f));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_RGB:
                mAdjustPanel.findViewById(R.id.vw_adjust_value_1).setVisibility(View.VISIBLE);
                mAdjustPanel.findViewById(R.id.vw_adjust_value_2).setVisibility(View.VISIBLE);
                mAdjustPanel.findViewById(R.id.vw_adjust_value_3).setVisibility(View.VISIBLE);
                mAdjustName1.setText(mContext.getResources().getText(R.string.edit_adjust_curve_red));
                mAdjustName2.setText(mContext.getResources().getText(R.string.edit_adjust_curve_green));
                mAdjustName3.setText(mContext.getResources().getText(R.string.edit_adjust_curve_blue));
                mAdjustBar1.setVisibility(View.VISIBLE);
                mAdjustBar2.setVisibility(View.VISIBLE);
                mAdjustBar3.setVisibility(View.VISIBLE);
                if (mRGBOp == null) {
                    mAdjustValue1.setText(String.valueOf(1.0));
                    mAdjustValue2.setText(String.valueOf(1.0));
                    mAdjustValue3.setText(String.valueOf(1.0));
                    mAdjustBar1.setProgress(mAdjustBar1.getMax());
                    mAdjustBar2.setProgress(mAdjustBar2.getMax());
                    mAdjustBar3.setProgress(mAdjustBar3.getMax());
                } else {
                    mAdjustValue1.setText(String.valueOf(mRGBOp.getRed()));
                    mAdjustValue2.setText(String.valueOf(mRGBOp.getGreen()));
                    mAdjustValue3.setText(String.valueOf(mRGBOp.getBlue()));
                    mAdjustBar1.setProgress((int) (mAdjustBar1.getMax() * mRGBOp.getRed()));
                    mAdjustBar2.setProgress((int) (mAdjustBar2.getMax() * mRGBOp.getGreen()));
                    mAdjustBar3.setProgress((int) (mAdjustBar3.getMax() * mRGBOp.getBlue()));
                }
                break;
            case AdjustListAdapter.AdjustItem.TYPE_COLOR_BALANCE:
                mAdjustPanel.findViewById(R.id.vw_adjust_value_1).setVisibility(View.VISIBLE);
                mAdjustPanel.findViewById(R.id.vw_adjust_value_2).setVisibility(View.VISIBLE);
                mAdjustPanel.findViewById(R.id.vw_adjust_value_3).setVisibility(View.VISIBLE);
                mAdjustName1.setText(mContext.getResources().getText(R.string.edit_adjust_curve_red));
                mAdjustName2.setText(mContext.getResources().getText(R.string.edit_adjust_curve_green));
                mAdjustName3.setText(mContext.getResources().getText(R.string.edit_adjust_curve_blue));
                mAdjustBar1.setVisibility(View.VISIBLE);
                mAdjustBar2.setVisibility(View.VISIBLE);
                mAdjustBar3.setVisibility(View.VISIBLE);
                if (mColorBalanceOp == null) {
                    mAdjustValue1.setText(String.valueOf(0.0));
                    mAdjustValue2.setText(String.valueOf(0.0));
                    mAdjustValue3.setText(String.valueOf(0.0));
                    mAdjustBar1.setProgress(0);
                    mAdjustBar2.setProgress(0);
                    mAdjustBar3.setProgress(0);
                } else {
                    mAdjustValue1.setText(String.valueOf(mColorBalanceOp.getRedShift()));
                    mAdjustValue2.setText(String.valueOf(mColorBalanceOp.getGreenShift()));
                    mAdjustValue3.setText(String.valueOf(mColorBalanceOp.getBlueShift()));
                    mAdjustBar1.setProgress((int) (mAdjustBar1.getMax() * mColorBalanceOp.getRedShift()));
                    mAdjustBar2.setProgress((int) (mAdjustBar2.getMax() * mColorBalanceOp.getGreenShift()));
                    mAdjustBar3.setProgress((int) (mAdjustBar3.getMax() * mColorBalanceOp.getBlueShift()));
                }
                break;
            default:
                mAdjustValue1.setText("0");
                mAdjustPanel.findViewById(R.id.vw_adjust_value_1).setVisibility(View.GONE);
                mAdjustPanel.findViewById(R.id.vw_adjust_value_2).setVisibility(View.GONE);
                mAdjustPanel.findViewById(R.id.vw_adjust_value_3).setVisibility(View.GONE);
                mAdjustBar1.setVisibility(View.GONE);
                mAdjustBar2.setVisibility(View.GONE);
                mAdjustBar3.setVisibility(View.GONE);
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
