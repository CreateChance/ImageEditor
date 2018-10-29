package com.createchance.imageeditor;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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
import java.util.concurrent.Semaphore;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/10/29
 */
class IEWorker extends HandlerThread {

    private static final String TAG = "IEWorker";

    private final int MSG_START = 0;
    private final int MSG_STOP = 1;
    private final int MSG_NEW_OP = 2;
    private final int MSG_REMOVE_OP = 3;
    private final int MSG_SAVE = 4;

    private Handler mHandler;

    // gles
    private EglCore mEglCore;
    private WindowSurface mWindowSurface;
    private int mSurfaceWidth, mSurfaceHeight;
    private int mBaseImgWidth, mBaseImgHeight;
    private int mBaseImgPosX, mBaseImgPosY;
    private int[] mFboBuffer = new int[1];
    private int[] mFboTextureIds = new int[2];
    private BaseImageDrawer mFboReader;

    private int mSaveTexture;

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
                    case MSG_STOP:
                        handleStop();
                        break;
                    case MSG_NEW_OP:
                        handleNewOperator((AbstractOperator) msg.obj);
                        break;
                    case MSG_REMOVE_OP:
                        handleRemoveOperator((AbstractOperator) msg.obj);
                        break;
                    case MSG_SAVE:
                        List<Object> saveParams = (List<Object>) msg.obj;
                        handleSave((File) saveParams.get(0), (SaveListener) saveParams.get(1));
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

    public void save(File target, SaveListener listener) {
        Message message = Message.obtain();
        message.what = MSG_SAVE;
        List<Object> params = new ArrayList<>(2);
        params.add(target);
        params.add(listener);
        message.obj = params;
        mHandler.sendMessage(message);
    }

    public void stopWork() {
        mHandler.sendEmptyMessage(MSG_STOP);
    }

    private void handleStart(Surface surface, int width, int height) {
        mEglCore = new EglCore();
        mWindowSurface = new WindowSurface(mEglCore, surface, true);
        mWindowSurface.makeCurrent();
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        createOffScreenFrameBuffer();
        createOffScreenTextures();
        mSaveTexture = mFboTextureIds[0];

        mFboReader = new BaseImageDrawer(1.0f, false, false);
    }

    private void handleStop() {
        deleteOffScreenFrameBuffer();
        mEglCore.release();
        mWindowSurface.release();
        quitSafely();
    }

    private void handleNewOperator(AbstractOperator operator) {
        switch (operator.getType()) {
            case AbstractOperator.OP_BASE_IMAGE:
                adjustBaseImage((BaseImageOperator) operator);
                break;
            case AbstractOperator.OP_FILTER:
                FilterOperator filterOperator = (FilterOperator) operator;
                filterOperator.setSize(mBaseImgWidth, mBaseImgHeight);
                filterOperator.setInputTexture(mFboTextureIds[0]);
                filterOperator.setOutputTexture(mFboTextureIds[1]);
                filterOperator.setFrameBuffer(mFboBuffer[0]);
                break;
            case AbstractOperator.OP_TEXT:
                break;
            default:
                break;
        }

        mOpList.add(operator);

        if (operator.getType() == AbstractOperator.OP_FILTER) {
            bindOffScreenFrameBuffer(mFboTextureIds[1]);
            operator.exec();
            bindDefaultFrameBuffer();
            mSaveTexture = mFboTextureIds[1];
        } else {
            bindOffScreenFrameBuffer(mFboTextureIds[0]);
            operator.exec();
            bindDefaultFrameBuffer();
            mSaveTexture = mFboTextureIds[0];
        }
        mFboReader.draw(mSaveTexture, 0, 0, mSurfaceWidth, mSurfaceHeight);
        mWindowSurface.swapBuffers();
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

    private void handleSave(File target, SaveListener listener) {
        bindOffScreenFrameBuffer(mSaveTexture);
        captureImage(target, mBaseImgPosX, mBaseImgPosY, mBaseImgWidth, mBaseImgHeight, listener);
        bindDefaultFrameBuffer();
    }

    private void adjustBaseImage(BaseImageOperator operator) {
        int imgWidth = operator.getImage().getWidth();
        int imgHeight = operator.getImage().getHeight();
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
        mBaseImgWidth = imgWidth;
        mBaseImgHeight = imgHeight;
        operator.setPosition(mBaseImgPosX, mBaseImgPosY);
    }

    private void captureImage(File target, int posX, int posY, int width, int height, final SaveListener listener) {
        final Semaphore waiter = new Semaphore(0);

        // Take picture on OpenGL thread
        final int[] pixelMirroredArray = new int[width * height];
        final IntBuffer pixelBuffer = IntBuffer.allocate(width * height);
        GLES20.glReadPixels(posX, posY, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
        int[] pixelArray = pixelBuffer.array();

        // Convert upside down mirror-reversed image to right-side up normal image.
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelMirroredArray[(height - i - 1) * width + j] = pixelArray[i * width + j];
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

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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
