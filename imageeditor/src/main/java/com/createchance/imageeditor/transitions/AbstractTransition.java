package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.RenderContext;

/**
 * Abstract transition class.
 *
 * @author createchance
 * @date 2018/12/28
 */
public abstract class AbstractTransition {
    public static final int TRANS_INVALID = -1;
    public static final int TRANS_WINDOW_SLICE = 0;
    public static final int TRANS_INVERTED_PAGE_CURL = 1;
    public static final int TRANS_ANGULAR = 2;
    public static final int TRANS_BOUNCE = 3;

    protected final String mName;

    protected final int mType;

    protected float mProgress;

    protected RenderContext mContext;

    public AbstractTransition(String name, int type) {
        mName = name;
        mType = type;
    }

    public final void setRenderContext(RenderContext context) {
        mContext = context;
    }

    public int getType() {
        return mType;
    }

    public abstract boolean checkRational();

    public abstract void exec();

    public void setProgress(float progress) {
        mProgress = progress;
    }

    @Override
    public String toString() {
        return "AbstractTransition{" +
                "mName='" + mName + '\'' +
                ", mType=" + mType +
                '}';
    }
}
