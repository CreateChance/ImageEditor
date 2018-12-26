package com.createchance.imageeditordemo;

import android.os.Environment;

import java.io.File;

/**
 * Constant class
 *
 * @author createchance
 * @date 2018/11/3
 */
public class Constants {

    public static final File mBaseDir = new File(Environment.getExternalStorageDirectory(), "imageeditor");

    public static int mScreenWidth, mScreenHeight;
}
