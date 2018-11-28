package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.BokehFilterDrawer;

/**
 * Bokeh filter operator.
 *
 * @author gaochao02
 * @date 2018/11/28
 */
public class BokehFilterOperator extends AbstractOperator {

    private static final String TAG = "BokehFilterOperator";

    private BokehFilterDrawer mDrawer;

    private float mRadius = 0f;

    public BokehFilterOperator() {
        super(BokehFilterOperator.class.getSimpleName(), OP_BOKEH_FILTER);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new BokehFilterDrawer();
        }
        mDrawer.setResolution(mWorker.getImgShowWidth(), mWorker.getImgShowHeight());
        mDrawer.setRadius(mRadius);
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

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
    }
}
