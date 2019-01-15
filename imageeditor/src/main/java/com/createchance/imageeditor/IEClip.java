package com.createchance.imageeditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.BaseImageDrawer;
import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.transitions.AbstractTransition;
import com.createchance.imageeditor.utils.Logger;
import com.createchance.imageeditor.utils.OpenGlUtils;
import com.createchance.imageeditor.utils.UiThreadUtil;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Image clip.
 *
 * @author createchance
 * @date 2018/12/24
 */
class IEClip implements RenderContext {

    private static final String TAG = "IEClip";

    private Bitmap mBitmap;

    private int mOriginWidth, mOriginHeight;

    private final String mImageFilePath;

    private long mStartTime;

    private long mEndTime;

    private long mDuration;

    private long mTransitionDuration;

    private int mBaseTextureId = -1;

    private ExifInterface mExifInterface;

    private final List<AbstractOperator> mOpList = new ArrayList<>();
    private final List<AbstractOperator> mRemovedOpList = new ArrayList<>();
    private AbstractTransition mTransition;

    private IRenderTarget mRenderTarget;

    private BaseImageDrawer mDrawer;

    private int mRenderLeft, mRenderTop, mRenderRight, mRenderBottom;
    private float mScissorX, mScissorY, mScissorWidth = 1.0f, mScissorHeight = 1.0f;
    private float mScaleX = 1.0f, mScaleY = 1.0f;
    private float mTranslateX, mTranslateY;

    IEClip(String imagePath, long startTime, long endTime) {
        mImageFilePath = imagePath;
        mStartTime = startTime;
        mEndTime = endTime;
        mDuration = endTime - startTime;
        getOriginSize();
    }

    @Override
    public float getScaleFactor() {
        return (mRenderRight - mRenderLeft) * 1.0f / mOriginWidth;
    }

    @Override
    public int getSurfaceWidth() {
        return mRenderTarget.getSurfaceWidth();
    }

    @Override
    public int getSurfaceHeight() {
        return mRenderTarget.getSurfaceHeight();
    }

    @Override
    public int getRenderWidth() {
        return mRenderRight - mRenderLeft;
    }

    @Override
    public int getRenderHeight() {
        return mRenderTop - mRenderBottom;
    }

    @Override
    public float getNextAspectRatio() {
        float aspectRatio = 1.0f;
        for (int i = 0; i < IEManager.getInstance().getClipList().size(); i++) {
            IEClip clip = IEManager.getInstance().getClip(i);
            if (clip == IEClip.this && i < IEManager.getInstance().getClipList().size() - 1) {
                aspectRatio = IEManager.getInstance().getClip(i + 1).getOriginWidth() * 1.0f /
                        IEManager.getInstance().getClip(i + 1).getOriginHeight();
            }
        }

        return aspectRatio;
    }

    @Override
    public int getRenderLeft() {
        return mRenderLeft;
    }

    @Override
    public int getRenderTop() {
        return mRenderTop;
    }

    @Override
    public int getRenderRight() {
        return mRenderRight;
    }

    @Override
    public int getRenderBottom() {
        return mRenderBottom;
    }

    @Override
    public float getScissorX() {
        return mScissorX;
    }

    @Override
    public float getScissorY() {
        return mScissorY;
    }

    @Override
    public float getScissorWidth() {
        return mScissorWidth;
    }

    @Override
    public float getScissorHeight() {
        return mScissorHeight;
    }

    @Override
    public int getInputTextureId() {
        return mRenderTarget.getInputTextureId();
    }

    @Override
    public int getOutputTextureId() {
        return mRenderTarget.getOutputTextureId();
    }

    @Override
    public int getFromTextureId() {
        // TODO: return origin base texture, no op render result.
        return mBaseTextureId;
    }

    @Override
    public int getToTextureId() {
        int textureId = -1;
        for (int i = 0; i < IEManager.getInstance().getClipList().size(); i++) {
            IEClip clip = IEManager.getInstance().getClip(i);
            if (clip == IEClip.this && i < IEManager.getInstance().getClipList().size() - 1) {
                textureId = IEManager.getInstance().getClip(i + 1).getBaseTextureId();
            }
        }
        // TODO: return origin base texture, no op render result.
        return textureId;
    }

