package com.createchance.imageeditordemo;

import android.app.Application;

import com.createchance.imageeditor.IEManager;

/**
 * Custom application class, init image editor library here.
 *
 * @author createchance
 * @date 2018/11/10
 */
public class ImageEditorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        IEManager.getInstance().init(this);

        Constants.setConstants(this);
    }
}
