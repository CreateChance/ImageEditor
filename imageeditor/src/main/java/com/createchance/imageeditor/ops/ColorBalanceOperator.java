package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.ColorBalanceDrawer;

/**
 * Color balance adjustment operator.
 *
 * @author createchance
 * @date 2018/12/21
 */
public class ColorBalanceOperator extends AbstractOperator {

    private static final String TAG = "ColorBalanceOperator";

    private ColorBalanceDrawer mDrawer;
    private float mRedShift, mGreenShift, mBlueShift;

    public ColorBalanceOperator() {
        super(ColorBalanceOperator.class.getSimpleName(), OP_COLOR_BALANCE_FILTER);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new ColorBalanceDrawer();
        }
        mDrawer.setRedShift(mRedShift);
        mDrawer.setGreenShift(mGreenShift);
        mDrawer.setBlueShift(mBlueShift);
        mDrawer.draw(mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public float getRedShift() {
        return mRedShift;
    }

    public void setRedShift(float mRedShift) {
        this.mRedShift = mRedShift;
    }

    public float getGreenShift() {
        return mGreenShift;
    }

    public void setGreenShift(float mGreenShift) {
        this.mGreenShift = mGreenShift;
    }

    public float getBlueShift() {
        return mBlueShift;
    }

    public void setBlueShift(float mBlueShift) {
        this.mBlueShift = mBlueShift;
    }
}
