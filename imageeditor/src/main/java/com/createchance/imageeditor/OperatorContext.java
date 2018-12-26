package com.createchance.imageeditor;

/**
 * Operator info context.
 *
 * @author createchance
 * @date 2018/12/24
 */
public interface OperatorContext {
    int getSurfaceWidth();

    int getSurfaceHeight();

    int getRenderWidth();

    int getRenderHeight();

    int getRenderLeft();

    int getRenderTop();

    int getRenderRight();

    int getRenderBottom();

    int getScissorX();

    int getScissorY();

    int getScissorWidth();

    int getScissorHeight();

    int getInputTextureId();

    int getOutputTextureId();

    void bindOffScreenFrameBuffer();

    void attachOffScreenTexture(int textureId);

    void bindDefaultFrameBuffer();

    void swapTexture();
}
