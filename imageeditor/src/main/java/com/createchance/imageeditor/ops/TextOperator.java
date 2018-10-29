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

    private TextDrawer mDrawer;

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
        mDrawer.setText(mFontPath, mText, mPosX, mPosY, mSize, mRed, mGreen, mBlue, mBackground);
        mDrawer.draw();
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

        public Builder background(Bitmap background) {
            operator.mBackground = background;

            return this;
        }

        public TextOperator build() {
            return operator;
        }
    }
}
