package com.createchance.imageeditor;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Surface;

import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/10/28
 */
public class IEManager {

    private static final String TAG = "IEManager";

    private IEWorker mWorker;

    private IEManager() {
    }

    public static IEManager getInstance() {
        return Holder.sInstance;
    }

    public void prepare(Surface surface, int width, int height) {
        mWorker = new IEWorker();
        mWorker.startWorking(surface, width, height);
    }

    public void setImage(File imgFile) {
        mWorker.setBaseImage(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
    }

    public void setImage(String imgPath) {
        mWorker.setBaseImage(BitmapFactory.decodeFile(imgPath));
    }

    public void setImage(Resources resources, int resId) {
        mWorker.setBaseImage(BitmapFactory.decodeResource(resources, resId));
    }

    public void setImage(AssetManager assetManager, String imgPath) {
        try {
            Bitmap image = BitmapFactory.decodeFileDescriptor(assetManager.openFd(imgPath).getFileDescriptor());
            mWorker.setBaseImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addOperator(AbstractOperator operator) {
        if (operator == null) {
            Logger.e(TAG, "Operator can not be null!");
            return false;
        }

        if (!operator.checkRational()) {
            Logger.e(TAG, "Operator check rational failed, can not be executed!");
            return false;
        }

        mWorker.execOperator(operator);

        return true;
    }

    public boolean addOperator(List<AbstractOperator> operatorList) {
        if (operatorList == null || operatorList.size() == 0) {
            Logger.e(TAG, "Operator list is null or empty!");
            return false;
        }

        boolean ret = true;
        for (AbstractOperator operator : operatorList) {
            if (!addOperator(operator)) {
                ret = false;
            }
        }

        return ret;
    }

    public boolean removeOperator(AbstractOperator operator) {
        if (operator == null) {
            Logger.e(TAG, "Operator can not be null!");
            return false;
        }

        mWorker.removeOperator(operator);

        return true;
    }

    public List<AbstractOperator> getOperatorList() {
        return mWorker.getOpList();
    }

    private static class Holder {
        private static final IEManager sInstance = new IEManager();
    }
}
