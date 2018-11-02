package com.createchance.imageeditordemo.panels;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/10/31
 */
public abstract class AbstractPanel {

    public static final int TYPE_EFFECT = 0;
    public static final int TYPE_ADJUST = 1;
    public static final int TYPE_CUT = 2;
    public static final int TYPE_ROTATE = 3;
    public static final int TYPE_TEXT = 4;
    public static final int TYPE_FOCUS = 5;
    public static final int TYPE_STICKER = 6;
    public static final int TYPE_MOSAIC = 7;

    protected Context mContext;

    protected ViewGroup mParent;

    protected PanelListener mListener;

    protected int mType;

    protected int mSurfaceWidth, mSurfaceHeight;

    public AbstractPanel(Context context, PanelListener listener, int type) {
        mContext = context;
        mListener = listener;
        mType = type;
    }

    public void show(ViewGroup parent, int surfaceWidth, int surfaceHeight) {
        mParent = parent;
        mSurfaceWidth = surfaceWidth;
        mSurfaceHeight = surfaceHeight;
    }

    public abstract void close();

    public abstract void onTouchEvent(MotionEvent event);

    public interface PanelListener {
        void onPanelShow(int type);

        void onPanelClosed(int type);
    }
}
