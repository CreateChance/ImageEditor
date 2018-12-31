package com.createchance.imageeditor.drawers;

import com.createchance.imageeditor.shaders.InvertedPageCurlTransShader;

/**
 * Inverted page curl transition drawer.
 *
 * @author createchance
 * @date 2018/12/23
 */
public class InvertedPageCurlTransDrawer extends AbstractTransDrawer {

    private static final String TAG = "InvertedPageCurlTransDr";

    @Override
    protected void getTransitionShader() {
        mTransitionShader = new InvertedPageCurlTransShader();
    }
}
