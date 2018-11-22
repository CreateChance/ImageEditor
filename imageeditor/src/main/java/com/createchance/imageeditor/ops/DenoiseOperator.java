package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.DenoiseDrawer;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/17
 */
public class DenoiseOperator extends AbstractOperator {

    private static final String TAG = "DenoiseOperator";

    private int mWidth, mHeight;

    private float mExponent = 5.0f;

    private DenoiseDrawer mDrawer;

    public DenoiseOperator() {
        super(DenoiseOperator.class.getSimpleName(), OP_DENOISE);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new DenoiseDrawer();
        }
        mDrawer.setResolution(mWorker.getSurfaceWidth(), mWorker.getSurfaceHeight());
        mDrawer.setExponent(mExponent);
        mDrawer.draw(mWorker.getTextures()[mWorker.getInputTextureIndex()],
                0,
                0,
                mWorker.getImgOriginWidth(),
                mWorker.getImgOriginHeight());
        mWorker.bindDefaultFrameBuffer();
        mWorker.swapTexture();
    }

    public float getExponent() {
        return mExponent;
    }

    public void setExponent(float exponent) {
        this.mExponent = exponent;
    }
}
