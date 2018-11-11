package com.createchance.imageeditor.ops;

import android.graphics.Bitmap;

import com.createchance.imageeditor.drawers.StickerDrawer;
import com.createchance.imageeditor.filters.OpenGlUtils;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/11/4
 */
public class StickerOperator extends AbstractOperator {

    private static final String TAG = "StickerOperator";

    private StickerDrawer mDrawer;

    private float mScaleFactor = 1.0f;
    private Bitmap mSticker;
    private int mPosX, mPosY;
    private int mWidth, mHeight;
    private boolean mNeedReload = true;
    private int mTextureId;
    private float mAlphaFactor = 1.0f;
    private float mRed = 1.0f, mGreen = 1.0f, mBlue = 1.0f;
    private float mRotateX, mRotateY, mRotateZ;

    private StickerOperator() {
        super(StickerOperator.class.getSimpleName(), OP_STICKER);
    }


    public float getScaleFactor() {
        return mScaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.mScaleFactor = scaleFactor;

        if (mPosX > mWorker.getImgShowRight() - mWidth * mScaleFactor) {
            mPosX = (int) (mWorker.getImgShowRight() - mWidth * mScaleFactor);
        }

        if (mPosY > mWorker.getImgShowTop() - mHeight * mScaleFactor) {
            mPosY = (int) (mWorker.getImgShowTop() - mHeight * mScaleFactor);
        }
    }

    public Bitmap getSticker() {
        return mSticker;
    }

    public void setSticker(Bitmap mSticker) {
        mNeedReload = true;
        this.mSticker = mSticker;
        mWidth = mSticker.getWidth();
        mHeight = mSticker.getHeight();
    }

    public int getPosX() {
        return mPosX;
    }

    public void setPosX(int posX) {
        this.mPosX = posX;
    }

    public int getPosY() {
        return mPosY;
    }

    public void setPosY(int posY) {
        this.mPosY = posY;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setAlphaFactor(float alphaFactor) {
        mAlphaFactor = alphaFactor;
    }

    public void setRed(float red) {
        this.mRed = red;
    }

    public void setGreen(float green) {
        this.mGreen = green;
    }

    public void setBlue(float blue) {
        this.mBlue = blue;
    }

    public void setRotationZ(float rotationZ) {
        this.mRotateZ = rotationZ;
    }

    @Override
    public boolean checkRational() {
        return mSticker != null &&
                mScaleFactor >= 0 &&
                mWidth >= 0 &&
                mHeight >= 0 &&
                mPosX >= 0 &&
                mPosY >= 0;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getInputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new StickerDrawer();
        }

        if (mNeedReload) {
            mNeedReload = false;
            mTextureId = OpenGlUtils.loadTexture(mSticker, OpenGlUtils.NO_TEXTURE);
        }

        mDrawer.setColor(mRed, mGreen, mBlue);
        mDrawer.setAlphaFactor(mAlphaFactor);
        mDrawer.setRotate(mRotateX, mRotateY, mRotateZ);
        mDrawer.draw(mTextureId,
                mPosX,
                mPosY,
                (int) (mWidth * mScaleFactor),
                (int) (mHeight * mScaleFactor));
        mWorker.bindDefaultFrameBuffer();
    }

    public static class Builder {
        private StickerOperator operator = new StickerOperator();

        public Builder position(int x, int y) {
            operator.mPosX = x;
            operator.mPosY = y;

            return this;
        }

        public Builder scaleFactor(float scaleFactor) {
            operator.mScaleFactor = scaleFactor;

            return this;
        }

        public Builder sticker(Bitmap sticker) {
            operator.mSticker = sticker;
            operator.mWidth = sticker.getWidth();
            operator.mHeight = sticker.getHeight();

            return this;
        }

        public Builder alpha(float alpha) {
            operator.mAlphaFactor = alpha;

            return this;
        }

        public Builder color(float red, float green, float blue) {
            operator.mRed = red;
            operator.mGreen = green;
            operator.mBlue = blue;

            return this;
        }

        public Builder rotation(float rotation) {
            operator.mRotateZ = rotation;

            return this;
        }

        public StickerOperator build() {
            return operator;
        }
    }
}
