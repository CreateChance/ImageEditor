package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.ShadowAdjustDrawer;

/**
 * Shadow adjust operator.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class ShadowAdjustOperator extends AbstractOperator {
    private static final String TAG = "ShadowAdjustOperator";

    private final float MAX_SHADOW = 100.0f;
    private final float MIN_SHADOW = 0.0f;

    private float mShadow = 0.0f;

    private ShadowAdjustDrawer mDrawer;

    private ShadowAdjustOperator() {
        super(ShadowAdjustOperator.class.getName(), OP_SHADOW);
    }

    @Override
    public boolean checkRational() {
        return mShadow >= MIN_SHADOW && mShadow <= MAX_SHADOW;
    }

    public void setShadow(float shadow) {
        mShadow = shadow;
    }

    public float getShadow() {
        return mShadow;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new ShadowAdjustDrawer();
        }
        mDrawer.setShadow(mShadow);
        mDrawer.draw(mWorker.getTextures()[mWorker.getInputTextureIndex()],
                0,
                0,
                mWorker.getImgOriginWidth(),
                mWorker.getImgOriginHeight());
        mWorker.bindDefaultFrameBuffer();
        mWorker.swapTexture();
    }

    public static class Builder {
        private ShadowAdjustOperator operator = new ShadowAdjustOperator();

        public Builder shadow(float shadow) {
            operator.mShadow = shadow;

            return this;
        }

        public ShadowAdjustOperator build() {
            return operator;
        }
    }
}
