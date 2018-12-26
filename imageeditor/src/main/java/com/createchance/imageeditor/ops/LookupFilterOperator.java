package com.createchance.imageeditor.ops;

import android.graphics.Bitmap;

import com.createchance.imageeditor.drawers.LookupFilterDrawer;

/**
 * Look up filter operator.
 *
 * @author createchance
 * @date 2018/11/10
 */
public class LookupFilterOperator extends AbstractOperator {

    private static final String TAG = "LookupFilterOperator";

    private float mIntensity = 1.0f;

    private Bitmap mLookupImage;
    private boolean mReloadLookup = true;
    private LookupFilterDrawer mDrawer;

    private LookupFilterOperator() {
        super(LookupFilterOperator.class.getSimpleName(), OP_LOOKUP_FILTER);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new LookupFilterDrawer();
        }

        mDrawer.setIntensity(mIntensity);
        if (mReloadLookup) {
            mReloadLookup = false;
            mDrawer.setLookup(mLookupImage);
        }
        mDrawer.draw(mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public void setIntensity(float intensity) {
        if (mIntensity == intensity) {
            return;
        }

        mIntensity = intensity;
    }

    public void setLookup(Bitmap lookup) {
        if (mLookupImage == lookup) {
            return;
        }

        mLookupImage = lookup;
        mReloadLookup = true;
    }

    public static class Builder {
        private LookupFilterOperator operator = new LookupFilterOperator();

        public Builder lookup(Bitmap lookup) {
            operator.mLookupImage = lookup;

            return this;
        }

        public Builder intensity(float intensity) {
            operator.mIntensity = intensity;

            return this;
        }

        public LookupFilterOperator build() {
            return operator;
        }
    }
}
