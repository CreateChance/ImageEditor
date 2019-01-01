package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.PixelizeTransDrawer;

/**
 * Pixelize transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class PixelizeTransition extends AbstractTransition {

    private static final String TAG = "PixelizeTransition";

    private int mWidth = 20, Height = 20;
    private int mStep = 50;

    public PixelizeTransition() {
        super(PixelizeTransition.class.getSimpleName(), TRANS_PIXELIZE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new PixelizeTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((PixelizeTransDrawer) mDrawer).setSquaresMin(mWidth, mStep);
        ((PixelizeTransDrawer) mDrawer).setStep(mStep);
    }
}
