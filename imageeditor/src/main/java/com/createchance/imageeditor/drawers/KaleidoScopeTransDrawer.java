package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.KaleidoScopeTransShader;

/**
 * Kaleido scope transition drawer.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class KaleidoScopeTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new KaleidoScopeTransShader();
    }

    public void setSpeed(float speed) {
        ((KaleidoScopeTransShader) mTransitionShader).setUSpeed(speed);
    }

    public void setAngle(float angle) {
        ((KaleidoScopeTransShader) mTransitionShader).setUAngle(angle);
    }

    public void setPower(float power) {
        ((KaleidoScopeTransShader) mTransitionShader).setUPower(power);
    }
}
