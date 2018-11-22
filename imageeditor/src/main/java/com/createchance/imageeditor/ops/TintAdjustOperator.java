package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.TintAdjustDrawer;

/**
 * Tint adjust operator.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class TintAdjustOperator extends AbstractOperator {

    private static final String TAG = "TintAdjustOperator";

    private final float MIN_TINT = -2.0f;
    private final float MAX_TINT = 2.0f;

    private float mTint = 0.0f;

    private TintAdjustDrawer mDrawer;

    private TintAdjustOperator() {
        super(TintAdjustOperator.class.getSimpleName(), OP_TINT);
    }

    @Override
    public boolean checkRational() {
        return mTint >= MIN_TINT && mTint <= MAX_TINT;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new TintAdjustDrawer();
        }
        mDrawer.setTint(mTint);
        mDrawer.draw(mWorker.getTextures()[mWorker.getInputTextureIndex()],
                0,
                0,
                mWorker.getImgOriginWidth(),
                mWorker.getImgOriginHeight());
        mWorker.bindDefaultFrameBuffer();
        mWorker.swapTexture();
    }

    public float getTint() {
        return mTint;
    }

    public void setTint(float tint) {
        this.mTint = tint;
    }

    public static class Builder {
        private TintAdjustOperator operator = new TintAdjustOperator();

        public Builder tint(float tint) {
            operator.mTint = tint;

            return this;
        }

        public TintAdjustOperator build() {
            return operator;
        }
    }
}
