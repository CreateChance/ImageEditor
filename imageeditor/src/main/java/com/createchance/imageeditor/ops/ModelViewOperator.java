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
            mNear = 0.1f,
            mFar = 100,
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

    public void setTranslateX(float mTranslateX) {
        this.mTranslateX = mTranslateX;
    }

    public void setTranslateY(float mTranslateY) {
        this.mTranslateY = mTranslateY;
    }

    public void setTranslateZ(float mTranslateZ) {
        this.mTranslateZ = mTranslateZ;
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

    @Override
    public void exec() {
        if (mDrawer == null) {
            mDrawer = new ModelViewDrawer();
        }
        mDrawer.setModel(mTranslateX, mTranslateY, mTranslateZ, mRotateX, mRotateY, mRotateZ);
        mDrawer.setPerspectiveProjection(mFov,
                mWorker.getSurfaceWidth() / mWorker.getSurfaceHeight(),
                mNear,
                mFar);
        mDrawer.draw(mWorker.getInputTexture(),
                0,
                0,
                mWorker.getSurfaceWidth(),
                mWorker.getSurfaceHeight());
    }

    public static class Builder {
        private ModelViewOperator operator = new ModelViewOperator();

        public ModelViewOperator build() {
            return operator;
        }
    }
}
