package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.SaturationAdjustDrawer;

/**
 * Saturation adjust operator.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class SaturationAdjustOperator extends AbstractOperator {
    private static final String TAG = "SaturationAdjustOperato";

    private final float MAX_SATURATION = 2.0f;
    private final float MIN_SATURATION = 0.0f;

    private float mSaturation = 1.0f;

    private SaturationAdjustDrawer mDrawer;

    private SaturationAdjustOperator() {
        super(SaturationAdjustOperator.class.getSimpleName(), OP_SATURATION_ADJUST);
    }

    @Override
    public boolean checkRational() {
        return mSaturation >= MIN_SATURATION && mSaturation <= MAX_SATURATION;
    }

    @Override
    public void exec() {
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new SaturationAdjustDrawer();
        }
        mDrawer.setSaturation(mSaturation);
        mDrawer.draw(mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public float getSaturation() {
        return mSaturation;
    }

    public void setSaturation(float saturation) {
        this.mSaturation = saturation;
    }

    public static class Builder {
        private SaturationAdjustOperator operator = new SaturationAdjustOperator();

        public Builder saturation(float saturation) {
            operator.mSaturation = saturation;

            return this;
        }

        public SaturationAdjustOperator build() {
            return operator;
        }
    }
}
