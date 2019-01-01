package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.WindowBlindsTransDrawer;

/**
 * Window blinds transition.
 *
 * @author createchance
 * @date 2019/1/1
 */
public class WindowBlindsTransition extends AbstractTransition {

    private static final String TAG = "WindowBlindsTransition";

    public WindowBlindsTransition() {
        super(WindowBlindsTransition.class.getSimpleName(), TRANS_WINDOW_BLINDS);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new WindowBlindsTransDrawer();
    }
}
