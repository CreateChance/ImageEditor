package com.createchance.imageeditor;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.view.TextureView;

import com.createchance.imageeditor.freetype.FreeType;
import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * IE manager, api class.
 *
 * @author createchance
 * @date 2018/10/28
 */
public class IEManager {

    private static final String TAG = "IEManager";

    private Context mAppContext;

    private List<IEClip> mClipList = new ArrayList<>();

    // render target
    private IRenderTarget mPreviewTarget;
    private IRenderTarget mSaveTarget;

    private GLRenderThread mRenderThread;

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

    public void startEngine() {
        mRenderThread = new GLRenderThread();
        mRenderThread.start();
    }

    public void stopEngine() {
        if (mRenderThread == null) {
            Logger.e(TAG, "Call startEngine first!!!");
            return;
        }
        for (IEClip clip : mClipList) {
            clip.release();
        }
        mClipList.clear();
        mRenderThread.quitSafely();
    }

    public void attachPreview(IEPreviewView previewView) {
        mPreviewTarget = previewView;
        previewView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
                Logger.d(TAG, "onSurfaceTextureAvailable, width: " + width + ", height: " + height);
                mRenderThread.post(new Runnable() {
                    @Override
                    public void run() {
                        mPreviewTarget.init(mRenderThread.getEglCore(), surface);
                        mPreviewTarget.makeCurrent();
                        // adjust img render size.
                        for (IEClip clip : mClipList) {
                            clip.adjustSize();
                        }
                        // render first clip for now.
                        renderClip(0);
                    }
                });
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                Logger.d(TAG, "onSurfaceTextureSizeChanged, width: " + width + ", height: " + height);
                // todo: reset env when surface texture size changed.
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    public void detachPreview(IEPreviewView previewView) {
        mRenderThread.post(new Runnable() {
            @Override
            public void run() {
                mPreviewTarget.release();
                mPreviewTarget = null;
            }
        });
    }

    public IEClip addClip(String imagePath) {
        return addClip(imagePath, 0);
    }

    public IEClip addClip(String imagePath, long duration) {
        if (TextUtils.isEmpty(imagePath)) {
            Logger.e(TAG, "Image path can not be null or empty!");
            return null;
        }

        if (duration < 0) {
            Logger.e(TAG, "Duration can not < 0!");
            return null;
        }

        long startTime = 0;
        for (IEClip ieClip : mClipList) {
            startTime += ieClip.getDuration();
        }
        IEClip clip = new IEClip(imagePath, startTime, startTime + duration, mRenderThread);
        clip.setRenderTarget(mPreviewTarget);
        mClipList.add(clip);

        return clip;
    }

    public boolean addOperator(int clipIndex, AbstractOperator operator) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Add operator failed, clip index invalid: " + clipIndex);
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

        IEClip clip = mClipList.get(clipIndex);
        clip.addOperator(operator);

        return true;
    }

    public boolean renderClip(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Render clip failed, clip index invalid: " + clipIndex);
            return false;
        }

        IEClip clip = mClipList.get(clipIndex);
        clip.render();
        return true;
    }

    public boolean undo(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Undo operator failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).undo();

        return true;
    }

    public boolean redo(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Redo operator failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).redo();

        return true;
    }

    public boolean removeOperator(int clipIndex, AbstractOperator operator) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Remove operator failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).removeOperator(operator);

        return true;
    }

    public boolean removeOperator(int clipIndex, List<AbstractOperator> operatorList) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Remove operator list failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).removeOperator(operatorList);

        return true;
    }

    public List<IEClip> getClipList() {
        return mClipList;
    }

    public IEClip getClip(int clipIndex) {
        return mClipList.get(clipIndex);
    }

    public void save(File target, SaveListener listener) {
        if (target == null) {
            Logger.e(TAG, "Target file can not be null!");
            return;
        }
    }

    public boolean generatorHistogram(int clipIndex, IHistogramGenerateListener listener) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Generator histogram failed, clip index invalid: " + clipIndex);
            return false;
        }

        if (listener == null) {
            Logger.e(TAG, "Listener can not be null!");
            return false;
        }

        mClipList.get(clipIndex).generatorHistogram(listener);

        return true;
    }

    public Context getContext() {
        return mAppContext;
    }

    private static class Holder {
        private static final IEManager sInstance = new IEManager();
    }
}
