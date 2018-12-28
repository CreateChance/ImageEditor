package com.createchance.imageeditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.createchance.imageeditor.drawers.BaseImageDrawer;
import com.createchance.imageeditor.ops.AbstractOperator;
import com.createchance.imageeditor.utils.OpenGlUtils;
import com.createchance.imageeditor.utils.UiThreadUtil;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Image clip.
 *
 * @author createchance
 * @date 2018/12/24
 */
class IEClip implements OperatorContext {

    private static final String TAG = "IEClip";

    private final Bitmap mBitmap;

    private final String mImageFilePath;

    private long mStartTime;

    private long mEndTime;

    private long mDuration;

    private int mBaseTextureId = -1;

    private final List<AbstractOperator> mOpList = new ArrayList<>();

    private IRenderTarget mRenderTarget;

    private BaseImageDrawer mDrawer;

    private int mRenderLeft, mRenderTop, mRenderRight, mRenderBottom;
    private int mScissorX, mScissorY, mScissorWidth, mScissorHeight;
    private float mScaleX = 1.0f, mScaleY = 1.0f;
    private float mTranslateX, mTranslateY;

    IEClip(String imagePath, long startTime, long endTime) {
        mBitmap = BitmapFactory.decodeFile(imagePath);
        mImageFilePath = imagePath;
        mStartTime = startTime;
        mEndTime = endTime;
        mDuration = endTime - startTime;
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
    public int getScissorX() {
        return mScissorX;
    }

    @Override
    public int getScissorY() {
        return mScissorY;
    }

    @Override
    public int getScissorWidth() {
        return mScissorWidth;
    }

    @Override
    public int getScissorHeight() {
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

    void init() {
        if (mDrawer == null) {
            mDrawer = new BaseImageDrawer();
        }
        if (mBaseTextureId == -1) {
            mBaseTextureId = OpenGlUtils.loadTexture(mBitmap, OpenGlUtils.NO_TEXTURE, false);
        }
    }

    void setRenderTarget(IRenderTarget target) {
        mRenderTarget = target;
    }

    void addOperator(AbstractOperator operator) {
        operator.setOperatorContext(this);
        mOpList.add(operator);
    }

    void updateOperator(AbstractOperator operator) {
    }

    void undo() {
    }

    void redo() {
    }

    void removeOperator(AbstractOperator operator) {
        mOpList.remove(operator);
    }

    void removeOperator(List<AbstractOperator> operatorList) {
        mOpList.removeAll(operatorList);
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

    void setScissorX(int scissorX) {
        this.mScissorX = scissorX;
    }

    void setScissorY(int scissorY) {
        this.mScissorY = scissorY;
    }

    void setScissorWidth(int scissorWidth) {
        this.mScissorWidth = scissorWidth;
    }

    void setScissorHeight(int scissorHeight) {
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
        return mBitmap.getWidth();
    }

    int getOriginHeight() {
        return mBitmap.getHeight();
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
    void render(boolean swap) {
        // render base image
        mRenderTarget.bindOffScreenFrameBuffer();
        mRenderTarget.attachOffScreenTexture(mRenderTarget.getInputTextureId());

        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // scissor for output
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(mScissorX,
                mScissorY,
                mScissorWidth,
                mScissorHeight);
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

    void adjustSize() {
        int imgWidth = mBitmap.getWidth();
        int imgHeight = mBitmap.getHeight();
        float scale = 1.0f;
        if (imgWidth > imgHeight) {
            scale = mRenderTarget.getSurfaceWidth() * 1.0f / imgWidth;
            imgWidth = mRenderTarget.getSurfaceWidth();
            imgHeight = (int) (imgHeight * scale);
        } else if (imgWidth == imgHeight) {
            imgWidth = mRenderTarget.getSurfaceWidth();
            imgHeight = imgWidth;
        } else {
            scale = mRenderTarget.getSurfaceHeight() * 1.0f / imgHeight;
            imgHeight = mRenderTarget.getSurfaceHeight();
            imgWidth = (int) (imgWidth * scale);
        }

        mRenderLeft = (mRenderTarget.getSurfaceWidth() - imgWidth) / 2;
        mRenderTop = (mRenderTarget.getSurfaceHeight() + imgHeight) / 2;
        mRenderRight = (mRenderTarget.getSurfaceWidth() + imgWidth) / 2;
        mRenderBottom = (mRenderTarget.getSurfaceHeight() - imgHeight) / 2;
        mScissorX = mRenderLeft;
        mScissorY = mRenderBottom;
        mScissorWidth = mRenderRight - mRenderLeft;
        mScissorHeight = mRenderTop - mRenderBottom;
    }

    void release() {
        mBitmap.recycle();
    }
}
