package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.InvertedPageCurlTransDrawer;

/**
 * Inverted page curl transition.
 *
 * @author createchance
 * @date 2018/12/23
 */
public class InvertedPageCurlTransition extends AbstractTransition {

    private static final String TAG = "InvertedPageCurlTransition";

    public InvertedPageCurlTransition() {
        super(InvertedPageCurlTransition.class.getSimpleName(), TRANS_INVERTED_PAGE_CURL);
    }

    @Override
    protected void getDrawer() {
        mDrawer = new InvertedPageCurlTransDrawer();
    }
}
