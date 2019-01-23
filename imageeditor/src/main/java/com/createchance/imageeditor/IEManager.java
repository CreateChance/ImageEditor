package com.createchance.imageeditor;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.view.TextureView;

import com.createchance.imageeditor.freetype.FreeType;
import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.transitions.AbstractTransition;
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

    private long mCurrentPosition;

    // render target
    private IRenderTarget mPreviewTarget;
    private IRenderTarget mSaveTarget;

    private GLRenderThread mRenderThread;

    public static final int IMG_FORMAT_PNG = 1;
    public static final int IMG_FORMAT_JPEG = 2;
    public static final int IMG_FORMAT_WEBP = 3;

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
        // release resources.
        mRenderThread.post(new Runnable() {
            @Override
            public void run() {
                for (IEClip clip : mClipList) {
                    clip.releaseImage();
                    clip.releaseTexture();
                }
            }
        });
        mClipList.clear();
        mRenderThread.quitSafely();
    }

    public void attachPreview(IEPreviewView previewView) {
        mPreviewTarget = previewView;
        previewView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                Logger.d(TAG, "onSurfaceTextureAvailable, width: " + width + ", height: " + height);
                mRenderThread.post(new Runnable() {
                    @Override
                    public void run() {
                        mPreviewTarget.init(mRenderThread.getEglCore());
                        mPreviewTarget.makeCurrent();
                        // set render target
                        for (IEClip clip : mClipList) {
                            clip.setRenderTarget(mPreviewTarget);
                            clip.loadImage(true);
                            clip.loadTexture();
                        }

                        if (mClipList.size() > 0) {
                            // render first clip for now.
                            mClipList.get(0).render(true, 0);
                        }
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
                Logger.d(TAG, "onSurfaceTextureDestroyed: ");
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                Logger.d(TAG, "onSurfaceTextureUpdated: ");
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

    public boolean addClip(String imagePath) {
        return addClip(imagePath, 0);
    }

    public boolean addClip(String imagePath, long duration) {
        if (TextUtils.isEmpty(imagePath)) {
            Logger.e(TAG, "Image path can not be null or empty!");
            return false;
        }

        if (duration < 0) {
            Logger.e(TAG, "Duration can not < 0!");
            return false;
        }

        long startTime = 0;
        for (IEClip ieClip : mClipList) {
            startTime += ieClip.getDuration();
        }
        IEClip clip = new IEClip(imagePath, startTime, startTime + duration);
        mClipList.add(clip);
        clip.loadImage(true);
        if (clip.getBitmap() != null) {
            mRenderThread.post(new Runnable() {
                @Override
                public void run() {
                    mClipList.get(mClipList.size() - 1).loadTexture();
                }
            });
        }

        return true;
    }

    public boolean setClipDuration(int clipIndex, long duration) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Add operator failed, clip index invalid: " + clipIndex);
            return false;
        }

        if (duration < 0) {
            Logger.e(TAG, "Clip duration can not < 0.");
            return false;
        }

        mClipList.get(clipIndex).setDuration(duration);

        return true;
    }

    public boolean seek(long position) {
        if (position < 0 || position > getTotalDuration()) {
            Logger.e(TAG, "Position invalid, seek failed!");
            return false;
        }

        // do seek here.
        renderAtPosition(position);

        return true;
    }

    public long getCurrentPosition() {
        return mCurrentPosition;
    }

    public boolean playback(long startPosition, long duration) {
        if (startPosition < 0 || startPosition > getTotalDuration()) {
            Logger.e(TAG, "Start position invalid, playback failed!");
            return false;
        }

        if (duration <= 0 || duration > getTotalDuration() - startPosition) {
            Logger.e(TAG, "Duration invalid, playback failed!");
            return false;
        }

        // do playback here.

        return true;
    }

    public long getTotalDuration() {
        long duration = 0;
        for (IEClip clip : mClipList) {
            duration += clip.getDuration();
        }

        return duration;
    }

    public long getDuration(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Add operator failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getDuration();
    }

    public boolean addOperator(int clipIndex, AbstractOperator operator, boolean render) {
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

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean updateOperator(int clipIndex, AbstractOperator operator, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Update operator failed, clip index invalid: " + clipIndex);
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

        final IEClip clip = mClipList.get(clipIndex);
        clip.updateOperator(operator);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean undo(int clipIndex, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Undo operator failed, clip index invalid: " + clipIndex);
            return false;
        }

        boolean ret = mClipList.get(clipIndex).undo();

        if (render) {
            renderClip(clipIndex);
        }

        return ret;
    }

    public boolean redo(int clipIndex, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Redo operator failed, clip index invalid: " + clipIndex);
            return false;
        }

        boolean ret = mClipList.get(clipIndex).redo();

        if (render) {
            renderClip(clipIndex);
        }

        return ret;
    }

    public boolean removeOperator(int clipIndex, AbstractOperator operator, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Remove operator failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).removeOperator(operator);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean removeOperator(int clipIndex, List<AbstractOperator> operatorList, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Remove operator list failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).removeOperator(operatorList);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean setTransition(int clipIndex, AbstractTransition transition, long duration, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Remove operator list failed, clip index invalid: " + clipIndex);
            return false;
        }

        if (transition == null || !transition.checkRational()) {
            Logger.e(TAG, "Transition invalid: " + transition);
            return false;
        }

        mClipList.get(clipIndex).setTransition(transition, duration);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean removeTransition(int clipIndex, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Remove operator list failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).removeTransition();

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean setTranslateX(int clipIndex, float translateX, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Set translate x failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).setTranslateX(translateX);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean setTranslateY(int clipIndex, float translateY, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Set translate y failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).setTranslateY(translateY);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean setScaleX(int clipIndex, float scaleX, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Set scale x failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).setScaleX(scaleX);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean setScaleY(int clipIndex, float scaleY, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Set scale y failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).setScaleY(scaleY);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean setScissorX(int clipIndex, float scissorX, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Set scissor x failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).setScissorX(scissorX);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean setScissorY(int clipIndex, float scissorY, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Set scissor y failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).setScissorY(scissorY);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean setScissorWidth(int clipIndex, float scissorWidth, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Set scissor width failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).setScissorWidth(scissorWidth);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public boolean setScissorHeight(int clipIndex, float scissorHeight, boolean render) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Set scissor height failed, clip index invalid: " + clipIndex);
            return false;
        }

        mClipList.get(clipIndex).setScissorHeight(scissorHeight);

        if (render) {
            renderClip(clipIndex);
        }

        return true;
    }

    public float getTranslateX(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get translate X failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getTranslateX();
    }

    public float getTranslateY(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get translate Y failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getTranslateY();
    }

    public float getScaleX(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get scale X failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getScaleX();
    }

    public float getScaleY(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get scale Y failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getScaleY();
    }

    public int getOriginWidth(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get origin width failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getOriginWidth();
    }

    public int getOriginHeight(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get origin height failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getOriginHeight();
    }

    public int getSurfaceWidth(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get surface width failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getSurfaceWidth();
    }

    public int getSurfaceHeight(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get surface height failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getSurfaceHeight();
    }

    public float getScissorX(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get scissor x failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getScissorX();
    }

    public float getScissorY(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get scissor y failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getScissorY();
    }

    public float getScissorWidth(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get scissor width failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getScissorWidth();
    }

    public float getScissorHeight(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get scissor height failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getScissorHeight();
    }

    public int getRenderLeft(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get render left failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getRenderLeft();
    }

    public int getRenderTop(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get render top failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getRenderTop();
    }

    public int getRenderRight(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get render right failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getRenderRight();
    }

    public int getRenderBottom(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get render bottom failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getRenderBottom();
    }

    public int getRenderWidth(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get render width failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getRenderWidth();
    }

    public int getRenderHeight(int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Get render height failed, clip index invalid: " + clipIndex);
            return -1;
        }

        return mClipList.get(clipIndex).getRenderHeight();
    }

    public List<IEClip> getClipList() {
        return mClipList;
    }

    public IEClip getClip(int clipIndex) {
        return mClipList.get(clipIndex);
    }

    public boolean saveAsImage(int clipIndex, File target, int format, int quality, SaveListener listener) {
        return saveAsImage(clipIndex, -1, -1, format, quality, target, listener);
    }

    public boolean saveAsImage(final int clipIndex,
                               int width,
                               int height,
                               int format,
                               int quality,
                               File target,
                               SaveListener listener) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Save image failed, clip index invalid: " + clipIndex);
            return false;
        }

        if (format != IMG_FORMAT_JPEG && format != IMG_FORMAT_PNG && format != IMG_FORMAT_WEBP) {
            Logger.e(TAG, "Save image failed, format error, current format: " + format);
            return false;
        }

        if (quality < 0 || quality > 100) {
            Logger.e(TAG, "Save image failed, quality invalid: " + quality);
            return false;
        }

        if (target == null) {
            Logger.e(TAG, "Target file can not be null!");
            return false;
        }

        int originWidth = getOriginWidth(clipIndex);
        int originHeight = getOriginHeight(clipIndex);
        if (width < 0) {
            width = (int) (getScissorWidth(clipIndex) * originWidth);
        }
        if (height < 0) {
            height = (int) (getScissorHeight(clipIndex) * originHeight);
        }
        mSaveTarget = new ImageSaver(originWidth,
                originHeight,
                (int) (getScissorX(clipIndex) * originWidth),
                (int) (getScissorY(clipIndex) * originHeight),
                width,
                height,
                format,
                quality,
                target,
                listener);
        mRenderThread.post(new Runnable() {
            @Override
            public void run() {
                mClipList.get(clipIndex).setRenderTarget(mSaveTarget);
                // reload image and texture
                mClipList.get(clipIndex).releaseImage();
                mClipList.get(clipIndex).releaseTexture();
                long loadImageStart = System.currentTimeMillis();
                mClipList.get(clipIndex).loadImage(false);
                long loadTextureStart = System.currentTimeMillis();
                mClipList.get(clipIndex).loadTexture();
                long loadTextureEnd = System.currentTimeMillis();
                Logger.e(TAG, "Load time, image: " + (loadTextureStart - loadImageStart) +
                        ", texture: " + (loadTextureEnd - loadTextureStart));
                // reset clip's translate and scale.
                mClipList.get(clipIndex).setTranslateX(0);
                mClipList.get(clipIndex).setTranslateY(0);
                mClipList.get(clipIndex).setScaleX(1.0f);
                mClipList.get(clipIndex).setScaleY(1.0f);

                mSaveTarget.init(mRenderThread.getEglCore());
                mSaveTarget.makeCurrent();
                mClipList.get(clipIndex).render(true, 0);

                mPreviewTarget.makeCurrent();
                mClipList.get(clipIndex).setRenderTarget(mPreviewTarget);
                // reload image and texture.
                mClipList.get(clipIndex).releaseImage();
                mClipList.get(clipIndex).loadImage(false);
                mClipList.get(clipIndex).releaseTexture();
                mClipList.get(clipIndex).loadTexture();
            }
        });

        return true;
    }

    public boolean saveAsVideo(int width,
                               int height,
                               int orientation,
                               File target,
                               File bgmFile,
                               long bgmStartTimeMs,
                               SaveListener saveListener) {
        if (width <= 0 || height <= 0) {
            Logger.e(TAG, "Output size invalid, width: " + width + ", height: " + height);
        }
        if (target == null) {
            Logger.e(TAG, "Target file can not be null!");
            return false;
        }

        long totalDuration = 0;
        for (IEClip clip : mClipList) {
            totalDuration += clip.getDuration();
        }
        mSaveTarget = new VideoSaver(width, height, orientation, totalDuration, target, bgmFile, bgmStartTimeMs, saveListener);
        for (IEClip clip : mClipList) {
            clip.setRenderTarget(mSaveTarget);
            // reload image.
            clip.releaseImage();
            clip.loadImage(false);
        }
        mRenderThread.post(new Runnable() {
            @Override
            public void run() {
                mSaveTarget.init(mRenderThread.getEglCore());
                mSaveTarget.makeCurrent();
                // reload texture
                long curTime = 0;
                for (IEClip clip : mClipList) {
                    clip.releaseTexture();
                    clip.loadTexture();
                    curTime = clip.getStartTime();
                    do {
                        clip.render(true, curTime - clip.getStartTime());
                        curTime += 40;
                    } while (curTime <= clip.getEndTime());
                }
                mSaveTarget.release();
                mSaveTarget = null;

                mPreviewTarget.makeCurrent();
                for (IEClip clip : mClipList) {
                    clip.setRenderTarget(mPreviewTarget);
                    clip.releaseImage();
                    clip.loadImage(false);
                    clip.releaseTexture();
                    clip.loadTexture();
                }
            }
        });

        return true;
    }

    public boolean generatorHistogram(final int clipIndex, final IHistogramGenerateListener listener) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Generator histogram failed, clip index invalid: " + clipIndex);
            return false;
        }

        if (listener == null) {
            Logger.e(TAG, "Listener can not be null!");
            return false;
        }

        mRenderThread.post(new Runnable() {
            @Override
            public void run() {
                mClipList.get(clipIndex).generatorHistogram(listener);
            }
        });

        return true;
    }

    public Context getContext() {
        return mAppContext;
    }

    private void renderClip(final int clipIndex) {
        if (clipIndex < 0 || clipIndex > mClipList.size() - 1) {
            Logger.e(TAG, "Generator histogram failed, clip index invalid: " + clipIndex);
            return;
        }

        mRenderThread.post(new Runnable() {
            @Override
            public void run() {
                mClipList.get(clipIndex).render(true, 0);
            }
        });
    }

    private void renderAtPosition(final long position) {
        mRenderThread.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mClipList.size(); i++) {
                    IEClip clip = mClipList.get(i);
                    if (position >= clip.getStartTime() && position < clip.getEndTime()) {
                        clip.render(true, position - clip.getStartTime());
                        break;
                    }
                }
            }
        });
    }

    private static class Holder {
        private static final IEManager sInstance = new IEManager();
    }
}
