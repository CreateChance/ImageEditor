package com.createchance.imageeditor.ops;

import android.opengl.GLES20;
import android.util.Log;

import com.createchance.imageeditor.drawers.MosaicDrawer;

import java.util.ArrayList;
import java.util.List;

/**
 * Mosaic operator.
 *
 * @author createchance
 * @date 2018/11/18
 */
public class MosaicOperator extends AbstractOperator {

    private static final String TAG = "MosaicOperator";

    private MosaicDrawer mDrawer;

    private float mSize = 1f;

    private float mMosaicWidth = 0f, mMosaicHeight = 0f;

    private final int SPAN_COUNT = 64;

    private final List<Area> mMosaicAreaList;

    private MosaicOperator() {
        super(MosaicOperator.class.getSimpleName(), OP_MOSAIC);
        mMosaicAreaList = new ArrayList<>();
    }

    @Override
    public boolean checkRational() {
        return mSize >= 0 && mMosaicWidth >= 0 && mMosaicHeight >= 0;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new MosaicDrawer();
        }

        if (mSize == 0 || mMosaicWidth == 0) {
            return;
        }

        if (mMosaicAreaList.size() == 0) {
            return;
        }
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        synchronized (mMosaicAreaList) {
            for (Area area : mMosaicAreaList) {
                GLES20.glScissor(area.x,
                        area.y,
                        area.width,
                        area.height);
                mDrawer.setImageSize(mWorker.getImgOriginWidth(), mWorker.getImgOriginHeight());
                mDrawer.setMosaicSize(mMosaicWidth, mMosaicHeight);
                mDrawer.draw(mWorker.getTextures()[mWorker.getInputTextureIndex()],
                        0,
                        0,
                        mWorker.getImgOriginWidth(),
                        mWorker.getImgOriginHeight());
            }
        }
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
        mWorker.bindDefaultFrameBuffer();
        mWorker.swapTexture();
    }

    public float getSize() {
        return mSize;
    }

    public void setSize(float size) {
        this.mSize = size;
    }

    public float getStrength() {
        return mMosaicWidth;
    }

    public void setStrength(float strength) {
        mMosaicWidth = strength;
        mMosaicHeight = strength;
    }

    public void addArea(float x, float y) {

        synchronized (mMosaicAreaList) {
            int curIndex = -1;
            for (int i = 0; i < mMosaicAreaList.size(); i++) {
                Area area = mMosaicAreaList.get(i);
                if (area.isIn(x, mWorker.getSurfaceHeight() - y)) {
                    curIndex = mMosaicAreaList.indexOf(area);
                    break;
                }
            }

            if (curIndex == -1) {
                Area area = new Area();
                int xIndex = (int) (x / area.width);
                int yIndex = (int) ((mWorker.getSurfaceHeight() - y) / area.height);
                area.x = area.width * xIndex;
                area.y = area.height * yIndex;
                mMosaicAreaList.add(area);
            } else {
                Log.w(TAG, "addArea failed, cause we already added!");
            }
        }
    }

    public void removeArea(float x, float y) {
        if (mMosaicAreaList.size() == 0) {
            return;
        }

        synchronized (mMosaicAreaList) {
            int indexToRemove = -1;
            for (Area area : mMosaicAreaList) {
                if (area.isIn(x, mWorker.getSurfaceHeight() - y)) {
                    indexToRemove = mMosaicAreaList.indexOf(area);
                    break;
                }
            }

            if (indexToRemove >= 0) {
                mMosaicAreaList.remove(indexToRemove);
            }
        }
    }

    public void clearArea() {
        synchronized (mMosaicAreaList) {
            mMosaicAreaList.clear();
        }
    }

    public static class Builder {
        private MosaicOperator operator = new MosaicOperator();

        public Builder size(float size) {
            operator.mSize = size;

            return this;
        }

        public Builder strength(float strength) {
            operator.mMosaicWidth = strength;
            operator.mMosaicHeight = strength;

            return this;
        }

        public MosaicOperator build() {
            return operator;
        }
    }

    private class Area {
        int x;
        int y;
        int width = (int) (mWorker.getSurfaceWidth() * 1.0f / getScaledSpanCount());
        int height = (int) (mWorker.getSurfaceHeight() * 1.0f / getScaledSpanCount());

        @Override
        public String toString() {
            return "Area{" +
                    ", x=" + x +
                    ", y=" + y +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }

        public boolean isIn(float posX, float posY) {
            return x <= posX && y <= posY && (x + width) >= posX && (y + height) > posY;
        }

        private int getScaledSpanCount() {
            if (mSize == 0) {
                return SPAN_COUNT;
            }
            int size = (int) (SPAN_COUNT / mSize);
            Log.d(TAG, "getScaledSpanCount: " + size);
            return size;
        }
    }
}
