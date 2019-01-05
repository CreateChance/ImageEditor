package com.createchance.imageeditordemo;

import android.content.Context;

import java.io.File;

/**
 * Constant class
 *
 * @author createchance
 * @date 2018/11/3
 */
public class Constants {

    public static File mBaseDir;

    public static int mScreenWidth, mScreenHeight;

    public static void setConstants(Context context) {
        mBaseDir = new File(context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), "outputs");
    }
}
