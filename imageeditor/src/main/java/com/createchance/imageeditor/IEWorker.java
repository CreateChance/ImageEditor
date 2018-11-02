package com.createchance.imageeditor;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import com.createchance.imageeditor.gles.EglCore;
import com.createchance.imageeditor.gles.WindowSurface;
import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.ops.BaseImageOperator;
import com.createchance.imageeditor.ops.FilterOperator;
import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditor.utils.UiThreadUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Semaphore;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/10/29
 */
public class IEWorker extends HandlerThread {

    private static final String TAG = "IEWorker";

    private final int MSG_START = 0;
    private final int MSG_STOP = 1;
    private final int MSG_NEW_OP = 2;
    private final int MSG_UPDATE_OP = 3;
    private final int MSG_UNDO_OP = 4;
    private final int MSG_REDO_OP = 5;
    private final int MSG_SAVE_OP = 6;
    private final int MSG_REMOVE_OP = 7;
    private final int MSG_REMOVE_OP_LIST = 8;

    private Handler mHandler;

    // gles
    private EglCore mEglCore;
    private WindowSurface mWindowSurface;
    private int mSurfaceWidth, mSurfaceHeight;
    private int mBaseImgShowWidth, mBaseImgShowHeight;
    private int mImgOriginWidth, mImgOriginHeight;
    private int mBaseImgPosX, mBaseImgPosY;
    private int[] mFboBuffer = new int[1];
    private int[] mFboTextureIds = new int[2];
    private int mCurrentTextureIndex = 0;
    private BaseImageDrawer mFboReader;

    // operator list
    private Stack<AbstractOperator> mOpList;
    private Stack<AbstractOperator> mRemovedOps;

    public IEWorker() {
        super("IEWorker thread");
        mOpList = new Stack<>();
        mRemovedOps = new Stack<>();
    }

