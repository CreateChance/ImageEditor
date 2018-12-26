package com.createchance.imageeditor.ops;

import android.util.Log;

import com.createchance.imageeditor.drawers.FiveXFiveSampleDrawer;

/**
 * 5 x 5 sample operator.
 *
 * @author createchance
 * @date 2018/12/2
 */
public class FiveXFiveSampleOperator extends AbstractOperator {

    private static final String TAG = "FiveXFiveSampleOperator";

    public static final float[] ALL_PASS_KERNEL = new float[]{
            0f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f,
    };

    public static final float[] MEAN_FILTER_KERNEL = new float[]{
            1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f,
            1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f,
            1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f,
            1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f,
            1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f, 1.0f / 25.0f,
    };

    private FiveXFiveSampleDrawer mDrawer;

    private float[] mSampleKernel = ALL_PASS_KERNEL;

    private int mRepeatTimes = 1;

    private FiveXFiveSampleOperator() {
        super(FiveXFiveSampleOperator.class.getSimpleName(), OP_5_X_5_SAMPLE);
    }

    @Override
    public boolean checkRational() {
        return mSampleKernel != null && mSampleKernel.length == 25;
    }

    @Override
    public void exec() {
        if (mDrawer == null) {
            mDrawer = new FiveXFiveSampleDrawer();
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

    public static float[] generateGaussianKernel(double sigma) {
        float[] kernel = new float[25];

        kernel[0] = (float) gaussianDistribution(2, 2, sigma);
        kernel[1] = (float) gaussianDistribution(1, 2, sigma);
        kernel[2] = (float) gaussianDistribution(0, 2, sigma);
        kernel[3] = kernel[1];
        kernel[4] = kernel[0];

        kernel[5] = (float) gaussianDistribution(2, 1, sigma);
        kernel[6] = (float) gaussianDistribution(1, 1, sigma);
        kernel[7] = (float) gaussianDistribution(0, 1, sigma);
        kernel[8] = kernel[6];
        kernel[9] = kernel[5];

        kernel[10] = (float) gaussianDistribution(2, 0, sigma);
        kernel[11] = (float) gaussianDistribution(1, 0, sigma);
        kernel[12] = (float) gaussianDistribution(0, 0, sigma);
        kernel[13] = kernel[11];
        kernel[14] = kernel[10];

        kernel[15] = kernel[5];
        kernel[16] = kernel[6];
        kernel[17] = kernel[7];
        kernel[18] = kernel[8];
        kernel[19] = kernel[9];

        kernel[20] = kernel[0];
        kernel[21] = kernel[1];
        kernel[22] = kernel[2];
        kernel[23] = kernel[3];
        kernel[24] = kernel[4];

        float total = 0;
        for (int i = 0; i < kernel.length; i++) {
            Log.d(TAG, "generateGaussianKernel: " + kernel[i]);
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

    public static class Builder {
        private FiveXFiveSampleOperator operator = new FiveXFiveSampleOperator();

        public Builder sampleKernel(float[] sampleKernel) {
            operator.mSampleKernel = sampleKernel;

            return this;
        }

        public FiveXFiveSampleOperator build() {
            return operator;
        }
    }
}
