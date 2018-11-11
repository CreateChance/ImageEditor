package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.ModelViewDrawer;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/11/9
 */
public class ModelViewOperator extends AbstractOperator {

    private static final String TAG = "ModelViewOperator";

    private ModelViewDrawer mDrawer;

    private float mLeft = -1f, mRight = 1f, mBottom = -1f, mTop = 1f;
    private float mFov = 45,
            mNear = 1f,
            mFar = 100f,
            mTranslateX,
            mTranslateY,
            mTranslateZ = (float) (1 / Math.tan(Math.toRadians(mFov / 2)));
    private float mRotateX = 180, mRotateY = 0, mRotateZ = 0;

    private ModelViewOperator() {
        super(ModelViewOperator.class.getSimpleName(), OP_TRANSFORM);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    public void setTranslateX(float translateX) {
        this.mTranslateX = translateX;
    }

    public void setTranslateY(float translateY) {
        this.mTranslateY = translateY;
    }

    public void setTranslateZ(float translateZ) {
        this.mTranslateZ = translateZ;
    }

    public void setRotateX(float mRotateX) {
        this.mRotateX = mRotateX;
    }

    public void setRotateY(float mRotateY) {
        this.mRotateY = mRotateY;
    }

    public void setRotateZ(float mRotateZ) {
        this.mRotateZ = mRotateZ;
    }

    public float getLeft() {
        return mLeft;
    }

    public float getRight() {
        return mRight;
    }

    public float getBottom() {
        return mBottom;
    }

    public float getTop() {
        return mTop;
    }

    public float getFov() {
        return mFov;
    }

    public float getNear() {
        return mNear;
    }

    public float getFar() {
        return mFar;
    }

    @Override
    public void exec() {
        mWorker.bindOffScreenFrameBuffer(mWorker.getTextures()[mWorker.getOutputTextureIndex()]);
        if (mDrawer == null) {
            mDrawer = new ModelViewDrawer();
        }
        mDrawer.setModel(mTranslateX, mTranslateY, mTranslateZ, mRotateX, mRotateY, mRotateZ);
        mDrawer.setPerspectiveProjection(mFov,
                mWorker.getSurfaceWidth() * 1.0f / mWorker.getSurfaceHeight(),
                mNear,
                mFar);
//        mDrawer.setOrthographicProjection(mLeft, mRight, mBottom, mTop, mNear, mFar);
        mDrawer.draw(mWorker.getTextures()[mWorker.getInputTextureIndex()],
                0,
                0,
                mWorker.getSurfaceWidth(),
                mWorker.getSurfaceHeight());
        mWorker.bindDefaultFrameBuffer();
        mWorker.swapTexture();
    }

    public static class Builder {
        private ModelViewOperator operator = new ModelViewOperator();

        public ModelViewOperator build() {
            return operator;
        }
    }
}
