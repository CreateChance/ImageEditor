package com.createchance.imageeditordemo.model;

import android.graphics.Bitmap;

/**
 * Clip data.
 *
 * @author createachance
 * @date 2018/12/23
 */
public class Clip {
    public final Bitmap bitmap;

    public long duration;

    public Clip(Bitmap bitmap, long duration) {
        this.bitmap = bitmap;
        this.duration = duration;
    }


}
