package com.createchance.imageeditor.ops;

import android.graphics.Bitmap;

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
    private int mTextureId;

    private BaseImageDrawer mDrawer;

    private BaseImageOperator() {
        super(BaseImageOperator.class.getSimpleName(), OP_BASE_IMAGE);
    }

    @Override
    public boolean checkRational() {
        return mImage != null
                && !mImage.isRecycled();
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getInputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new BaseImageDrawer();
            mTextureId = OpenGlUtils.loadTexture(mImage, OpenGlUtils.NO_TEXTURE, false);
        }
        mDrawer.draw(mTextureId,
                0,
                0,
                mWorker.getImgOriginWidth(),
                mWorker.getImgOriginHeight());
        mWorker.bindDefaultFrameBuffer();
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

        public BaseImageOperator build() {
            return operator;
        }
    }
}
