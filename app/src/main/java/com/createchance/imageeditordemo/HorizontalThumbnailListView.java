package com.createchance.imageeditordemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.createchance.imageeditor.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Thumbnail list view.
 *
 * @author createchance
 * @date 2018/12/6
 */
public class HorizontalThumbnailListView extends FrameLayout {

    private static final String TAG = "ThumbnailListView";

    private final int DEFAULT_IMAGE_WIDTH = 48, DEFAULT_IMAGE_HEIGHT = 48;

    // 绘制相关
    private Paint mPaint;
    private Rect mImageDstRect;

    private int mWidth, mHeight;

    private int mPaddingColor = Color.TRANSPARENT;
    private int mPaddingStartWidth, mPaddingEndWidth, mPaddingVerticalHeight;
    private int mGroupPaddingWidth;
    private Drawable mSelectedGroupBg;
    private int mImageWidth, mImageHeight;

    // listeners
    private ImageGroupListener mImageGroupListener;

    private final List<ImageGroup> mImageGroupList = new ArrayList<>();
    private ImageGroup mCurImageGroup;

    private boolean mHoldScroll;
    private boolean mIsLeft;
    private boolean mFromLeftToRight;
    private boolean mIsScrollFromUser;

    private float mLastX;

    public HorizontalThumbnailListView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public HorizontalThumbnailListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalThumbnailListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public HorizontalThumbnailListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            width = mPaddingStartWidth + mPaddingEndWidth + getTotalGroupWidth();
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mImageHeight + mPaddingVerticalHeight * 2;
        }

        Log.d(TAG, "onMeasure, width: " + width + ", height: " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.d(TAG, "onSizeChanged, width: " + w + ", height: " + h);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Logger.d(TAG, "onDraw, padding start: " + mPaddingStartWidth +
                ", padding end: " + mPaddingEndWidth +
                ", group padding: " + mGroupPaddingWidth +
                ", vertical padding: " + mPaddingVerticalHeight +
                ", image width: " + mImageWidth +
                " image width: " + mImageHeight);

        int curPos = 0;
        mPaint.setColor(mPaddingColor);
        canvas.drawRect(curPos, 0, mPaddingStartWidth, mHeight, mPaint);
        curPos += mPaddingStartWidth;

        curPos += mGroupPaddingWidth;
        for (ImageGroup imageGroup : mImageGroupList) {
            Logger.d(TAG, "onDraw: " + imageGroup);
            if (imageGroup.isHidden) {
                Logger.d(TAG, "Image group hidden, so skip this!");
                continue;
            }
            imageGroup.draw(canvas, curPos);
            curPos += imageGroup.getWidth() + mGroupPaddingWidth;
        }

        mPaint.setColor(mPaddingColor);
        canvas.drawRect(curPos,
                0,
                curPos + mPaddingEndWidth,
                mHeight,
                mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getRawX();
                mIsScrollFromUser = true;
                Logger.d(TAG, "Down pos x: " + mLastX +
                        ", left bound: [" + (mCurImageGroup.measuredLeft - mGroupPaddingWidth - getScrollX()) +
                        ", " + (mCurImageGroup.measuredLeft + mGroupPaddingWidth - getScrollX()) +
                        "], right bound: [" + (mCurImageGroup.measuredRight - mGroupPaddingWidth - getScrollX()) +
                        ", " + (mCurImageGroup.measuredRight + mGroupPaddingWidth - getScrollX()) + "].");
                if (mLastX >= (mCurImageGroup.measuredLeft - mGroupPaddingWidth - getScrollX()) &&
                        mLastX <= (mCurImageGroup.measuredLeft + mGroupPaddingWidth - getScrollX())) {
                    mHoldScroll = true;
                    mIsLeft = true;
                } else if (mLastX >= (mCurImageGroup.measuredRight - mGroupPaddingWidth - getScrollX()) &&
                        mLastX <= (mCurImageGroup.measuredRight + mGroupPaddingWidth - getScrollX())) {
                    mHoldScroll = true;
                    mIsLeft = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mHoldScroll) {
                    int delta = (int) (event.getRawX() - mLastX);
                    Logger.d(TAG, "Adjust delta: " + delta + ", is left: " + mIsLeft);
                    // adjust view width.
                    if (mIsLeft) {
                        if (delta > 0) {
                            mFromLeftToRight = true;
                            mCurImageGroup.shrinkLeft(delta);
                        } else if (delta < 0) {
                            mFromLeftToRight = false;
                            mCurImageGroup.expandLeft(-delta);
                        }
                    } else {
                        if (delta > 0) {
                            mFromLeftToRight = true;
                            mCurImageGroup.expandRight(delta);
                        } else if (delta < 0) {
                            mFromLeftToRight = false;
                            mCurImageGroup.shrinkRight(-delta);
                        }
                    }
                    invalidate();
                } else {
                    int scrollX = (int) (getScrollX() + (mLastX - event.getRawX()));
                    if (scrollX < 0) {
                        scrollX = 0;
                    } else if (scrollX > getTotalGroupWidth()) {
                        scrollX = getTotalGroupWidth();
                    }
                    scrollTo(scrollX, getScrollY());
                }
                mLastX = event.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起的时候重新设置padding start
                mIsScrollFromUser = false;
                if (mHoldScroll) {
                    mHoldScroll = false;

                    int curIndex = 0;
                    for (int i = 0; i < mImageGroupList.size(); i++) {
                        ImageGroup imageGroup = mImageGroupList.get(i);
                        if (imageGroup == mCurImageGroup) {
                            break;
                        }
                        if (!mImageGroupList.get(i).isHidden) {
                            curIndex++;
                        }
                    }
                    if (mIsLeft) {
                        if (mFromLeftToRight) {
                            if (mImageGroupListener != null) {
                                mImageGroupListener.onImageGroupLeftShrink(curIndex,
                                        mCurImageGroup.curLeftPos * 1.0f / getGroupContentMaxWidth(),
                                        true);
                            }
                        } else {
                            if (mImageGroupListener != null) {
                                mImageGroupListener.onImageGroupLeftExpand(curIndex,
                                        mCurImageGroup.curLeftPos * 1.0f / getGroupContentMaxWidth(),
                                        true);
                            }
                        }
                    } else {
                        if (mFromLeftToRight) {
                            if (mImageGroupListener != null) {
                                mImageGroupListener.onImageGroupRightExpand(curIndex,
                                        mCurImageGroup.curRightPos * 1.0f / getGroupContentMaxWidth(),
                                        true);
                            }
                        } else {
                            if (mImageGroupListener != null) {
                                mImageGroupListener.onImageGroupRightShrink(curIndex,
                                        mCurImageGroup.curRightPos * 1.0f / getGroupContentMaxWidth(),
                                        true);
                            }
                        }
                    }
                    mPaddingStartWidth = getDisplay().getWidth() / 2 - mGroupPaddingWidth;
                    requestLayout();
                }
                break;
            default:
                break;
        }

        // 我们需要消费事件
        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        Log.d(TAG, "onScrollChanged: " + l);

        select(l);

        mCurImageGroup.notifyPosX(l, oldl);
    }

    public void clear() {
        mCurImageGroup = null;
        mImageGroupList.clear();
    }

    public void newImageGroup(List<ImageItem> imageItemList) {
        if (imageItemList == null || imageItemList.size() == 0) {
            Logger.e(TAG, "Image item list can not be null or empty!");
            return;
        }

        if (mImageGroupList.size() == 0) {
            mImageGroupList.add(new ImageGroup(0, imageItemList));
            // 默认第一个是选中的
            mCurImageGroup = mImageGroupList.get(0);
        } else {
            int leftBound = mImageGroupList.get(mImageGroupList.size() - 1).RIGHT_BOUND;
            mImageGroupList.add(new ImageGroup(leftBound, imageItemList));
        }
    }

    public void setImageGroupListener(ImageGroupListener listener) {
        mImageGroupListener = listener;
    }

    /**
     * 给定一个X轴位置，根据这个位置判断一下这个位置选中了那个image group
     *
     * @param posX 给定的位置，这个位置有效范围：[0, mWidth - mPaddingStartWidth - mPaddingEndWidth - mGroupPaddingWidth * 2]
     */
    private void select(int posX) {
        if (posX < 0 ||
                posX > (mWidth - mPaddingStartWidth - mPaddingEndWidth - mGroupPaddingWidth * 2)) {
            Logger.e(TAG, "Position X is out of range, should between: [" + (mPaddingStartWidth + mGroupPaddingWidth) +
                    ", " + (mWidth - mPaddingEndWidth - mGroupPaddingWidth) + "]" + ", Current position: " + posX);
            return;
        }

        int curPos = mPaddingStartWidth + mGroupPaddingWidth;
        posX += curPos;
        for (ImageGroup imageGroup : mImageGroupList) {
            if (imageGroup.isHidden) {
                continue;
            }
            if (posX >= curPos && posX <= (curPos + imageGroup.getWidth())) {
                if (mCurImageGroup != imageGroup && mGroupPaddingWidth == 0) {
                    mCurImageGroup.notifyPosX(posX - mPaddingStartWidth + mGroupPaddingWidth,
                            posX - mPaddingStartWidth + mGroupPaddingWidth);
                }
                if (mCurImageGroup != null) {
                    mCurImageGroup.isSelected = false;
                }
                imageGroup.isSelected = true;
                mCurImageGroup = imageGroup;
            } else {
                imageGroup.isSelected = false;
            }
            curPos += imageGroup.getWidth() + mGroupPaddingWidth;
        }

        invalidate();
    }

    /**
     * 分割当前选中的ImageGroup, 分割之后产生一个新的ImageGroup，旧的ImageGroup在原来的位置上，新的ImageGroup在下一个位置上
     *
     * @param posX 在这个位置上进行分割，这个位置有效范围：(0, mWidth - mPaddingStartWidth - mPaddingEndWidth - mGroupPaddingWidth * 2)
     */
    public void splitImageGroup(int posX) {
        if (mCurImageGroup == null) {
            Logger.e(TAG, "Current no image group selected!");
            return;
        }

        if (posX <= 0 || posX >= getTotalGroupWidth()) {
            Logger.e(TAG, "Position X is out of range, should between: (" + 0 +
                    ", " + getTotalGroupWidth() + ")" + ", Current position: " + posX);
            return;
        }

        posX += mPaddingStartWidth + mGroupPaddingWidth;
        Logger.d(TAG, "Split image group, position: " + posX);

        int curIndex = mImageGroupList.indexOf(mCurImageGroup);
        List<ImageItem> oldImageItemList = new ArrayList<>();
        List<ImageItem> newImageItemList = new ArrayList<>();
        int curStartPos = mPaddingStartWidth + mGroupPaddingWidth;
        for (int i = 0; i < curIndex; i++) {
            if (!mImageGroupList.get(i).isHidden) {
                curStartPos += mImageGroupList.get(i).getWidth() + mGroupPaddingWidth;
            }
        }

        int posXDelta = posX - curStartPos;
        Logger.d(TAG, "Split position x delta: " + posXDelta);
        int imageItemPos = 0;
        for (ImageItem imageItem : mCurImageGroup.imageItemList) {
            if (posXDelta >= (imageItemPos + imageItem.getWidth())) {
                oldImageItemList.add(imageItem);
            } else if (posXDelta <= imageItemPos) {
                newImageItemList.add(imageItem);
            } else if (posXDelta > imageItemPos && posXDelta < (imageItemPos + imageItem.getWidth())) {
                ImageItem oldItem = new ImageItem(imageItem.image,
                        mImageWidth,
                        imageItem.leftBound,
                        imageItem.leftBound + posXDelta - imageItemPos);
                ImageItem newItem = new ImageItem(imageItem.image,
                        mImageWidth,
                        posXDelta - imageItemPos,
                        imageItem.rightBound);
                oldImageItemList.add(oldItem);
                newImageItemList.add(newItem);
            }

            imageItemPos += imageItem.getWidth();
        }

        // make new image group
        ImageGroup oldImageGroup = new ImageGroup(mCurImageGroup.LEFT_BOUND, oldImageItemList);
        ImageGroup newImageGroup = new ImageGroup(oldImageGroup.RIGHT_BOUND, newImageItemList);
//        newImageGroup.measuredLeft += mGroupPaddingWidth;
//        newImageGroup.measuredRight += mGroupPaddingWidth;

        mImageGroupList.remove(curIndex);
        mImageGroupList.add(curIndex, oldImageGroup);
        mImageGroupList.add(curIndex + 1, newImageGroup);
        oldImageGroup.isSelected = true;
        mCurImageGroup = oldImageGroup;

        if (mImageGroupListener != null) {
            mImageGroupListener.onImageGroupSplit(curIndex, oldImageGroup.curRightPos * 1.0f / getGroupContentMaxWidth());
        }

        // for debug use
        for (ImageGroup imageGroup : mImageGroupList) {
            Logger.d(TAG, "After spilt image group, image group: " + imageGroup);
        }

        requestLayout();
    }

    /**
     * 隐藏当前选中的image group
     */
    public void hiddenImageGroup(int index) {
        if (index < 0 || index > mImageGroupList.size() - 1) {
            Logger.e(TAG, "Index invalid! index: " + index);
            return;
        }

        mImageGroupList.get(index).isHidden = true;
        mImageGroupList.get(index).isSelected = false;
        int curIndex = mImageGroupList.indexOf(mCurImageGroup);
        if (index == curIndex) {
            // 如果是当前的image group被隐藏了，那么需要更新image group
            // 更新逻辑如下：
            // 1. 如果curindex不是最后一个，那就将当前image group变更为下一个
            // 2. 如果是最后一个，那就将当前的image group变更为下前一个
            if (curIndex != mImageGroupList.size() - 1) {
                curIndex++;
            } else {
                curIndex--;
            }
            mCurImageGroup = mImageGroupList.get(curIndex);
            mCurImageGroup.isSelected = true;
        }

        int validIndex = 0;
        for (int i = 0; i < mImageGroupList.size(); i++) {
            if (i == index) {
                break;
            }
            if (!mImageGroupList.get(i).isHidden) {
                validIndex++;
            }
        }
        if (mImageGroupListener != null) {
            mImageGroupListener.onImageGroupHidden(validIndex);
        }

        requestLayout();
    }

    /**
     * 重新展示被隐藏的image group
     *
     * @param index 需要重新展示的index
     */
    public void showHiddenImageGroup(int index) {
        if (index < 0 || index > mImageGroupList.size() - 1) {
            Logger.e(TAG, "Index invalid! index: " + index);
            return;
        }

        ImageGroup targetGroup = mImageGroupList.get(index);
        targetGroup.isHidden = false;

        requestLayout();
    }

    public void setStartPaddingWidth(int startPaddingWidth) {
        mPaddingStartWidth = startPaddingWidth;
    }

    public void setEndPaddingWidth(int endPaddingWidth) {
        mPaddingEndWidth = endPaddingWidth;
    }

    public void setGroupPaddingWidth(int width) {
        mGroupPaddingWidth = width;
    }

    public void setImageWidth(int mImageWidth) {
        this.mImageWidth = mImageWidth;
    }

    public void setImageHeight(int mImageHeight) {
        this.mImageHeight = mImageHeight;
    }

    public void setPaddingVerticalHeight(int mPaddingVerticalHeight) {
        this.mPaddingVerticalHeight = mPaddingVerticalHeight;
    }

    public void setSelectedGroupBg(Drawable mSelectedGroupBg) {
        this.mSelectedGroupBg = mSelectedGroupBg;
    }

    public void setPaddingColor(int mPaddingColor) {
        this.mPaddingColor = mPaddingColor;
    }

    public int getGroupPaddingWidth() {
        return mGroupPaddingWidth;
    }

    public int getPaddingStartWidth() {
        return mPaddingStartWidth;
    }

    public int getPaddingEndWidth() {
        return mPaddingEndWidth;
    }

    public int getGroupContentWidth() {
        int width = 0;
        for (ImageGroup imageGroup : mImageGroupList) {
            if (imageGroup.isHidden) {
                continue;
            }
            width += imageGroup.getWidth();
        }

        return width;
    }

    public int getGroupContentMaxWidth() {
        int width = 0;
        for (ImageGroup imageGroup : mImageGroupList) {
            width += imageGroup.getMaxWidth();
        }

        return width;
    }

    public int getCurImageGroupIndex() {
        return mImageGroupList.indexOf(mCurImageGroup);
    }

    /**
     * 获得除去start和end padding部分的宽度
     *
     * @return 除去start和end padding部分的宽度
     */
    private int getTotalGroupWidth() {
        int width = getGroupContentWidth();
        int validSize = 0;
        for (ImageGroup imageGroup : mImageGroupList) {
            if (!imageGroup.isHidden) {
                validSize++;
            }
        }
        if (validSize > 0) {
            width += (validSize - 1) * mGroupPaddingWidth;
        }
        return width;
    }

    public List<ImageGroup> getImageGroupList() {
        return mImageGroupList;
    }

    public ImageGroup getCurImageGroup() {
        return mCurImageGroup;
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.setWillNotDraw(false);

        // init params
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.HorizontalThumbnailListView);
        mPaddingStartWidth = (int) typedArray.getDimension(R.styleable.HorizontalThumbnailListView_paddingStartWidth, 0);
        mPaddingEndWidth = (int) typedArray.getDimension(R.styleable.HorizontalThumbnailListView_paddingEndWidth, 0);
        mPaddingVerticalHeight = (int) typedArray.getDimension(R.styleable.HorizontalThumbnailListView_paddingVerticalHeight, 0);
        mGroupPaddingWidth = (int) typedArray.getDimension(R.styleable.HorizontalThumbnailListView_groupPaddingWidth, 0);
        mSelectedGroupBg = typedArray.getDrawable(R.styleable.HorizontalThumbnailListView_selectedGroupBg);
        mImageWidth = (int) typedArray.getDimension(R.styleable.HorizontalThumbnailListView_imageWidth, DEFAULT_IMAGE_WIDTH);
        mImageHeight = (int) typedArray.getDimension(R.styleable.HorizontalThumbnailListView_imageHeight, DEFAULT_IMAGE_HEIGHT);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mImageDstRect = new Rect();
    }

    public static class ImageItem {
        private Bitmap image;

        // 图片绘制的矩形区域，保存的位置都是相对位置
        private Rect srcRect;

        private final int leftBound, rightBound;

        public ImageItem(Bitmap image, int imageSize, int leftBound, int rightBound) {
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();
            float widthScale = imageSize * 1.0f / originWidth;
            float heightScale = imageSize * 1.0f / originHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(widthScale, heightScale);
            this.image = Bitmap.createBitmap(image, 0, 0, originWidth, originHeight, matrix, true);
            this.leftBound = leftBound;
            this.rightBound = rightBound;
            this.srcRect = new Rect(leftBound, 0, rightBound, this.image.getHeight());
        }

        public int getLeft() {
            return srcRect.left;
        }

        public int getRight() {
            return srcRect.right;
        }

        public int getWidth() {
            return srcRect.right - srcRect.left;
        }

        public int getMaxWidth() {
            return rightBound - leftBound;
        }

        @Override
        public String toString() {
            return "ImageItem{" +
                    "srcRect=" + srcRect +
                    ", leftBound=" + leftBound +
                    ", rightBound=" + rightBound +
                    '}';
        }
    }

    public class ImageGroup {
        // 图片分组，将一系列的ImageItem组成一个组
        private final List<ImageItem> imageItemList = new ArrayList<>();

        private boolean isSelected;

        private boolean isHidden;

        // 绝对位置值
        private final int LEFT_BOUND, RIGHT_BOUND;
        private int curLeftPos, curRightPos;
        // 测量位置值
        private int measuredLeft, measuredRight;

        /**
         * 构建image group
         *
         * @param leftBound     左边边界值，绝对位置
         * @param imageItemList image item列表
         */
        ImageGroup(int leftBound, List<ImageItem> imageItemList) {
            this.imageItemList.addAll(imageItemList);
            LEFT_BOUND = leftBound;
            curLeftPos = leftBound;
            RIGHT_BOUND = leftBound + getWidth();
            curRightPos = RIGHT_BOUND;
            measuredLeft = curLeftPos;
            measuredRight = curRightPos;
        }

        void draw(Canvas canvas, int basePos) {
            if (isSelected && mSelectedGroupBg != null) {
                mSelectedGroupBg.setBounds(basePos - mGroupPaddingWidth, 0, basePos + getWidth() + mGroupPaddingWidth, mHeight);
                mSelectedGroupBg.draw(canvas);
            }

            measuredLeft = basePos;
            int curPos = basePos;
            for (ImageItem imageItem : imageItemList) {
                mImageDstRect.set(
                        curPos,
                        mPaddingVerticalHeight,
                        curPos + imageItem.getWidth(),
                        mImageHeight + mPaddingVerticalHeight
                );
                Logger.d(TAG, "Draw image item at: " + mImageDstRect);
                canvas.drawBitmap(imageItem.image, imageItem.srcRect, mImageDstRect, null);
                curPos += imageItem.getWidth();
            }
            measuredRight = curPos;

            Logger.d(TAG, "Image group draw, index: " + mImageGroupList.indexOf(this) + ", group: " + this);
        }

        void shrinkLeft(int size) {
            if (curLeftPos == curRightPos) {
                Logger.e(TAG, "Current image group reach min size, can not adjust!");
                return;
            }

            int remain = size;
            if (remain > (curRightPos - curLeftPos)) {
                remain = curRightPos - curLeftPos;
                size = remain;
            }

            int leftIndex = mCurImageGroup.getLeftIndex();
            int rightIndex = mCurImageGroup.getRightIndex();
            for (int i = leftIndex; i <= rightIndex; i++) {
                ImageItem imageItem = mCurImageGroup.imageItemList.get(i);
                if (imageItem.getWidth() >= remain) {
                    imageItem.srcRect.left += remain;
                    mPaddingStartWidth += remain;
                    remain = 0;
                } else {
                    remain -= imageItem.getWidth();
                    mPaddingStartWidth += imageItem.getWidth();
                    imageItem.srcRect.left = imageItem.srcRect.right;
                }

                if (remain == 0) {
                    break;
                }
            }
            mCurImageGroup.curLeftPos += size;

            Logger.d(TAG, "Adjust image group, shrink left, left progress: " + getCurLeftPos() + ", right process: " + getCurRightPos());
            int index = 0;
            for (int i = 0; i < mImageGroupList.size(); i++) {
                ImageGroup imageGroup = mImageGroupList.get(i);
                if (imageGroup == this) {
                    break;
                }
                if (!mImageGroupList.get(i).isHidden) {
                    index++;
                }
            }
            if (mImageGroupListener != null) {
                mImageGroupListener.onImageGroupLeftShrink(index,
                        curLeftPos * 1.0f / getGroupContentMaxWidth(), false);
            }
        }

        void expandLeft(int size) {
            if (curLeftPos == LEFT_BOUND) {
                Logger.e(TAG, "Current image group reach left bound, can not adjust.");
                return;
            }

            int remain = size;
            if (remain > (curLeftPos - LEFT_BOUND)) {
                remain = (curLeftPos - LEFT_BOUND);
                size = remain;
            }

            int leftIndex = mCurImageGroup.getLeftIndex();
            for (int i = leftIndex; i >= 0; i--) {
                ImageItem imageItem = mCurImageGroup.imageItemList.get(i);
                if ((imageItem.srcRect.left - imageItem.leftBound) >= remain) {
                    imageItem.srcRect.left -= remain;
                    mPaddingStartWidth -= remain;
                    remain = 0;
                } else {
                    remain -= (imageItem.srcRect.left - imageItem.leftBound);
                    mPaddingStartWidth -= (imageItem.srcRect.left - imageItem.leftBound);
                    imageItem.srcRect.left = imageItem.leftBound;
                }

                if (remain == 0) {
                    break;
                }
            }
            curLeftPos -= size;

            Logger.d(TAG, "Adjust image group, expand left, progress: " + curLeftPos * 1.0f / getGroupContentMaxWidth());
            int index = 0;
            for (int i = 0; i < mImageGroupList.size(); i++) {
                ImageGroup imageGroup = mImageGroupList.get(i);
                if (imageGroup == this) {
                    break;
                }
                if (!mImageGroupList.get(i).isHidden) {
                    index++;
                }
            }
            if (mImageGroupListener != null) {
                mImageGroupListener.onImageGroupLeftExpand(index,
                        curLeftPos * 1.0f / getGroupContentMaxWidth(), false);
            }
        }

        void shrinkRight(int size) {
            if (curRightPos == curLeftPos) {
                Logger.e(TAG, "Adjust right, shrink right, current image group reach min size, can not adjust!");
                return;
            }

            int remain = size;
            if (remain > (curRightPos - curLeftPos)) {
                remain = curRightPos - curLeftPos;
                size = remain;
                Logger.w(TAG, "Adjust right, shrink right, remain too big, adjust to: " + remain);
            }

            Logger.d(TAG, "Adjust right, shrink right, remain: " + remain);
            int leftIndex = mCurImageGroup.getLeftIndex();
            int rightIndex = mCurImageGroup.getRightIndex();
            Logger.d(TAG, "Adjust right, shrink right, left index: " + leftIndex + ", right index: " + rightIndex);
            for (int i = rightIndex; i >= leftIndex; i--) {
                ImageItem imageItem = mCurImageGroup.imageItemList.get(i);
                if (imageItem.getWidth() >= remain) {
                    imageItem.srcRect.right -= remain;
                    remain = 0;
                } else {
                    remain -= imageItem.getWidth();
                    imageItem.srcRect.right = imageItem.srcRect.left;
                }

                if (remain == 0) {
                    break;
                }
            }
            curRightPos -= size;

            Logger.d(TAG, "Adjust image group, shrink right, progress: " + curRightPos * 1.0f / getGroupContentMaxWidth());
            int index = 0;
            for (int i = 0; i < mImageGroupList.size(); i++) {
                ImageGroup imageGroup = mImageGroupList.get(i);
                if (imageGroup == this) {
                    break;
                }
                if (!mImageGroupList.get(i).isHidden) {
                    index++;
                }
            }
            if (mImageGroupListener != null) {
                mImageGroupListener.onImageGroupRightShrink(index,
                        curRightPos * 1.0f / getGroupContentMaxWidth(), false);
            }
        }

        void expandRight(int size) {
            if (curRightPos == RIGHT_BOUND) {
                Logger.e(TAG, "Adjust right, expand right, current image group reach right bound, can not adjust!");
                return;
            }

            int remain = size;
            if (remain > (RIGHT_BOUND - curRightPos)) {
                remain = RIGHT_BOUND - curRightPos;
                size = remain;
                Logger.w(TAG, "Adjust right, expand right, remain too big, adjust to : " + remain);
            }

            Logger.d(TAG, "Adjust right, expand right, remain: " + remain);
            int rightIndex = mCurImageGroup.getRightIndex();
            Logger.d(TAG, "Adjust right, expand right, right index: " + rightIndex + ", right image item: " + mCurImageGroup.imageItemList.get(rightIndex));
            for (int i = rightIndex; i < mCurImageGroup.imageItemList.size(); i++) {
                ImageItem imageItem = mCurImageGroup.imageItemList.get(i);
                Logger.d(TAG, "Adjust right, expand right, adjust image item, before: " + imageItem + ", remain: " + remain);
                if ((imageItem.rightBound - imageItem.srcRect.right) >= remain) {
                    imageItem.srcRect.right += remain;
                    remain = 0;
                } else {
                    remain -= (imageItem.rightBound - imageItem.srcRect.right);
                    imageItem.srcRect.right = imageItem.rightBound;
                }

                Logger.d(TAG, "Adjust right, expand right, adjust image item, after: " + imageItem + ", remain: " + remain);
                if (remain == 0) {
                    break;
                }
            }
            curRightPos += size;

            Logger.d(TAG, "Adjust image group, expand right, progress: " + curRightPos * 1.0f / getGroupContentMaxWidth());
            int index = 0;
            for (int i = 0; i < mImageGroupList.size(); i++) {
                ImageGroup imageGroup = mImageGroupList.get(i);
                if (imageGroup == this) {
                    break;
                }
                if (!mImageGroupList.get(i).isHidden) {
                    index++;
                }
            }
            if (mImageGroupListener != null) {
                mImageGroupListener.onImageGroupRightExpand(index, curRightPos * 1.0f / getGroupContentMaxWidth(), false);
            }
        }

        // 根据left pos获取当前左边的index
        int getLeftIndex() {
            int index = 0;
            int curPos = LEFT_BOUND;

            if (curLeftPos == RIGHT_BOUND) {
                index = mCurImageGroup.imageItemList.size() - 1;
            } else {
                for (int i = 0; i < imageItemList.size(); i++) {
                    ImageItem imageItem = imageItemList.get(i);
                    if (curLeftPos >= curPos && curLeftPos < (curPos + imageItem.getMaxWidth())) {
                        index = i;
                        break;
                    }
                    curPos += imageItem.getMaxWidth();
                }
            }

            return index;
        }

        int getRightIndex() {
            int index = imageItemList.size() - 1;
            int curPos = LEFT_BOUND;

            for (int i = 0; i < imageItemList.size(); i++) {
                ImageItem imageItem = imageItemList.get(i);
                if (curRightPos >= curPos && curRightPos < (curPos + imageItem.getMaxWidth())) {
                    index = i;
                    break;
                }
                curPos += imageItem.getMaxWidth();
            }

            return index;
        }

        public void addImageItem(ImageItem imageItem) {
            imageItemList.add(imageItem);
        }

        public int getWidth() {
            int width = 0;
            for (ImageItem imageItem : imageItemList) {
                width += imageItem.getWidth();
            }
            return width;
        }

        public int getMaxWidth() {
            int maxWidth = 0;
            for (ImageItem imageItem : imageItemList) {
                maxWidth += imageItem.getMaxWidth();
            }

            return maxWidth;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public boolean isHidden() {
            return isHidden;
        }

        public float getLeftBound() {
            return LEFT_BOUND * 1.0f / getGroupContentMaxWidth();
        }

        public float getRightBound() {
            return RIGHT_BOUND * 1.0f / getGroupContentMaxWidth();
        }

        /**
         * 获得当前左边百分比位置
         *
         * @return 左边的百分比位置
         */
        public float getCurLeftPos() {
            return curLeftPos * 1.0f / getGroupContentMaxWidth();
        }

        /**
         * 获得当前右边百分比位置
         *
         * @return 右边百分比位置
         */
        public float getCurRightPos() {
            return curRightPos * 1.0f / getGroupContentMaxWidth();
        }

        public int getMeasuredLeft() {
            return measuredLeft;
        }

        public int getMeasuredRight() {
            return measuredRight;
        }

        void notifyPosX(int posX, int oldPosX) {
            if ((posX + mPaddingStartWidth + mGroupPaddingWidth) >= measuredLeft &&
                    (posX + mPaddingStartWidth + mGroupPaddingWidth) <= measuredRight &&
                    mImageGroupListener != null) {
                int index = mImageGroupList.indexOf(this);
                float progress = (curLeftPos + posX + mPaddingStartWidth + mGroupPaddingWidth - measuredLeft) * 1.0f / getGroupContentMaxWidth();
                Log.d(TAG, "notifyPosX: index: " + index +
                        ", pos x: " + posX +
                        ", old pos x: " + oldPosX +
                        ", cur left pos: " + curLeftPos +
                        ", padding start: " + mPaddingStartWidth +
                        ", group padding: " + mGroupPaddingWidth +
                        ", measure left: " + measuredLeft +
                        ", measure right: " + measuredRight +
                        ", progress: " + progress);

                if ((oldPosX + mPaddingStartWidth + mGroupPaddingWidth) == measuredLeft ||
                        (posX + mPaddingStartWidth + mGroupPaddingWidth) == measuredLeft) {
                    Logger.d(TAG, "notifyPosX: reach start!!!!!!!!! index: " + index);
                    mImageGroupListener.onImageGroupStart(index, mIsScrollFromUser);
                }
                mImageGroupListener.onImageGroupProcess(index, progress, mIsScrollFromUser);
                if ((posX + mPaddingStartWidth + mGroupPaddingWidth) == measuredRight) {
                    Logger.d(TAG, "notifyPosX: reach end!!!!!!!!! index: " + index);
                    mImageGroupListener.onImageGroupEnd(index, mIsScrollFromUser);
                }
            }
        }

        @Override
        public String toString() {
            return "ImageGroup{" +
                    "imageItemList=" + imageItemList +
                    "\n, isSelected=" + isSelected +
                    "\n, isHidden=" + isHidden +
                    "\n, LEFT_BOUND=" + LEFT_BOUND +
                    "\n, RIGHT_BOUND=" + RIGHT_BOUND +
                    "\n, curLeftPos=" + curLeftPos +
                    "\n, curRightPos=" + curRightPos +
                    "\n, measuredLeft=" + measuredLeft +
                    "\n, measuredRight=" + measuredRight +
                    '}';
        }
    }

    public static class ImageGroupListener {
        public void onImageGroupLeftShrink(int index, float leftProgress, boolean isLast) {

        }

        public void onImageGroupLeftExpand(int index, float leftProgress, boolean isLast) {

        }

        public void onImageGroupRightShrink(int index, float rightProgress, boolean isLast) {

        }

        public void onImageGroupRightExpand(int index, float rightProgress, boolean isLast) {

        }

        public void onImageGroupSplit(int index, float splitProgress) {

        }

        public void onImageGroupHidden(int index) {

        }

        public void onImageGroupStart(int index, boolean isFromUser) {

        }

        public void onImageGroupProcess(int index, float progress, boolean isFromUser) {

        }

        public void onImageGroupEnd(int index, boolean isFromUser) {

        }
    }
}
