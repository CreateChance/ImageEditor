package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.ButterflyWaveScrawlerTransDrawer;

/**
 * Butterfly Wave Scrawler transition
 *
 * @author createchance
 * @date 2018/12/31
 */
public class ButterflyWaveScrawlerTransition extends AbstractTransition {

    private static final String TAG = "ButterflyWaveScrawlerTr";

    private float mAmplitude = 1.0f;
    private float mWaves = 30;
    private float mColorSeparation = 0.3f;

    public ButterflyWaveScrawlerTransition() {
        super(ButterflyWaveScrawlerTransition.class.getSimpleName(), TRANS_BUTTERFLY_WAVE_SCRAWLER);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new ButterflyWaveScrawlerTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        ((ButterflyWaveScrawlerTransDrawer) mDrawer).setAmplitude(mAmplitude);
        ((ButterflyWaveScrawlerTransDrawer) mDrawer).setWaves(mWaves);
        ((ButterflyWaveScrawlerTransDrawer) mDrawer).setColorSeparation(mColorSeparation);
    }
}