    @Override
    public void bindOffScreenFrameBuffer() {
        mRenderTarget.bindOffScreenFrameBuffer();
    }

    @Override
    public void attachOffScreenTexture(int textureId) {
        mRenderTarget.attachOffScreenTexture(textureId);
    }

    @Override
    public void bindDefaultFrameBuffer() {
        mRenderTarget.bindDefaultFrameBuffer();
    }

    @Override
    public void swapTexture() {
        mRenderTarget.swapTexture();
    }

    int getBaseTextureId() {
        return mBaseTextureId;
    }

    void setRenderTarget(IRenderTarget target) {
        mRenderTarget = target;
    }

    void loadImage(boolean reset) {
        if (mRenderTarget == null ||
                mRenderTarget.getSurfaceWidth() == 0 ||
                mRenderTarget.getSurfaceHeight() == 0) {
            return;
        }
        if (mBitmap == null) {
            Logger.d(TAG, "Clip load image, index: " + IEManager.getInstance().getClipList().indexOf(this));
            mBitmap = loadBitmap(mImageFilePath, mRenderTarget.getSurfaceWidth(), mRenderTarget.getSurfaceHeight());
            Logger.d(TAG, "Loaded image width: " + mBitmap.getWidth() + ", height: " + mBitmap.getHeight());
            adjustSize(reset);
        }
    }

    void loadTexture() {
        if (mBaseTextureId == -1) {
            mBaseTextureId = OpenGlUtils.loadTexture(mBitmap, OpenGlUtils.NO_TEXTURE, false);
        }
    }

    void addOperator(AbstractOperator operator) {
        operator.setRenderContext(this);
        mOpList.add(operator);
        // remove removed op list when add operator.
        mRemovedOpList.clear();
    }

    void updateOperator(AbstractOperator operator) {
    }

    boolean undo() {
        if (mOpList.size() > 0) {
            mRemovedOpList.add(mOpList.get(mOpList.size() - 1));
            mOpList.remove(mOpList.size() - 1);
        } else {
            Logger.e(TAG, "Can not undo.");
        }

        return !mOpList.isEmpty();
    }

    boolean redo() {
        if (mRemovedOpList.size() > 0) {
            mOpList.add(mRemovedOpList.get(mRemovedOpList.size() - 1));
            mRemovedOpList.remove(mRemovedOpList.size() - 1);
        } else {
            Logger.e(TAG, "Can not redo.");
        }

        return !mRemovedOpList.isEmpty();
    }

    void removeOperator(AbstractOperator operator) {
        mOpList.remove(operator);
    }

    void removeOperator(List<AbstractOperator> operatorList) {
        mOpList.removeAll(operatorList);
    }

    void setTransition(AbstractTransition transition, long duration) {
        mTransition = transition;
        mTransitionDuration = duration;
        mTransition.setRenderContext(this);
    }

    void removeTransition() {
        mTransition = null;
    }

    Bitmap getBitmap() {
        return mBitmap;
    }

    String getImageFilePath() {
        return mImageFilePath;
    }

    long getStartTime() {
        return mStartTime;
    }

    long getEndTime() {
        return mEndTime;
    }

    List<AbstractOperator> getOpList() {
        return mOpList;
    }

    long getDuration() {
        return mDuration;
    }

    void setDuration(long duration) {
        this.mDuration = duration;
    }

    void setScissorX(float scissorX) {
        this.mScissorX = scissorX;
    }

    void setScissorY(float scissorY) {
        this.mScissorY = scissorY;
    }

    void setScissorWidth(float scissorWidth) {
        this.mScissorWidth = scissorWidth;
    }

    void setScissorHeight(float scissorHeight) {
        this.mScissorHeight = scissorHeight;
    }

    float getScaleX() {
        return mScaleX;
    }

