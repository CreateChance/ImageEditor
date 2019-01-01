package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.WindTransDrawer;
import com.createchance.imageeditor.shaders.WindTransShader;

/**
 * Wind transition
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WindTransition extends AbstractTransition {

    private static final String TAG = "WindTransition";

    private float mSize = 0.2f;

    public WindTransition() {
        super(WindTransShader.class.getSimpleName(), TRANS_WIND);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new WindTransDrawer();
    }

    @Override
    protected void setDrawerParams() {
        super.setDrawerParams();

        ((WindTransDrawer) mDrawer).setSize(mSize);
    }
}
