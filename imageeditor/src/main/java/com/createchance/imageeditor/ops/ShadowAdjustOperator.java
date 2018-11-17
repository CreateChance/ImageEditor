package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

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
