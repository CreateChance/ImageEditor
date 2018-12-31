package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.drawers.ModelViewDrawer;

/**
 * Model view operator, adjust image space position.
 *
 * @author createchance
 * @date 2018/11/9
 */
public class ModelViewOperator extends AbstractOperator {

    private static final String TAG = "ModelViewOperator";

    private ModelViewDrawer mDrawer;

    private float mLeft = -1f, mRight = 1f, mBottom = -1f, mTop = 1f;
    private float mFov = 45;
    private final float JUST_FIT_TRANS_Z = (float) (1 / Math.tan(Math.toRadians(mFov / 2)));
    private float mNear = 1f,
            mFar = 100f,
            mTranslateX,
            mTranslateY,
            mTranslateZ = JUST_FIT_TRANS_Z;
    private float mRotateX = 180, mRotateY = 0, mRotateZ = 0;
    private boolean mIsPerspective = true;

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
        if (mTranslateZ == translateZ) {
            return;
        }
        this.mTranslateZ = translateZ;
    }

    public void setRotateX(float mRotateX) {
        this.mRotateX = mRotateX;
    }

    public void setRotateY(float mRotateY) {
        this.mRotateY = mRotateY;
    }

    public void setRotateZ(float rotateZ) {
        this.mRotateZ = rotateZ;
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

    public boolean isPerspective() {
        return mIsPerspective;
    }

    public void setPerspective(boolean isPerspective) {
        if (mIsPerspective == isPerspective) {
            return;
        }
        this.mIsPerspective = isPerspective;
    }

    @Override
    public void exec() {
        mContext.attachOffScreenTexture(mContext.getOutputTextureId());
        if (mDrawer == null) {
            mDrawer = new ModelViewDrawer();
        }

        if (mIsPerspective) {
            mDrawer.setPerspectiveProjection(mFov,
                    1f,
                    mNear,
                    mFar);
        } else {
            mDrawer.setOrthographicProjection(mLeft, mRight, mBottom, mTop, mNear, mFar);
        }
        mDrawer.setModel(mTranslateX, mTranslateY, mTranslateZ, mRotateX, mRotateY, mRotateZ);
        // do transform.
        mDrawer.draw(mContext.getInputTextureId(),
                0,
                0,
                mContext.getSurfaceWidth(),
                mContext.getSurfaceHeight());
        mContext.swapTexture();
    }

    public static class Builder {
        private ModelViewOperator operator = new ModelViewOperator();

        public ModelViewOperator build() {
            return operator;
        }
    }
}
