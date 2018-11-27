package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.ThreeXThreeSampleDrawer;

/**
 * 3 x 3 sampling operator.
 *
 * @author createchance
 * @date 2018/11/27
 */
public class ThreeXThreeSampleOperator extends AbstractOperator {

    private static final String TAG = "ThreeXThreeSampleOperat";

    public static final float[] ALL_PASS_KERNEL = new float[]{
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f
    };

    public static final float[] MEAN_SAMPLE_KERNEL = new float[]{
            1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
            1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
            1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
    };

    private ThreeXThreeSampleDrawer mDrawer;

    private float mWidthStep, mHeightStep;

    private float[] mSampleKernel = ALL_PASS_KERNEL;

    private ThreeXThreeSampleOperator() {
        super(ThreeXThreeSampleOperator.class.getSimpleName(), OP_3_X_3_SAMPLE);
    }

    @Override
    public boolean checkRational() {
        return mSampleKernel != null && mSampleKernel.length == 9;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new ThreeXThreeSampleDrawer();
        }
        mDrawer.setWidthStep(mWidthStep);
        mDrawer.setHeightStep(mHeightStep);
        mDrawer.setSampleKernel(mSampleKernel);
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

    public float getWidthStep() {
        return mWidthStep;
    }

    public void setWidthStep(float mWidthStep) {
        this.mWidthStep = mWidthStep;
    }

    public float getHeightStep() {
        return mHeightStep;
    }

    public void setHeightStep(float mHeightStep) {
        this.mHeightStep = mHeightStep;
    }

    public float[] getSampleKernel() {
        return mSampleKernel;
    }

    public void setSampleKernel(float[] mSampleKernel) {
        this.mSampleKernel = mSampleKernel;
    }

    public static class Builder {
        private ThreeXThreeSampleOperator operator = new ThreeXThreeSampleOperator();

        public Builder widthStep(float widthStep) {
            operator.mWidthStep = widthStep;

            return this;
        }

        public Builder heightStep(float heightStep) {
            operator.mHeightStep = heightStep;

            return this;
        }

        public Builder sampleKernel(float[] sampleKernel) {
            operator.mSampleKernel = sampleKernel;

            return this;
        }

        public ThreeXThreeSampleOperator build() {
            return operator;
        }
    }
}
