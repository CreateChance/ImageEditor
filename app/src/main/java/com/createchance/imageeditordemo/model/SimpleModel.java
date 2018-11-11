package com.createchance.imageeditordemo.model;

import android.graphics.Bitmap;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/11/3
 */
public class SimpleModel {
    private static SimpleModel sInstance;

    private Bitmap mImage;

    private SimpleModel() {

    }

    public static synchronized SimpleModel getInstance() {
        if (sInstance == null) {
            sInstance = new SimpleModel();
        }

        return sInstance;
    }

    public void putImage(Bitmap image) {
        if (mImage != null) {
            mImage.recycle();
        }
        mImage = image;
    }

    public Bitmap getImage() {
        return mImage;
    }
}
