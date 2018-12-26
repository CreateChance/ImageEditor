package com.createchance.imageeditor.ops;

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

    public static final float[] SIGMA_1_5_GAUSSIAN_SAMPLE_KERNEL = new float[]{
            0.0947416f, 0.118318f, 0.0947416f,
            0.118318f, 0.147716f, 0.118318f,
            0.0947416f, 0.118318f, 0.0947416f,
    };

    public static final float[] LAPLACIAN_SAMPLE_FILTER = new float[]{
            0.5f, 1.0f, 0.5f,
            1.0f, -6.0f, 1.0f,
            0.5f, 1.0f, 0.5f
    };

    private ThreeXThreeSampleDrawer mDrawer;

    private float mWidthStep, mHeightStep;

    private int mRepeatTimes = 1;

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
        if (mDrawer == null) {
            mDrawer = new ThreeXThreeSampleDrawer();
        }
        mDrawer.setWidthStep(1.0f / mContext.getRenderWidth());
        mDrawer.setHeightStep(1.0f / mContext.getRenderHeight());
        mDrawer.setSampleKernel(mSampleKernel);
        for (int i = 0; i < mRepeatTimes; i++) {
            mContext.attachOffScreenTexture(mContext.getOutputTextureId());
            mDrawer.draw(mContext.getInputTextureId(),
                    0,
                    0,
                    mContext.getSurfaceWidth(),
                    mContext.getSurfaceHeight());
            mContext.swapTexture();
        }
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

    public int getRepeatTimes() {
        return mRepeatTimes;
    }

    public void setRepeatTimes(int mRepeatTimes) {
        this.mRepeatTimes = mRepeatTimes;
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

    public static float[] generateGaussianKernel(double sigma) {
        float[] kernel = new float[9];

        kernel[0] = (float) gaussianDistribution(1.0, 1.0, sigma);
        kernel[1] = (float) gaussianDistribution(0.0, 1.0, sigma);
        kernel[2] = kernel[0];

        kernel[3] = (float) gaussianDistribution(1.0, 0.0, sigma);
        kernel[4] = (float) gaussianDistribution(0, 0, sigma);
        kernel[5] = kernel[2];

        kernel[6] = kernel[0];
        kernel[7] = kernel[1];
        kernel[8] = kernel[2];

        float total = 0;
        for (int i = 0; i < kernel.length; i++) {
            total += kernel[i];
        }
        for (int i = 0; i < kernel.length; i++) {
            kernel[i] = kernel[i] / total;
        }

        return kernel;
    }

    private static double gaussianDistribution(double x, double y, double sigma) {
        return Math.exp(-(x * x + y * y) / (2 * sigma * sigma)) / (2 * Math.PI * sigma * sigma);
    }
}
