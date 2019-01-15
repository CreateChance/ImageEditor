package com.createchance.imageeditor;

/**
 * Operator info context.
 *
 * @author createchance
 * @date 2018/12/24
 */
public interface RenderContext {

    /**
     * Get scale factor of preview and saver.
     *
     * @return scale factor.
     */
    float getScaleFactor();

    int getSurfaceWidth();

    int getSurfaceHeight();

    int getRenderWidth();

    int getRenderHeight();

    float getNextAspectRatio();

    int getRenderLeft();

    int getRenderTop();

    int getRenderRight();

    int getRenderBottom();

    float getScissorX();

    float getScissorY();

    float getScissorWidth();

    float getScissorHeight();

    int getInputTextureId();

    int getOutputTextureId();

    int getFromTextureId();

    int getToTextureId();

    void bindOffScreenFrameBuffer();

    void attachOffScreenTexture(int textureId);

    void bindDefaultFrameBuffer();

    void swapTexture();
}
