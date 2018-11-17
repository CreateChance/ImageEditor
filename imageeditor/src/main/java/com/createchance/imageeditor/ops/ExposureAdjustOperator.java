package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.ExposureAdjustDrawer;

/**
 * Exposure adjust operator.
 *
 * @author gaochao02
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
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new ExposureAdjustDrawer();
        }
        mDrawer.setExposure(mExposure);
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
