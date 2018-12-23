package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.WindowSliceTransDrawer;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/12/23
 */
public class WindowSliceTransOperator extends AbstractOperator {

    private static final String TAG = "WindowSliceTransOperato";

    private WindowSliceTransDrawer mDrawer;

    private float mProgress;
    private float mCount = 10.0f;
    private float mSmoothness = 0.5f;

    public WindowSliceTransOperator() {
        super(WindowSliceTransOperator.class.getSimpleName(), OP_TRANS_WINDOW_SLICE);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new WindowSliceTransDrawer();
        }

        mDrawer.setProgress(mProgress);
        mDrawer.setCount(mCount);
        mDrawer.setSmoothness(mSmoothness);

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
