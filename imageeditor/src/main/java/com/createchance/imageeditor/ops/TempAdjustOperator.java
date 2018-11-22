package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.TempAdjustDrawer;

/**
 * Temperature adjust operator.
 *
 * @author createchance
 * @date 2018/11/17
 */
public class TempAdjustOperator extends AbstractOperator {

    private static final String TAG = "TempAdjustOperator";

    private final float MAX_TEMP = 2.0f;
    private final float MIN_TEMP = -2.0f;

    private float mTemperature = 0.0f;

    private TempAdjustDrawer mDrawer;

    private TempAdjustOperator() {
        super(TempAdjustOperator.class.getSimpleName(), OP_TEMPERATURE);
    }

    @Override
    public boolean checkRational() {
        return mTemperature >= MIN_TEMP && mTemperature <= MAX_TEMP;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new TempAdjustDrawer();
        }
        mDrawer.setTemperature(mTemperature);
        mDrawer.draw(mWorker.getTextures()[mWorker.getInputTextureIndex()],
                0,
                0,
                mWorker.getImgOriginWidth(),
                mWorker.getImgOriginHeight());
        mWorker.bindDefaultFrameBuffer();
        mWorker.swapTexture();
    }

    public float getTemperature() {
        return mTemperature;
    }

    public void setTemperature(float temperature) {
        this.mTemperature = temperature;
    }

    public static class Builder {
        private TempAdjustOperator operator = new TempAdjustOperator();

        public Builder temperature(float temp) {
            operator.mTemperature = temp;

            return this;
        }

        public TempAdjustOperator build() {
            return operator;
        }
    }

}
