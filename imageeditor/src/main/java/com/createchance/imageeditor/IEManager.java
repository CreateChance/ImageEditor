package com.createchance.imageeditor;

import android.content.Context;
import android.view.Surface;

import com.createchance.imageeditor.freetype.FreeType;
import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.utils.Logger;

import java.io.File;
import java.util.List;

/**
 * ${DESC}
 *
 * @author createchance
 * @date 2018/10/28
 */
public class IEManager {

    private static final String TAG = "IEManager";

    private IEWorker mWorker;

    private Context mAppContext;

    private IEManager() {
        // init freetype
        FreeType.init();
    }

    public static IEManager getInstance() {
        return Holder.sInstance;
    }

    public void init(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public void prepare(Surface surface, int width, int height) {
        mWorker = new IEWorker();
        mWorker.startWorking(surface, width, height);
    }

    public boolean addOperator(AbstractOperator operator) {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return false;
        }

        if (operator == null) {
            Logger.e(TAG, "Operator can not be null!");
            return false;
        }

        if (!operator.checkRational()) {
            Logger.e(TAG, "Operator check rational failed, can not be executed! op: " + operator.getName());
            return false;
        }

        mWorker.execOperator(operator);

        return true;
    }

    public boolean updateOperator(AbstractOperator operator) {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return false;
        }

        if (operator == null) {
            Logger.e(TAG, "Operator can not be null!");
            return false;
        }

        if (!operator.checkRational()) {
            Logger.e(TAG, "Operator check rational failed, can not be executed! op: " + operator.getName());
            return false;
        }

        return mWorker.updateOperator(operator);
    }

    public boolean addOperator(List<AbstractOperator> operatorList) {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return false;
        }

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

    public boolean undo() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return false;
        }

        mWorker.undo();

        return true;
    }

    public boolean redo() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return false;
        }

        mWorker.redo();

        return true;
    }

    public boolean removeOperator(AbstractOperator operator) {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return false;
        }

        if (operator == null) {
            Logger.e(TAG, "Operator can not be null!");
            return false;
        }

        if (!operator.checkRational()) {
            Logger.e(TAG, "Operator check rational failed, can not be executed! op: " + operator.getName());
            return false;
        }

        return mWorker.removeOperator(operator);
    }

    public boolean removeOperator(List<AbstractOperator> operatorList) {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return false;
        }

        if (operatorList == null || operatorList.size() == 0) {
            Logger.e(TAG, "Operator list is null or empty!");
            return false;
        }

        return mWorker.removeOperator(operatorList);
    }

    public List<AbstractOperator> getOperatorList() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return null;
        }

        return mWorker.getOpList();
    }

    public void save(File target, SaveListener listener) {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return;
        }

        if (target == null) {
            Logger.e(TAG, "Target file can not be null!");
            return;
        }

        mWorker.save(target, listener);
    }

    public void stop() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return;
        }

        mWorker.stopWork();
        mWorker = null;
    }

    public int getImgShowWidth() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return -1;
        }

        return mWorker.getImgShowWidth();
    }

    public int getImgShowHeight() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return -1;
        }

        return mWorker.getImgShowHeight();
    }

    public int getImgShowTop() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return -1;
        }

        return mWorker.getImgShowTop();
    }

    public int getImgShowLeft() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return -1;
        }

        return mWorker.getImgShowLeft();
    }

    public int getImgShowBottom() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return -1;
        }

        return mWorker.getImgShowBottom();
    }

    public int getImgShowRight() {
        if (mWorker == null) {
            Logger.e(TAG, "Failed, not prepared before!");
            return -1;
        }

        return mWorker.getImgShowRight();
    }

    public Context getContext() {
        return mAppContext;
    }

    private static class Holder {
        private static final IEManager sInstance = new IEManager();
    }
}
