package com.createchance.imageeditordemo;

import android.os.Environment;

import com.createchance.imageeditor.ops.AbstractOperator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Constant class
 *
 * @author createchance
 * @date 2018/11/3
 */
public class Constants {

    public static final File mBaseDir = new File(Environment.getExternalStorageDirectory(), "imageeditor");

    public static int mScreenWidth, mScreenHeight;

    public static int mSurfaceWidth, mSurfaceHeight;

    public static List<AbstractOperator> mOpList = new ArrayList<>();
}
