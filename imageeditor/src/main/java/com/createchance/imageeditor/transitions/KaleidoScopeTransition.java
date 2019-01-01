package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.KaleidoScopeTransDrawer;

/**
 * Kaleido scope transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class KaleidoScopeTransition extends AbstractTransition {

    private static final String TAG = "KaleidoScopeTransition";

    private float mSpeed = 1.0f;
    private float mAngle = 1.0f;
    private float mPower = 1.0f;

    public KaleidoScopeTransition() {
        super(KaleidoScopeTransition.class.getSimpleName(), TRANS_KALEIDO_SCOPE);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new KaleidoScopeTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((KaleidoScopeTransDrawer) mDrawer).setAngle(mAngle);
        ((KaleidoScopeTransDrawer) mDrawer).setPower(mPower);
        ((KaleidoScopeTransDrawer) mDrawer).setSpeed(mSpeed);
    }
}
