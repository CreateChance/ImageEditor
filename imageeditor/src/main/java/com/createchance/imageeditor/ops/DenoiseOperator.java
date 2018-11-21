package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

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

    public float getExponent() {
        return mExponent;
    }

    public void setExponent(float exponent) {
        this.mExponent = exponent;
    }
}
