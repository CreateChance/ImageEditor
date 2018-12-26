package com.createchance.imageeditor.ops;

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
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new InvertedPageCurlTransDrawer();
        }

        mDrawer.setProgress(mProgress);

        mDrawer.draw(mContext.getInputTextureId(),
                mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }
}
