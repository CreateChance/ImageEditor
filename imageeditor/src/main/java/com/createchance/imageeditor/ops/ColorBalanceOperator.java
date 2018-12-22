package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.ColorBalanceDrawer;

/**
 * Color balance adjustment operator.
 *
 * @author gaochao02
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
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new ColorBalanceDrawer();
        }
        mDrawer.setRedShift(mRedShift);
        mDrawer.setGreenShift(mGreenShift);
        mDrawer.setBlueShift(mBlueShift);
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(mWorker.getImgShowLeft(),
                mWorker.getImgShowBottom(),
                mWorker.getImgShowWidth(),
                mWorker.getImgShowHeight());
        mDrawer.draw(mWorker.getTextures()[mWorker.getInputTextureIndex()],
                0,
                0,
                mWorker.getSurfaceWidth(),
                mWorker.getSurfaceHeight());
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
        mWorker.bindDefaultFrameBuffer();
        mWorker.swapTexture();
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
