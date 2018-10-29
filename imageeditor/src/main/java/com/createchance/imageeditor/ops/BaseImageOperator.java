package com.createchance.imageeditor.ops;

import android.graphics.Bitmap;

import com.createchance.imageeditor.BaseImageDrawer;
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
    private int mWidth, mHeight;
    private float mScaleFactor = 1.0f;
    private int mRotation;
    private int mPosX, mPosY;
    private int mTextureId;

    private BaseImageDrawer mDrawer;

    private BaseImageOperator() {
        super(BaseImageOperator.class.getSimpleName(), OP_BASE_IMAGE);
    }

    @Override
    public boolean checkRational() {
        return mImage != null
                && !mImage.isRecycled()
                && mWidth >= 0
                && mHeight >= 0
                && mScaleFactor >= 0
                && mRotation >= 0
                && mRotation <= 360;
    }

    @Override
    public void exec() {
        if (mDrawer == null) {
            mDrawer = new BaseImageDrawer(mScaleFactor, false, true);
            mTextureId = OpenGlUtils.loadTexture(mImage, OpenGlUtils.NO_TEXTURE, false);
        }
        mDrawer.draw(mTextureId,
                mPosX,
                mPosY,
                mWidth,
                mHeight);
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public void setPosition(int x, int y) {
        mPosX = x;
        mPosY = y;
    }

    public static class Builder {
        private BaseImageOperator operator = new BaseImageOperator();

        public Builder image(Bitmap image) {
            operator.mImage = image;
            operator.mWidth = image.getWidth();
            operator.mHeight = image.getHeight();

            return this;
        }

        public Builder scaleFactor(float scaleFactor) {
            operator.mScaleFactor = scaleFactor;

            return this;
        }

        public Builder rotation(int rotation) {
            operator.mRotation = rotation;

            return this;
        }

        public BaseImageOperator build() {
            return operator;
        }
    }
}
