package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.BokehFilterDrawer;

/**
 * Bokeh filter operator.
 *
 * @author createchance
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
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new BokehFilterDrawer();
        }
        mDrawer.setResolution(mContext.getRenderWidth(), mContext.getRenderHeight());
        mDrawer.setRadius(mRadius);
        mDrawer.draw(mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
    }
}