    public IEWorker(int priority) {
        super("IEWorker thread", priority);
        mOpList = new Stack<>();
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
                    case MSG_STOP:
                        handleStop();
                        break;
                    case MSG_NEW_OP:
                        AbstractOperator operatorAdd = (AbstractOperator) msg.obj;
                        mOpList.push(operatorAdd);
                        mRemovedOps.clear();
                        handleOperator((AbstractOperator) msg.obj, true);
                        break;
                    case MSG_UPDATE_OP:
                        handleUpdateOperator((AbstractOperator) msg.obj);
                        break;
                    case MSG_UNDO_OP:
                        handleUndo();
                        break;
                    case MSG_REDO_OP:
                        handleRedo();
                        break;
                    case MSG_SAVE_OP:
                        List<Object> saveParams = (List<Object>) msg.obj;
                        handleSave((File) saveParams.get(0), (SaveListener) saveParams.get(1));
                        break;
                    case MSG_REMOVE_OP:
                        AbstractOperator removedOp = (AbstractOperator) msg.obj;
                        handleRemoveOp(removedOp);
                        break;
                    case MSG_REMOVE_OP_LIST:
                        handleRemoveOpList((List<AbstractOperator>) msg.obj);
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

    public void execOperator(AbstractOperator operator) {
        if (mOpList.contains(operator)) {
            Logger.w(TAG, "Operator already added!");
            return;
        }

        operator.setWorker(this);
        Message message = Message.obtain();
        message.what = MSG_NEW_OP;
        message.obj = operator;
        mHandler.sendMessage(message);
    }

    public boolean updateOperator(AbstractOperator operator) {
        if (!mOpList.contains(operator)) {
            Logger.e(TAG, "No such operator, can not update! op: " + operator.getName());
            return false;
        }

        Message message = Message.obtain();
        message.what = MSG_UPDATE_OP;
        message.obj = operator;
        mHandler.sendMessage(message);

        return true;
    }

    public void undo() {
        mHandler.sendEmptyMessage(MSG_UNDO_OP);
    }

    public void redo() {
        mHandler.sendEmptyMessage(MSG_REDO_OP);
    }

    public boolean removeOperator(AbstractOperator operator) {
        if (!mOpList.contains(operator) && !mRemovedOps.contains(operator)) {
            return false;
        }

        Message message = Message.obtain();
        message.what = MSG_REMOVE_OP;
        message.obj = operator;
        mHandler.sendMessage(message);

        return true;
    }

    public boolean removeOperator(List<AbstractOperator> operatorList) {
        for (AbstractOperator operator : operatorList) {
            if (!mOpList.contains(operator) && !mRemovedOps.contains(operator)) {
                Logger.e(TAG, "Got one op not contained in op list, remove failed.");
                return false;
            }
        }

        Message message = Message.obtain();
        message.what = MSG_REMOVE_OP_LIST;
        message.obj = operatorList;
        mHandler.sendMessage(message);

        Log.e(TAG, "removeOperator: " + operatorList.size());

        return true;
    }

    public List<AbstractOperator> getOpList() {
        return mOpList;
    }

    public void save(File target, SaveListener listener) {
        Message message = Message.obtain();
        message.what = MSG_SAVE_OP;
        List<Object> params = new ArrayList<>(2);
        params.add(target);
        params.add(listener);
        message.obj = params;
        mHandler.sendMessage(message);
    }

    public void stopWork() {
        mHandler.sendEmptyMessage(MSG_STOP);
    }

    public int getImgShowWidth() {
        return mBaseImgShowWidth;
    }

    public int getImgShowHeight() {
        return mBaseImgShowHeight;
    }

    public int getImgShowTop() {
        return mBaseImgPosY + mBaseImgShowHeight;
    }

    public int getImgShowLeft() {
        return mBaseImgPosX;
    }

    public int getImgShowBottom() {
        return mBaseImgPosY;
    }

    public int getImgShowRight() {
        return mBaseImgPosX + mBaseImgShowWidth;
    }

    private void handleStart(Surface surface, int width, int height) {
        mEglCore = new EglCore();
        mWindowSurface = new WindowSurface(mEglCore, surface, true);
        mWindowSurface.makeCurrent();
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        createOffScreenFrameBuffer();
        createOffScreenTextures();

        mFboReader = new BaseImageDrawer(1.0f, false, false);
    }

    private void handleStop() {
        deleteOffScreenFrameBuffer();
        mEglCore.release();
        mWindowSurface.release();
        quitSafely();
    }

    private void handleOperator(AbstractOperator operator, boolean swap) {
        switch (operator.getType()) {
            case AbstractOperator.OP_BASE_IMAGE:
                adjustBaseImage((BaseImageOperator) operator);
                break;
            case AbstractOperator.OP_FILTER:
                FilterOperator filterOperator = (FilterOperator) operator;
                filterOperator.setSize(mBaseImgShowWidth, mBaseImgShowHeight);
                mCurrentTextureIndex = (mCurrentTextureIndex + 1) % mFboTextureIds.length;
                filterOperator.setInputTexture(mFboTextureIds[(mCurrentTextureIndex + 1) % mFboTextureIds.length]);
                filterOperator.setOutputTexture(mFboTextureIds[mCurrentTextureIndex]);
                filterOperator.setFrameBuffer(mFboBuffer[0]);
                break;
            case AbstractOperator.OP_TEXT:
                break;
            default:
                break;
        }

        if (operator.getType() == AbstractOperator.OP_FILTER) {
            bindOffScreenFrameBuffer(mFboTextureIds[mCurrentTextureIndex]);
            operator.exec();
            bindDefaultFrameBuffer();
        } else {
            bindOffScreenFrameBuffer(mFboTextureIds[mCurrentTextureIndex]);
            operator.exec();
            bindDefaultFrameBuffer();
        }
        mFboReader.draw(mFboTextureIds[mCurrentTextureIndex], 0, 0, mSurfaceWidth, mSurfaceHeight);
        if (swap) {
            mWindowSurface.swapBuffers();
        }
    }

    public void handleUpdateOperator(AbstractOperator operator) {
        for (AbstractOperator op : mOpList) {
            handleOperator(op, false);
        }
        mWindowSurface.swapBuffers();
    }

    private void handleUndo() {
        if (mOpList.size() > 1) {
            AbstractOperator removedOp = mOpList.pop();
            mRemovedOps.push(removedOp);
            for (AbstractOperator op : mOpList) {
                handleOperator(op, false);
            }
            mWindowSurface.swapBuffers();
        } else {
            Logger.e(TAG, "Can not undo for now.");
        }
    }

    private void handleRedo() {
        if (!mRemovedOps.empty()) {
            AbstractOperator operator = mRemovedOps.pop();
            mOpList.push(operator);
            handleOperator(operator, true);
        } else {
            Logger.e(TAG, "Can not redo for now.");
        }
    }

    private void handleSave(File target, SaveListener listener) {
        bindOffScreenFrameBuffer(mFboTextureIds[mCurrentTextureIndex]);
        captureImage(target, listener);
        bindDefaultFrameBuffer();
    }

    private void handleRemoveOp(AbstractOperator operator) {
        if (mOpList.contains(operator)) {
            mOpList.remove(operator);

            for (AbstractOperator op : mOpList) {
                handleOperator(op, false);
            }
            mWindowSurface.swapBuffers();
        } else if (mRemovedOps.contains(operator)) {
            mRemovedOps.remove(operator);
        }
    }

    private void handleRemoveOpList(List<AbstractOperator> operatorList) {
        boolean needDraw = false;
        for (AbstractOperator op : operatorList) {
            if (mOpList.contains(op)) {
                mOpList.remove(op);
                needDraw = true;
            } else if (mRemovedOps.contains(op)) {
                mRemovedOps.remove(op);
            }
        }
        if (needDraw) {
            for (AbstractOperator op : mOpList) {
                handleOperator(op, false);
            }
            mWindowSurface.swapBuffers();
        }
    }

    private void adjustBaseImage(BaseImageOperator operator) {
        mImgOriginWidth = operator.getImage().getWidth();
        mImgOriginHeight = operator.getImage().getHeight();
        int imgWidth = mImgOriginWidth;
        int imgHeight = mImgOriginHeight;
        float scale = 1.0f;
        if (imgWidth > imgHeight) {
            if (imgWidth > mSurfaceWidth) {
                scale = mSurfaceWidth * 1.0f / imgWidth;
                imgWidth = mSurfaceWidth;
                imgHeight = (int) (imgHeight * scale);
            }
        } else {
            if (imgHeight > mSurfaceHeight) {
                scale = mSurfaceHeight * 1.0f / imgHeight;
                imgHeight = mSurfaceHeight;
                imgWidth = (int) (imgWidth * scale);
            }
        }
        operator.setWidth(imgWidth);
        operator.setHeight(imgHeight);
        mBaseImgPosX = 0;
        mBaseImgPosY = (mSurfaceHeight - imgHeight) / 2;
        mBaseImgShowWidth = imgWidth;
        mBaseImgShowHeight = imgHeight;
        operator.setPosition(mBaseImgPosX, mBaseImgPosY);
    }

    private void captureImage(File target, final SaveListener listener) {
        final Semaphore waiter = new Semaphore(0);

        // Take picture on OpenGL thread
        final int[] pixelMirroredArray = new int[mBaseImgShowWidth * mBaseImgShowHeight];
        final IntBuffer pixelBuffer = IntBuffer.allocate(mBaseImgShowWidth * mBaseImgShowHeight);
        GLES20.glReadPixels(mBaseImgPosX,
                mBaseImgPosY,
                mBaseImgShowWidth,
                mBaseImgShowHeight,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                pixelBuffer);
        int[] pixelArray = pixelBuffer.array();

        // Convert upside down mirror-reversed image to right-side up normal image.
        for (int i = 0; i < mBaseImgShowHeight; i++) {
            for (int j = 0; j < mBaseImgShowWidth; j++) {
                pixelMirroredArray[(mBaseImgShowHeight - i - 1) * mBaseImgShowWidth + j] =
                        pixelArray[i * mBaseImgShowWidth + j];
            }
        }
        waiter.release();
        try {
            waiter.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            UiThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onSaveFailed();
                    }
                }
            });
            return;
        }

        Bitmap bitmap = Bitmap.createBitmap(mBaseImgShowWidth, mBaseImgShowHeight, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(pixelMirroredArray));
        saveBitmap(bitmap, target, listener);
    }

    private static void saveBitmap(Bitmap bitmap, final File picFile, final SaveListener listener) {
        try {
            FileOutputStream out = new FileOutputStream(picFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            UiThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onSaved(picFile);
                    }
                }
            });
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            UiThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onSaveFailed();
                    }
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            UiThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onSaveFailed();
                    }
                }
            });
        }
    }

    private void createOffScreenTextures() {
        GLES20.glGenTextures(mFboTextureIds.length, mFboTextureIds, 0);
        for (int mTextureId : mFboTextureIds) {
            // bind to fbo texture cause we are going to do setting.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mSurfaceWidth, mSurfaceHeight,
                    0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
            // 设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            // 设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            // 设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            // 设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            // unbind fbo texture.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    private void createOffScreenFrameBuffer() {
        GLES20.glGenFramebuffers(mFboBuffer.length, mFboBuffer, 0);
    }

    private void bindOffScreenFrameBuffer(int textureId) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFboBuffer[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, textureId, 0);
    }

    private void bindDefaultFrameBuffer() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void deleteOffScreenFrameBuffer() {
        GLES20.glDeleteFramebuffers(mFboBuffer.length, mFboBuffer, 0);
    }
}
