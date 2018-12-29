package com.createchance.imageeditor.transitions;

import com.createchance.imageeditor.drawers.InvertedPageCurlTransDrawer;
import com.createchance.imageeditor.utils.Logger;

/**
 * Inverted page curl transition.
 *
 * @author createchance
 * @date 2018/12/23
 */
public class InvertedPageCurlTransition extends AbstractTransition {

    private static final String TAG = "InvertedPageCurlTransition";

    private InvertedPageCurlTransDrawer mDrawer;

    public InvertedPageCurlTransition() {
        super(InvertedPageCurlTransition.class.getSimpleName(), TRANS_INVERTED_PAGE_CURL);
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        int texture2 = mContext.getToTextureId();
        if (texture2 != -1) {
            mContext.attachOffScreenTexture(mContext.getOutputTextureId());
            if (mDrawer == null) {
                mDrawer = new InvertedPageCurlTransDrawer();
            }

            mDrawer.setProgress(mProgress);

            mDrawer.draw(mContext.getFromTextureId(),
                    texture2,
                    mContext.getRenderLeft(),
                    mContext.getRenderBottom(),
                    mContext.getRenderWidth(),
                    mContext.getRenderHeight());
            mContext.swapTexture();
        } else {
            Logger.e(TAG, "Can not get texture 2 id.");
        }
    }
}
