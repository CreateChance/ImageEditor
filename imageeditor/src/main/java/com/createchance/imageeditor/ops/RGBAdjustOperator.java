package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.RGBAdjustDrawer;

/**
 * RGB channel adjust operator.
 *
 * @author createchance
 * @date 2018/11/25
 */
public class RGBAdjustOperator extends AbstractOperator {

    private static final String TAG = "RGBAdjustOperator";

    private final float MAX_ADJUST = 1.0f;
    private final float MIN_ADJUST = 0.0f;

    private float mRed = 1.0f, mGreen = 1.0f, mBlue = 1.0f;

    private RGBAdjustDrawer mDrawer;

    private RGBAdjustOperator() {
        super(RGBAdjustOperator.class.getSimpleName(), OP_RGB);
    }

    @Override
    public boolean checkRational() {
        return mRed >= MIN_ADJUST && mRed <= MAX_ADJUST &&
                mGreen >= MIN_ADJUST && mGreen <= MAX_ADJUST &&
                mBlue >= MIN_ADJUST && mBlue <= MAX_ADJUST;
    }

    @Override
    public void exec() {
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new RGBAdjustDrawer();
        }
        mDrawer.setRed(mRed);
        mDrawer.setGreen(mGreen);
        mDrawer.setBlue(mBlue);
        mDrawer.draw(mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public float getRed() {
        return mRed;
    }

    public void setRed(float mRed) {
        this.mRed = mRed;
    }

    public float getGreen() {
        return mGreen;
    }

    public void setGreen(float mGreen) {
        this.mGreen = mGreen;
    }

    public float getBlue() {
        return mBlue;
    }

    public void setBlue(float mBlue) {
        this.mBlue = mBlue;
    }

    public static class Builder {
        private RGBAdjustOperator operator = new RGBAdjustOperator();

        public Builder red(float red) {
            operator.mRed = red;

            return this;
        }

        public Builder green(float green) {
            operator.mGreen = green;

            return this;
        }

        public Builder blue(float blue) {
            operator.mBlue = blue;

            return this;
        }

        public RGBAdjustOperator build() {
            return operator;
        }
    }
}
