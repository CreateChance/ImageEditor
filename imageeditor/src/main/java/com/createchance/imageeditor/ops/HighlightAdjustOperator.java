package com.createchance.imageeditor.ops;

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
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new HighlightAdjustDrawer();
        }
        mDrawer.setHighlight(mHighlight);
        mDrawer.draw(mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
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
