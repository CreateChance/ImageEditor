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
public class IEClip implements OperatorContext {

    private static final String TAG = "IEClip";

    private final Bitmap mBitmap;

    private final String mImageFilePath;

    private long mStartTime;

    private long mEndTime;

    private long mDuration;

    private float mWidthScaleFactor = 1.0f, mHeightScaleFactor = 1.0f;

    private int mBaseTextureId;

    private final List<AbstractOperator> mOpList = new ArrayList<>();

    private IRenderTarget mRenderTarget;

    private GLRenderThread mRenderThread;

    private BaseImageDrawer mDrawer;

    private int mRenderLeft, mRenderTop, mRenderRight, mRenderBottom;
    private int mScissorX, mScissorY, mScissorWidth, mScissorHeight;

    IEClip(String imagePath, long startTime, long endTime, GLRenderThread renderThread) {
        mBitmap = BitmapFactory.decodeFile(imagePath);
        mImageFilePath = imagePath;
        mStartTime = startTime;
        mEndTime = endTime;
        mDuration = endTime - startTime;
        mRenderThread = renderThread;
    }

    public void setRenderTarget(IRenderTarget target) {
        mRenderTarget = target;
    }

    public void addOperator(AbstractOperator operator) {
        operator.setOperatorContext(this);
        mOpList.add(operator);

        render();
    }

    public void undo() {

    }

    public void redo() {

    }

    public void removeOperator(AbstractOperator operator) {
        mOpList.remove(operator);
        render();
    }

    public void removeOperator(List<AbstractOperator> operatorList) {
        mOpList.removeAll(operatorList);
        render();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getImageFilePath() {
        return mImageFilePath;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public List<AbstractOperator> getOpList() {
        return mOpList;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public void setScissorX(int scissorX) {
        this.mScissorX = scissorX;
    }

    public void setScissorY(int scissorY) {
        this.mScissorY = scissorY;
    }

    public void setScissorWidth(int scissorWidth) {
        this.mScissorWidth = scissorWidth;
    }

    public void setScissorHeight(int scissorHeight) {
        this.mScissorHeight = scissorHeight;
    }

    public void generatorHistogram(final IHistogramGenerateListener listener) {
        mRenderThread.post(new Runnable() {
            @Override
            public void run() {
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
                    int r = rgbVal & 0xF;
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
        });
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

    void release() {
        mBitmap.recycle();
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
    void render() {
        mRenderThread.post(new Runnable() {
            @Override
            public void run() {
                // render base image
                mRenderTarget.bindOffScreenFrameBuffer();
                mRenderTarget.attachOffScreenTexture(mRenderTarget.getInputTextureId());
                if (mDrawer == null) {
                    mDrawer = new BaseImageDrawer(mWidthScaleFactor, mHeightScaleFactor);
                    mBaseTextureId = OpenGlUtils.loadTexture(mBitmap, OpenGlUtils.NO_TEXTURE, false);
                }

                GLES20.glClearColor(0, 0, 0, 0);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
                mDrawer.draw(mBaseTextureId,
                        mRenderLeft,
                        mRenderBottom,
                        getRenderWidth(),
                        getRenderHeight(),
                        true);

                for (AbstractOperator operator : mOpList) {
                    operator.exec();
                }

                mRenderTarget.bindDefaultFrameBuffer();

                GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
                GLES20.glScissor(mScissorX,
                        mScissorY,
                        mScissorWidth,
                        mScissorHeight);
                mDrawer.draw(mRenderTarget.getInputTextureId(),
                        0,
                        0,
                        mRenderTarget.getSurfaceWidth(),
                        mRenderTarget.getSurfaceHeight(),
                        false);
                GLES20.glDisable(GLES20.GL_SCISSOR_TEST);

                // swap to show it.
                mRenderTarget.swapBuffers();
            }
        });
    }

    void adjustSize() {
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
        mScissorX = mRenderLeft;
        mScissorY = mRenderBottom;
        mScissorWidth = mRenderRight - mRenderLeft;
        mScissorHeight = mRenderTop - mRenderBottom;
    }
}
