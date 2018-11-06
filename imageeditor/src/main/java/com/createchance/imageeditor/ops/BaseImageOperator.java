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
    private float mWidthScaleFactor = 1.0f, mHeightScaleFactor = 1.0f;
    private float mFov = 45, mNear = 0.1f, mFar = 100, mTranslateX, mTranslateY, mTranslateZ = 2.5f;
    private float mFlipX = 180, mFlipY = 0, mFlipZ = 0;
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
            mDrawer = new BaseImageDrawer(mWidthScaleFactor, mHeightScaleFactor);
            mTextureId = OpenGlUtils.loadTexture(mImage, OpenGlUtils.NO_TEXTURE, false);
        }
        mDrawer.draw(mTextureId,
                0,
                0,
                mWorker.getSurfaceWidth(),
                mWorker.getSurfaceHeight(),
                mFov,
                mWorker.getSurfaceWidth() * 1.0f / mWorker.getSurfaceHeight(),
                mNear,
                mFar,
                mTranslateX,
                mTranslateY,
                mTranslateZ,
                mFlipX,
                mFlipY,
                mFlipZ);
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setFlipX(float flipX) {
        this.mFlipX = flipX;
    }

    public void setFlipY(float flipY) {
        this.mFlipY = flipY;
    }

    public void setFlipZ(float flipZ) {
        this.mFlipZ = flipZ;
    }

    public void setFov(float mFov) {
        this.mFov = mFov;
    }

    public void setNear(float mNear) {
        this.mNear = mNear;
    }

    public float getNear() {
        return mNear;
    }

    public float getFar() {
        return mFar;
    }

    public void setFar(float mFar) {
        this.mFar = mFar;
    }

    public void setTranslateX(float mTranslateX) {
        this.mTranslateX = mTranslateX;
    }

    public void setTranslateY(float mTranslateY) {
        this.mTranslateY = mTranslateY;
    }

    public void setTranslateZ(float mTranslateZ) {
        this.mTranslateZ = mTranslateZ;
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

        public Builder flip(float flipX, float flipY, float flipZ) {
            operator.mFlipX = flipX;
            operator.mFlipY = flipY;
            operator.mFlipZ = flipZ;

            return this;
        }

        public BaseImageOperator build() {
            return operator;
        }
    }
}
