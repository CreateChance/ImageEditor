package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.WaterDropTransDrawer;

/**
 * Water drop transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WaterDropTransition extends AbstractTransition {

    private static final String TAG = "WaterDropTransition";

    private float mAmplitude = 30;
    private float mSpeed = 30;

    public WaterDropTransition() {
        super(WaterDropTransition.class.getSimpleName(), TRANS_WATER_DROP);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new WaterDropTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((WaterDropTransDrawer) mDrawer).setAmplitude(mAmplitude);
        ((WaterDropTransDrawer) mDrawer).setSpeed(mSpeed);
    }
}
