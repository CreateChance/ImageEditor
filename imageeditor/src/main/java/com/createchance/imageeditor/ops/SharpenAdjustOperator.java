package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.SharpenAdjustDrawer;

/**
 * Sharpness adjust operator.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class SharpenAdjustOperator extends AbstractOperator {
    private static final String TAG = "SharpenAdjustOperator";

    private final float MAX_SHARP = 4.0f;
    private final float MIN_SHARP = -4.0f;

    private float mSharpness = 0.0f;

    private SharpenAdjustDrawer mDrawer;

    private SharpenAdjustOperator() {
        super(SharpenAdjustOperator.class.getSimpleName(), OP_SHARPNESS_ADJUST);
    }

    @Override
    public boolean checkRational() {
        return mSharpness >= MIN_SHARP && mSharpness <= MAX_SHARP;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new SharpenAdjustDrawer();
        }
        mDrawer.setImageSizeFactor(mWorker.getImgShowWidth(), mWorker.getImgShowHeight());
        mDrawer.setSharpness(mSharpness);
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

    public float getSharpness() {
        return mSharpness;
    }

    public void setSharpness(float sharpness) {
        this.mSharpness = sharpness;
    }

    public static class Builder {
        private SharpenAdjustOperator operator = new SharpenAdjustOperator();

        public Builder sharpness(float sharpness) {
            operator.mSharpness = sharpness;

            return this;
        }

        public SharpenAdjustOperator build() {
            return operator;
        }
    }
}
