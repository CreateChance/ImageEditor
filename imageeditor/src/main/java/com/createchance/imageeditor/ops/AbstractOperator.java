package com.createchance.imageeditor.ops;

import com.createchance.imageeditor.RenderContext;

/**
 * Base class of all operators.
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
    public static final int OP_TEMPERATURE = 13;
    public static final int OP_TINT = 14;
    public static final int OP_EXPOSURE = 15;
    public static final int OP_GAMMA = 16;
    public static final int OP_DENOISE = 17;
    public static final int OP_MOSAIC = 18;
    public static final int OP_RGB = 19;
    public static final int OP_3_X_3_SAMPLE = 20;
    public static final int OP_5_X_5_SAMPLE = 21;
    public static final int OP_BOKEH_FILTER = 22;
    public static final int OP_COLOR_BALANCE_FILTER = 23;

    protected final String mName;

    protected final int mType;

    protected RenderContext mContext;

    public AbstractOperator(String name, int type) {
        mName = name;
        mType = type;
    }

    public void setRenderContext(RenderContext context) {
        mContext = context;
    }

    public String getName() {
        return mName;
    }

    public int getType() {
        return mType;
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