    void setScaleX(float scaleX) {
        this.mScaleX = scaleX;
        if (mScaleX < 0.1f) {
            mScaleX = 0.1f;
        } else if (mScaleX > 10.0f) {
            mScaleX = 10.0f;
        }
    }

    float getScaleY() {
        return mScaleY;
    }

    void setScaleY(float scaleY) {
        this.mScaleY = scaleY;
        if (mScaleY < 0.1f) {
            mScaleY = 0.1f;
        } else if (mScaleY > 10.0f) {
            mScaleY = 10.0f;
        }
    }

    float getTranslateX() {
        return mTranslateX;
    }

    void setTranslateX(float translateX) {
        this.mTranslateX = translateX;
    }

    float getTranslateY() {
        return mTranslateY;
    }

    void setTranslateY(float translateY) {
        this.mTranslateY = translateY;
    }

    int getOriginWidth() {
        return mOriginWidth;
    }

    int getOriginHeight() {
        return mOriginHeight;
    }

    void generatorHistogram(final IHistogramGenerateListener listener) {
        mRenderTarget.bindOffScreenFrameBuffer();
        mRenderTarget.attachOffScreenTexture(mRenderTarget.getInputTextureId());
        final IntBuffer pixelBuffer = IntBuffer.allocate(getRenderWidth() * getRenderHeight());
        GLES20.glReadPixels(mRenderLeft,
                mRenderBottom,
                getRenderWidth(),
                getRenderHeight(),
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                pixelBuffer);
        final List<HistogramData> data = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) {
            data.add(new HistogramData());
        }

