package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.CrazyParametricFunTransShader;

/**
 * Crazy parametric fun transition drawer.
 *
 * @author createchance
 * @date 2018/12/31
 */
public class CrazyParametricFunTransDrawer extends AbstractTransDrawer {
    @Override
    protected void getTransitionShader() {
        mTransitionShader = new CrazyParametricFunTransShader();
    }

    public void setA(float a) {
        ((CrazyParametricFunTransShader) mTransitionShader).setUA(a);
    }

    public void setB(float b) {
        ((CrazyParametricFunTransShader) mTransitionShader).setUB(b);
    }

    public void setAmplitude(float amplitude) {
        ((CrazyParametricFunTransShader) mTransitionShader).setUAmplitude(amplitude);
    }

    public void setSmoothness(float smoothness) {
        ((CrazyParametricFunTransShader) mTransitionShader).setUSmoothness(smoothness);
    }
}
