package com.createchance.imageeditor.ops;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.createchance.imageeditor.TextDrawer;

/**
 * Text operator.
 *
 * @author createchane
 * @date 2018-10-29
 */
public class TextOperator extends AbstractOperator {

    private static final String TAG = "TextOperator";

    private String mText;

    private String mFontPath;

    private int mPosX, mPosY;

    private int mSize;

    private float mRed = 1.0f, mGreen = 1.0f, mBlue = 1.0f;

    private Bitmap mBackground;

    private float mAlpha = 1.0f;

    private TextDrawer mDrawer;

    private boolean mNeedReload = true;

    private TextOperator() {
        super(TextOperator.class.getSimpleName(), OP_TEXT);
    }

    @Override
    public boolean checkRational() {
        return !TextUtils.isEmpty(mText)
                && !TextUtils.isEmpty(mFontPath)
                && mPosX >= 0
                && mPosY >= 0
                && mSize >= 0
                && mRed >= 0.0f
                && mGreen >= 0.0f
                && mBlue >= 0.0f;

    }

    @Override
    public void exec() {
        if (mDrawer == null) {
            mDrawer = new TextDrawer();
        }
        if (mNeedReload) {
            mNeedReload = false;
            mDrawer.setText(mFontPath, mText, mSize);
        }

        // adjust position
        if (mPosX > mWorker.getImgShowRight() - mDrawer.getWidth()) {
            mPosX = mWorker.getImgShowRight() - mDrawer.getWidth();
        } else if (mPosX < mWorker.getImgShowLeft()) {
            mPosX = mWorker.getImgShowLeft();
        }
        if (mPosY > mWorker.getImgShowTop() - mDrawer.getHeight()) {
            mPosY = mWorker.getImgShowTop() - mDrawer.getHeight();
        } else if (mPosY < mWorker.getImgShowBottom()) {
            mPosY = mWorker.getImgShowBottom();
        }

        mDrawer.setParams(mPosX, mPosY, mRed, mGreen, mBlue, mAlpha, mBackground);
        mDrawer.draw();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        if (!TextUtils.equals(mText, text)) {
            mNeedReload = true;
        }

        this.mText = text;
    }

    public String getFontPath() {
        return mFontPath;
    }

    public void setFontPath(String fontPath) {
        if (!TextUtils.equals(mFontPath, fontPath)) {
            mNeedReload = true;
        }

        this.mFontPath = fontPath;
    }

    public int getPosX() {
        return mPosX;
    }

    public void setPosX(int mPosX) {
        this.mPosX = mPosX;
    }

    public int getPosY() {
        return mPosY;
    }

    public void setPosY(int mPosY) {
        this.mPosY = mPosY;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        if (mSize != size) {
            mNeedReload = true;
        }

        this.mSize = size;
    }

    public float getRed() {
        return mRed;
    }

    public void setRed(float mRed) {
        this.mRed = mRed;
        mBackground = null;
    }

    public float getGreen() {
        return mGreen;
    }

    public void setGreen(float mGreen) {
        this.mGreen = mGreen;
        mBackground = null;
    }

    public float getBlue() {
        return mBlue;
    }

    public void setBlue(float mBlue) {
        this.mBlue = mBlue;
        mBackground = null;
    }

    public Bitmap getBackground() {
        return mBackground;
    }

    public void setBackground(Bitmap mBackground) {
        this.mBackground = mBackground;
    }

    public float getAlpha() {
        return mAlpha;
    }

    public void setAlpha(float mAlpha) {
        this.mAlpha = mAlpha;
    }

    public static class Builder {
        private TextOperator operator = new TextOperator();

        public Builder text(String text) {
            operator.mText = text;

            return this;
        }

        public Builder font(String fontPath) {
            operator.mFontPath = fontPath;

            return this;
        }

        public Builder position(int x, int y) {
            operator.mPosX = x;
            operator.mPosY = y;

            return this;
        }

        public Builder size(int size) {
            operator.mSize = size;

            return this;
        }

        public Builder color(float red, float green, float blue) {
            operator.mRed = red;
            operator.mGreen = green;
            operator.mBlue = blue;

            return this;
        }

        public Builder alpha(float alpha) {
            operator.mAlpha = alpha;

            return this;
        }

        public Builder background(Bitmap background) {
            operator.mBackground = background;

            return this;
        }

        public TextOperator build() {
            return operator;
        }
    }
}
