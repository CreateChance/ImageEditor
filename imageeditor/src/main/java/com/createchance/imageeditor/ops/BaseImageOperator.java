package com.createchance.imageeditor.ops;

import android.graphics.Bitmap;

import com.createchance.imageeditor.drawers.BaseImageDrawer;
import com.createchance.imageeditor.filters.OpenGlUtils;

/**
 * Base image operator.
 *
 * @author createchance
 * @date 2018-10-29
 */
public class BaseImageOperator extends AbstractOperator {

    private static final String TAG = "BaseImageOperator";

    private Bitmap mImage;
    private float mWidthScaleFactor = 1.0f, mHeightScaleFactor = 1.0f;
    private int mTextureId;

    private BaseImageDrawer mDrawer;

    private BaseImageOperator() {
        super(BaseImageOperator.class.getSimpleName(), OP_BASE_IMAGE);
    }

    @Override
    public boolean checkRational() {
        return mImage != null
                && !mImage.isRecycled()
                && mWidthScaleFactor >= 0
                && mHeightScaleFactor >= 0;
    }

    @Override
    public void exec() {
        if (mDrawer == null) {
            mWidthScaleFactor = mWorker.getImgShowWidth() * 1.0f / mWorker.getSurfaceWidth();
            mHeightScaleFactor = mWorker.getImgShowHeight() * 1.0f / mWorker.getSurfaceHeight();
            mDrawer = new BaseImageDrawer(mWidthScaleFactor, mHeightScaleFactor, false);
            mTextureId = OpenGlUtils.loadTexture(mImage, OpenGlUtils.NO_TEXTURE, false);
        }
        mDrawer.draw(mTextureId,
                0,
                0,
                mWorker.getSurfaceWidth(),
                mWorker.getSurfaceHeight());
    }

    public Bitmap getImage() {
        return mImage;
    }

    public static class Builder {
        private BaseImageOperator operator = new BaseImageOperator();

        public Builder image(Bitmap image) {
            operator.mImage = image;

            return this;
        }

        public Builder scaleFactor(float widthScaleFactor, float heightScaleHeight) {
            operator.mWidthScaleFactor = widthScaleFactor;
            operator.mHeightScaleFactor = heightScaleHeight;

            return this;
        }

        public BaseImageOperator build() {
            return operator;
        }
    }
}
