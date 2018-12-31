package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.VignetteDrawer;

/**
 * Vignetting effect operator.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class VignetteOperator extends AbstractOperator {

    private static final String TAG = "VignetteOperator";

    private float mCenterX = 0.5f, mCenterY = 0.5f;
    private float mRed = 0f, mGreen = 0f, mBlue = 0f;
    private float mStart = 0.3f, mEnd = 0.75f;

    private VignetteDrawer mDrawer;

    private VignetteOperator() {
        super(VignetteOperator.class.getSimpleName(), OP_VIGNETTE);
    }

    @Override
    public boolean checkRational() {
        // TODO: We need to check params here.
        return true;
    }

    @Override
    public void exec() {
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new VignetteDrawer();
        }
        mDrawer.setVignetteCenter(mCenterX, mCenterY);
        mDrawer.setVignetteColor(mRed, mGreen, mBlue);
        mDrawer.setVignetteStart(mStart);
        mDrawer.setVignetteEnd(mEnd);
        mDrawer.draw(mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public float getCenterX() {
        return mCenterX;
    }

    public void setCenterX(float mCenterX) {
        this.mCenterX = mCenterX;
    }

    public float getCenterY() {
        return mCenterY;
    }

    public void setCenterY(float mCenterY) {
        this.mCenterY = mCenterY;
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

    public float getStart() {
        return mStart;
    }

    public void setStart(float mStart) {
        this.mStart = mStart;
    }

    public float getEnd() {
        return mEnd;
    }

    public void setEnd(float mEnd) {
        this.mEnd = mEnd;
    }

    public static class Builder {
        private VignetteOperator operator = new VignetteOperator();

        public Builder center(float x, float y) {
            operator.mCenterX = x;
            operator.mCenterY = y;

            return this;
        }

        public Builder color(float red, float green, float blue) {
            operator.mRed = red;
            operator.mGreen = green;
            operator.mBlue = blue;

            return this;
        }

        public Builder startValue(float start) {
            operator.mStart = start;

            return this;
        }

        public Builder endValue(float end) {
            operator.mEnd = end;

            return this;
        }

        public VignetteOperator build() {
            return operator;
        }
    }
}
