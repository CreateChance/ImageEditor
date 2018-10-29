package com.createchance.imageeditor;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Surface;

import com.createchance.imageeditor.gles.EglCore;
import com.createchance.imageeditor.gles.WindowSurface;
import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/10/29
 */
class IEWorker extends HandlerThread {

    private static final String TAG = "IEWorker";

    private final int MSG_START = 0;
    private final int MSG_SET_BASE_IMAGE = 1;
    private final int MSG_NEW_OP = 2;
    private final int MSG_REMOVE_OP = 3;

    private Handler mHandler;

    private Bitmap mBaseImage;

    // gles
    private EglCore mEglCore;
    private WindowSurface mWindowSurface;
    private int mBaseWidth, mBaseHeight;

    // operator list
    private List<AbstractOperator> mOpList;

    public IEWorker() {
        super("IEWorker thread");
        mOpList = new ArrayList<>();
    }

    public IEWorker(int priority) {
        super("IEWorker thread", priority);
        mOpList = new ArrayList<>();
    }

    public void startWorking(Surface surface, int width, int height) {
        start();
        mHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_START:
                        handleStart((Surface) msg.obj, msg.arg1, msg.arg2);
                        break;
                    case MSG_SET_BASE_IMAGE:
                        handleBaseImage((Bitmap) msg.obj);
                        break;
                    case MSG_NEW_OP:
                        handleNewOperator((AbstractOperator) msg.obj);
                        break;
                    case MSG_REMOVE_OP:
                        handleRemoveOperator((AbstractOperator) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
        Message message = Message.obtain();
        message.what = MSG_START;
        message.obj = surface;
        message.arg1 = width;
        message.arg2 = height;
        mHandler.sendMessage(message);
    }

    public void setBaseImage(Bitmap image) {
        Message message = Message.obtain();
        message.what = MSG_SET_BASE_IMAGE;
        message.obj = image;
        mHandler.sendMessage(message);
    }

    public void execOperator(AbstractOperator operator) {
        Message message = Message.obtain();
        message.what = MSG_NEW_OP;
        message.obj = operator;
        mHandler.sendMessage(message);
    }

    public void removeOperator(AbstractOperator operator) {
        Message message = Message.obtain();
        message.what = MSG_REMOVE_OP;
        message.obj = operator;
        mHandler.sendMessage(message);
    }

    public List<AbstractOperator> getOpList() {
        return mOpList;
    }

    private void handleStart(Surface surface, int width, int height) {
        mEglCore = new EglCore(null, EglCore.FLAG_RECORDABLE);
        mWindowSurface = new WindowSurface(mEglCore, surface, true);
        mBaseWidth = width;
        mBaseHeight = height;
    }

    private void handleBaseImage(Bitmap image) {
        mBaseImage = image;
        BaseImageDrawer imageDrawer = new BaseImageDrawer(null, null);
        imageDrawer.draw(image, 0, 0, mBaseWidth, mBaseHeight);
    }

    private void handleNewOperator(AbstractOperator operator) {
        mOpList.add(operator);
        operator.exec();
    }

    private void handleRemoveOperator(AbstractOperator operator) {
        if (mOpList.contains(operator)) {
            mOpList.remove(operator);
            for (AbstractOperator op : mOpList) {
                op.exec();
            }
        } else {
            Logger.e(TAG, "No such operator! Can not remove.");
        }
    }
}