        for (int i = 0; i < pixelBuffer.limit(); i++) {
            int rgbVal = pixelBuffer.get(i);
            int r = rgbVal & 0xFF;
            int g = (rgbVal >> 8) & 0xFF;
            int b = (rgbVal >> 16) & 0xFF;
            data.get(r).mRed++;
            data.get(g).mGreen++;
            data.get(b).mBlue++;
            data.get((r + g + b) / 3).mAll++;
        }
        UiThreadUtil.post(new Runnable() {
            @Override
            public void run() {
                listener.onHistogramGenerated(data, pixelBuffer.limit());
            }
        });
        mRenderTarget.bindDefaultFrameBuffer();
    }

    void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }

    void setEndTime(long endTime) {
        this.mEndTime = endTime;
    }

    /**
     * Render all operators
     */
    void render(boolean swap, long localTime) {
        if (mDrawer == null) {
            mDrawer = new BaseImageDrawer();
        }

        // render base image
        mRenderTarget.bindOffScreenFrameBuffer();
        mRenderTarget.attachOffScreenTexture(mRenderTarget.getInputTextureId());
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        mRenderTarget.attachOffScreenTexture(mRenderTarget.getOutputTextureId());
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // scissor for output
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor((int) (mScissorX * getRenderWidth()) + mRenderLeft,
                (int) (mScissorY * getRenderHeight()) + mRenderBottom,
                (int) (mScissorWidth * getRenderWidth()),
                (int) (mScissorHeight * getRenderHeight()));
        mRenderTarget.attachOffScreenTexture(mRenderTarget.getInputTextureId());
        mDrawer.draw(mBaseTextureId,
                mRenderLeft,
                mRenderBottom,
                getRenderWidth(),
                getRenderHeight(),
                true,
                1.0f,
                1.0f,
                0,
                0);

        mRenderTarget.attachOffScreenTexture(mRenderTarget.getOutputTextureId());
        mDrawer.draw(mBaseTextureId,
                mRenderLeft,
                mRenderBottom,
                getRenderWidth(),
                getRenderHeight(),
                true,
                1.0f,
                1.0f,
                0,
                0);

        for (AbstractOperator operator : mOpList) {
            operator.exec();
        }

        if (mTransition != null && mDuration - localTime <= mTransitionDuration) {
            mTransition.setProgress(1.0f - (mDuration - localTime) * 1.0f / mTransitionDuration);
            mTransition.exec();
        }

        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);

        mRenderTarget.bindDefaultFrameBuffer();

        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mDrawer.draw(mRenderTarget.getInputTextureId(),
                0,
                0,
                mRenderTarget.getSurfaceWidth(),
                mRenderTarget.getSurfaceHeight(),
                false,
                mScaleX,
                mScaleY,
                mTranslateX,
                mTranslateY);

        if (swap) {
            // swap to send image to render target.
            mRenderTarget.swapBuffers();
        }
    }

    private void adjustSize(boolean reset) {
        int imgWidth = mBitmap.getWidth();
        int imgHeight = mBitmap.getHeight();
        float scale = 1.0f;
        if (imgWidth > imgHeight) {
            if (imgWidth > mRenderTarget.getSurfaceWidth()) {
                scale = mRenderTarget.getSurfaceWidth() * 1.0f / imgWidth;
                imgWidth = mRenderTarget.getSurfaceWidth();
                imgHeight = (int) (imgHeight * scale);
            }
        } else if (imgWidth == imgHeight) {
            if (imgWidth > mRenderTarget.getSurfaceWidth()) {
                imgWidth = mRenderTarget.getSurfaceWidth();
                imgHeight = imgWidth;
            }
        } else {
            if (imgHeight > mRenderTarget.getSurfaceHeight()) {
                scale = mRenderTarget.getSurfaceHeight() * 1.0f / imgHeight;
                imgHeight = mRenderTarget.getSurfaceHeight();
                imgWidth = (int) (imgWidth * scale);
            }
        }

        mRenderLeft = (mRenderTarget.getSurfaceWidth() - imgWidth) / 2;
        mRenderTop = (mRenderTarget.getSurfaceHeight() + imgHeight) / 2;
        mRenderRight = (mRenderTarget.getSurfaceWidth() + imgWidth) / 2;
        mRenderBottom = (mRenderTarget.getSurfaceHeight() - imgHeight) / 2;
        if (reset) {
            mScissorX = 0;
            mScissorY = 0;
            mScissorWidth = 1;
            mScissorHeight = 1;
        }
    }

    void releaseImage() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    void releaseTexture() {
        if (mBaseTextureId != -1) {
            GLES20.glDeleteTextures(1, new int[]{mBaseTextureId}, 0);
            mBaseTextureId = -1;
        }
    }

    private Bitmap loadBitmap(String filePath, int width, int height) {
        int degree = 0;
        try {
            mExifInterface = new ExifInterface(mImageFilePath);
            // read orientation info.
            int orientation = mExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // get rotate degree.
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        // preload size
        BitmapFactory.decodeFile(filePath, options);

        // get origin size by rotate degree.
        if (degree == 0 || degree == 180) {
            mOriginWidth = options.outWidth;
            mOriginHeight = options.outHeight;
        } else {
            mOriginWidth = options.outHeight;
            mOriginHeight = options.outWidth;
        }

        // real load size
        options.inJustDecodeBounds = false;

        // get sample size by target size.
        options.inSampleSize = getSampleSize(mOriginWidth, mOriginHeight, width, height);

        Bitmap rawBitmap = BitmapFactory.decodeFile(filePath, options);

        if (degree != 0) {
            // rotate image
            Matrix m = new Matrix();
            m.postRotate(degree);
            rawBitmap = Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.getWidth(),
                    rawBitmap.getHeight(), m, true);
        }

        return rawBitmap;
    }

    private int getSampleSize(int originalWidth, int originalHeight, int width, int height) {
        int sampleSize = 1;

        if (originalWidth >= originalHeight && originalWidth > width) {
            sampleSize = originalWidth / width;
        } else if (originalWidth < originalHeight && originalHeight > height) {
            sampleSize = originalHeight / height;
        }

        if (sampleSize <= 0) {
            sampleSize = 1;
        }

        return sampleSize;
    }

    private void getOriginSize() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        // preload size
        BitmapFactory.decodeFile(mImageFilePath, options);

        mOriginWidth = options.outWidth;
        mOriginHeight = options.outHeight;
    }
}
