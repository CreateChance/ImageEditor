package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.IEWorker;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/10/28
 */
public abstract class AbstractOperator {

    public static final int OP_BASE_IMAGE = 0;
    public static final int OP_FILTER = 1;
    public static final int OP_TEXT = 2;
    public static final int OP_STICKER = 3;
    public static final int OP_TRANSFORM = 4;
    public static final int OP_LOOKUP_FILTER = 5;
    public static final int OP_BRIGHTNESS_ADJUST = 6;
    public static final int OP_CONTRAST_ADJUST = 7;
    public static final int OP_SATURATION_ADJUST = 8;
    public static final int OP_SHARPNESS_ADJUST = 9;
    public static final int OP_VIGNETTE = 10;
    public static final int OP_SHADOW = 11;
    public static final int OP_HIGHLIGHT = 12;

    protected final String mName;

    protected final int mType;

    protected IEWorker mWorker;

    public AbstractOperator(String name, int type) {
        mName = name;
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public int getType() {
        return mType;
    }

    public void setWorker(IEWorker worker) {
        mWorker = worker;
    }

    public abstract boolean checkRational();

    public abstract void exec();

    @Override
    public String toString() {
        return "AbstractOperator{" +
                "mName='" + mName + '\'' +
                '}';
    }
}
