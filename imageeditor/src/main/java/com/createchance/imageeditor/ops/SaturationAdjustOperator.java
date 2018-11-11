package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

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
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new SaturationAdjustDrawer();
        }
        mDrawer.setSaturation(mSaturation);
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
