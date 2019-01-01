package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.HexagonalTransDrawer;

/**
 * Hexagonal transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class HexagonalTransition extends AbstractTransition {

    private static final String TAG = "HexagonalTransition";

    private int mStep = 50;
    private float mHorizontalHexagons = 20;

    public HexagonalTransition() {
        super(HexagonalTransition.class.getSimpleName(), TRANS_HEXAGONAL);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new HexagonalTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((HexagonalTransDrawer) mDrawer).setStep(mStep);
        ((HexagonalTransDrawer) mDrawer).setHorizontalHexagons(mHorizontalHexagons);
    }
}
