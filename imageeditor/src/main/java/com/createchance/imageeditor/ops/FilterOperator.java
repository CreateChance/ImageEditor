package com.createchance.imageeditor.ops;

import android.opengl.GLES20;

import com.createchance.imageeditor.filters.GPUImageFilter;
import com.createchance.imageeditor.filters.GPUImageFilterGroup;
import com.createchance.imageeditor.filters.Rotation;
import com.createchance.imageeditor.filters.util.TextureRotationUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Filter operator.
 *
 * @author createchance
 * @date 2018/10/28
 */
public class FilterOperator extends AbstractOperator {

    private static final String TAG = "FilterOperator";

    private GPUImageFilter mFilter;

    private final float CUBE[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
    };
    private FloatBuffer mGLCubeBuffer;
    private FloatBuffer mGLTextureBuffer;

    private int mInputTextureId, mOutputTextureId;
    private int mFrameBufferId;

    private FilterOperator() {
        super(FilterOperator.class.getSimpleName(), OP_FILTER);
    }

    public void setInputTexture(int texture) {
        mInputTextureId = texture;
    }

    public void setOutputTexture(int texture) {
        mOutputTextureId = texture;
    }

    public void setFrameBuffer(int frameBuffer) {
        mFrameBufferId = frameBuffer;
    }

    public void setFilter(GPUImageFilter filter) {
        this.mFilter = filter;
    }

    public GPUImageFilter getFilter() {
        return mFilter;
    }

    @Override
    public boolean checkRational() {
        return true;
    }

    @Override
    public void exec() {
        if (mFilter == null) {
            return;
        }

        mGLCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLCubeBuffer.put(CUBE).position(0);

        mGLTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        float[] textureCords = TextureRotationUtil.getRotation(Rotation.ROTATION_270, false, true);
        mGLTextureBuffer.put(textureCords).position(0);

        mFilter.init();
        GLES20.glUseProgram(mFilter.getProgram());
        mFilter.onOutputSizeChanged(mWorker.getImgShowWidth(), mWorker.getImgShowHeight());
        if (mFilter instanceof GPUImageFilterGroup) {
            ((GPUImageFilterGroup) mFilter).setOutput(mFrameBufferId, mOutputTextureId);
        }

        GLES20.glViewport(0, 0, mWorker.getSurfaceWidth(), mWorker.getSurfaceHeight());
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(mWorker.getImgShowLeft(),
                mWorker.getImgShowBottom(),
                mWorker.getImgShowWidth(),
                mWorker.getImgShowHeight());
        mFilter.onDraw(mInputTextureId, mGLCubeBuffer, mGLTextureBuffer);
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
    }

    public static class Builder {
        private FilterOperator operator = new FilterOperator();

        public Builder filter(GPUImageFilter filter) {
            operator.mFilter = filter;

            return this;
        }

        public FilterOperator build() {
            return operator;
        }
    }
}
