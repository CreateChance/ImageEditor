package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.BrightnessAdjustDrawer;

/**
 * Brightness adjust operator.
 *
 * @author createchance
 * @date 2018/11/11
 */
public class BrightnessAdjustOperator extends AbstractOperator {
    private static final String TAG = "BrightnessAdjustOperato";

    private final float MAX_BRIGHTNESS = 1.0f;
    private final float MIN_BRIGHTNESS = -1.0f;

    private float mBrightness = 0.0f;

    private BrightnessAdjustDrawer mDrawer;

    private BrightnessAdjustOperator() {
        super(BrightnessAdjustOperator.class.getSimpleName(), OP_BRIGHTNESS_ADJUST);
    }

    @Override
    public boolean checkRational() {
        return mBrightness >= MIN_BRIGHTNESS && mBrightness <= MAX_BRIGHTNESS;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new BrightnessAdjustDrawer();
        }
        mDrawer.setBrightness(mBrightness);
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

    public float getBrightness() {
        return mBrightness;
    }

    public void setBrightness(float brightness) {
        this.mBrightness = brightness;
    }

    public static class Builder {
        private BrightnessAdjustOperator operator = new BrightnessAdjustOperator();

        public Builder brightness(float brightness) {
            operator.mBrightness = brightness;

            return this;
        }

        public BrightnessAdjustOperator build() {
            return operator;
        }
    }
}
