package com.createchance.imageeditordemo;

import android.os.Environment;

import java.io.File;

/**
 * Constant class
 *
 * @author gaochao1-iri
 * @date 2018/11/3
 */
public class Constants {

    public static final File mBaseDir = new File(Environment.getExternalStorageDirectory(), "imageeditor");

    public static int mScreenWidth, mScreenHeight;

    public static int mSurfaceWidth, mSurfaceHeight;
}
