package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.HighlightAdjustDrawer;

/**
 * Highlight adjust operator.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class HighlightAdjustOperator extends AbstractOperator {

    private static final String TAG = "HighlightAdjustOperator";

    private final float MAX_HIGHLIGHT = 1.0f;
    private final float MIN_HIGHLIGHT = 0.0f;

    private float mHighlight = 0.0f;

    private HighlightAdjustDrawer mDrawer;

    private HighlightAdjustOperator() {
        super(HighlightAdjustOperator.class.getSimpleName(), OP_HIGHLIGHT);
    }

    @Override
    public boolean checkRational() {
        return mHighlight >= MIN_HIGHLIGHT && mHighlight <= MAX_HIGHLIGHT;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new HighlightAdjustDrawer();
        }
        mDrawer.setHighlight(mHighlight);
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

    public float getHighlight() {
        return mHighlight;
    }

    public void setHighlight(float highlight) {
        this.mHighlight = highlight;
    }

    public static class Builder {
        private HighlightAdjustOperator operator = new HighlightAdjustOperator();

        public Builder highlight(float highlight) {
            operator.mHighlight = highlight;

            return this;
        }

        public HighlightAdjustOperator build() {
            return operator;
        }
    }
}
