package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.ExposureAdjustDrawer;

/**
 * Exposure adjust operator.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class ExposureAdjustOperator extends AbstractOperator {

    private static final String TAG = "ExposureAdjustOperator";

    private final float MIN_EXPOSURE = -2f;
    private final float MAX_EXPOSURE = 2f;

    private float mExposure = 0.0f;

    private ExposureAdjustDrawer mDrawer;

    private ExposureAdjustOperator() {
        super(ExposureAdjustOperator.class.getSimpleName(), OP_EXPOSURE);
    }

    @Override
    public boolean checkRational() {
        return mExposure >= MIN_EXPOSURE && mExposure <= MAX_EXPOSURE;
    }

    @Override
    public void exec() {
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new ExposureAdjustDrawer();
        }
        mDrawer.setExposure(mExposure);
        mDrawer.draw(mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public float getExposure() {
        return mExposure;
    }

    public void setExposure(float exposure) {
        this.mExposure = exposure;
    }

    public static class Builder {
        private ExposureAdjustOperator operator = new ExposureAdjustOperator();

        public Builder exposure(float exposure) {
            operator.mExposure = exposure;

            return this;
        }

        public ExposureAdjustOperator build() {
            return operator;
        }
    }
}
