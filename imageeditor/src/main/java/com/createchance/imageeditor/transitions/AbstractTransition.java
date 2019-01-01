package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.RenderContext;
import com.createchance.imageeditor.drawers.AbstractTransDrawer;

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
    public static final int TRANS_BOW_TIE_HORIZONTAL = 4;
    public static final int TRANS_BOW_TIE_VERTICAL = 5;
    public static final int TRANS_BURN = 6;
    public static final int TRANS_BUTTERFLY_WAVE_SCRAWLER = 7;
    public static final int TRANS_CANNABIS_LEAF = 8;
    public static final int TRANS_CIRCLE = 9;
    public static final int TRANS_CIRCLE_CORP = 10;
    public static final int TRANS_CIRCLE_OPEN = 11;
    public static final int TRANS_COLOR_PHASE = 12;
    public static final int TRANS_COLOR_DISTANCE = 13;
    public static final int TRANS_CRAZY_PARAMETRIC_FUN = 14;
    public static final int TRANS_CROSS_HATCH = 15;
    public static final int TRANS_CROSS_WARP = 16;
    public static final int TRANS_CROSS_ZOOM = 17;
    public static final int TRANS_CUBE = 18;
    public static final int TRANS_DIRECTIONAL = 19;
    public static final int TRANS_DIRECTIONAL_WARP = 20;
    public static final int TRANS_DIRECTIONAL_WIPE = 21;
    public static final int TRANS_DOOM_SCREEN = 22;
    public static final int TRANS_DOOR_WAY = 23;
    public static final int TRANS_DREAMY = 24;
    public static final int TRANS_DREAMY_ZOOM = 25;
    public static final int TRANS_FADE = 26;
    public static final int TRANS_FADE_COLOR = 27;
    public static final int TRANS_FADE_GRAY_SCALE = 28;
    public static final int TRANS_FLY_EYE = 29;
    public static final int TRANS_GLITCH_DISPLACE = 30;
    public static final int TRANS_GLITCH_MEMORIES = 31;
    public static final int TRANS_GRID_FLIP = 32;
    public static final int TRANS_HEART = 33;
    public static final int TRANS_HEXAGONAL = 34;
    public static final int TRANS_KALEIDO_SCOPE = 35;
    public static final int TRANS_LINEAR_BLUR = 36;
    public static final int TRANS_LUMINANCE_MELT = 37;
    public static final int TRANS_MORPH = 38;
    public static final int TRANS_MOSAIC = 39;
    public static final int TRANS_MULTIPLY_BLEND = 40;

    protected final String mName;

    protected final int mType;

    protected float mProgress;

    protected RenderContext mContext;

    protected AbstractTransDrawer mDrawer;

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

    public boolean checkRational() {
        return true;
    }

    protected abstract void getDrawer();

    protected void setDrawerParams() {

    }

    public void exec() {
        int texture2 = mContext.getToTextureId();
        if (texture2 != -1) {
            mContext.attachOffScreenTexture(mContext.getOutputTextureId());
            if (mDrawer == null) {
                getDrawer();
            }

            mDrawer.setProgress(mProgress);
            setDrawerParams();

            mDrawer.draw(mContext.getFromTextureId(),
                    texture2,
                    mContext.getRenderLeft(),
                    mContext.getRenderBottom(),
                    mContext.getRenderWidth(),
                    mContext.getRenderHeight());
            mContext.swapTexture();
        }
    }

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
