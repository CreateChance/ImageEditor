package com.createchance.imageeditor.ops;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.BaseImageDrawer;
import com.createchance.imageeditor.utils.OpenGlUtils;

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

    private int mScissorX = -1, mScissorY = -1, mScissorWidth = -1, mScissorHeight = -1;

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
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getInputTextureIndex()]);
        if (mDrawer == null) {
            mWidthScaleFactor = mScissorWidth * 1.0f / mWorker.getSurfaceWidth();
            mHeightScaleFactor = mScissorHeight * 1.0f / mWorker.getSurfaceHeight();
            mDrawer = new BaseImageDrawer(mWidthScaleFactor, mHeightScaleFactor, false);
            mTextureId = OpenGlUtils.loadTexture(mImage, OpenGlUtils.NO_TEXTURE, false);
        }

        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(mScissorX,
                mScissorY,
                mScissorWidth,
                mScissorHeight);
        mDrawer.draw(mTextureId,
                0,
                0,
                mWorker.getSurfaceWidth(),
                mWorker.getSurfaceHeight());
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
        mWorker.bindDefaultFrameBuffer();
    }

    public void setScissor(int scissorX, int scissorY, int scissorWidth, int scissorHeight) {
        this.mScissorX = scissorX;
        this.mScissorY = scissorY;
        this.mScissorWidth = scissorWidth;
        this.mScissorHeight = scissorHeight;
    }

    public int getScissorX() {
        return mScissorX;
    }

    public int getScissorY() {
        return mScissorY;
    }

    public int getScissorWidth() {
        return mScissorWidth;
    }

    public int getScissorHeight() {
        return mScissorHeight;
    }

    public int getImageWidth() {
        return mImage.getWidth();
    }

    public int getImageHeight() {
        return mImage.getHeight();
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
