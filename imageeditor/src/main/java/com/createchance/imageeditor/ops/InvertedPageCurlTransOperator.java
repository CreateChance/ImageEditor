package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.InvertedPageCurlTransDrawer;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/12/23
 */
public class InvertedPageCurlTransOperator extends AbstractOperator {

    private static final String TAG = "InvertedPageCurlTransOp";

    private InvertedPageCurlTransDrawer mDrawer;

    private float mProgress;

    public InvertedPageCurlTransOperator() {
        super(InvertedPageCurlTransOperator.class.getSimpleName(), OP_TRANS_INVERTED_PAGE_CURL);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new InvertedPageCurlTransDrawer();
        }

        mDrawer.setProgress(mProgress);

        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(mWorker.getImgShowLeft(),
                mWorker.getImgShowBottom(),
                mWorker.getImgShowWidth(),
                mWorker.getImgShowHeight());
        mDrawer.draw(mWorker.getTextures()[mWorker.getInputTextureIndex()],
                mWorker.getTextures()[mWorker.getInputTextureIndex()],
                0,
                0,
                mWorker.getSurfaceWidth(),
                mWorker.getSurfaceHeight());
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
        mWorker.bindDefaultFrameBuffer();
        mWorker.swapTexture();
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }
}
