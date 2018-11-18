package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.DenoiseDrawer;

/**
 * ${DESC}
 *
 * @author gaochao02
 * @date 2018/11/17
 */
public class DenoiseOperator extends AbstractOperator {

    private static final String TAG = "DenoiseOperator";

    private int mWidth, mHeight;

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
        mDrawer.setSketchSize(mWorker.getImgShowWidth(), mWorker.getImgShowHeight());
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
}
