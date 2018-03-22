package com.fzui.libs.FzTabLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.fzui.libs.R;

/**
 * 搜索云标签
 */
public class FzFloatLayout extends ViewGroup {
    private int mChildHorizontalSpacing;
    private int mChildVerticalSpacing;

    private int mGravity;

    private static final int LINES = 0;
    private static final int NUMBER = 1;
    private int mMaxMode = LINES;
    private int mMaximum = Integer.MAX_VALUE;


    private int[] mItemNumberInEachLine;

    private int[] mWidthSumInEachLine;

    private int measuredChildCount;

    public FzFloatLayout(Context context) {
        this(context, null);
    }

    public FzFloatLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FzFloatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.FzFloatLayout);
        mChildHorizontalSpacing = array.getDimensionPixelSize(
                R.styleable.FzFloatLayout_fz_childHorizontalSpacing, 0);
        mChildVerticalSpacing = array.getDimensionPixelSize(
                R.styleable.FzFloatLayout_fz_childVerticalSpacing, 0);
        mGravity = array.getInteger(R.styleable.FzFloatLayout_android_gravity, Gravity.LEFT);
        int maxLines = array.getInt(R.styleable.FzFloatLayout_android_maxLines, -1);
        if (maxLines >= 0) {
            setMaxLines(maxLines);
        }
        int maxNumber = array.getInt(R.styleable.FzFloatLayout_fz_maxNumber, -1);
        if (maxNumber >= 0) {
            setMaxNumber(maxNumber);
        }
        array.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int maxLineHeight = 0;

        int resultWidth;
        int resultHeight;

        final int count = getChildCount();

        mItemNumberInEachLine = new int[count];
        mWidthSumInEachLine = new int[count];
        int lineIndex = 0;


        if (widthSpecMode == MeasureSpec.EXACTLY) {
            resultWidth = widthSpecSize;

            measuredChildCount = 0;

            int childPositionX = getPaddingLeft();
            int childPositionY = getPaddingTop();

            int childMaxRight = widthSpecSize - getPaddingRight();

            for (int i = 0; i < count; i++) {
                if (mMaxMode == NUMBER && measuredChildCount >= mMaximum) {
                    break;
                } else if (mMaxMode == LINES && lineIndex >= mMaximum) {
                    break;
                }

                final View child = getChildAt(i);
                if (child.getVisibility() == GONE) {
                    continue;
                }

                final LayoutParams childLayoutParams = child.getLayoutParams();
                final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                        getPaddingLeft() + getPaddingRight(), childLayoutParams.width);
                final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                        getPaddingTop() + getPaddingBottom(), childLayoutParams.height);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

                final int childw = child.getMeasuredWidth();
                maxLineHeight = Math.max(maxLineHeight, child.getMeasuredHeight());
                if (childPositionX + childw > childMaxRight) {
                    if (mMaxMode == LINES) {
                        if (lineIndex + 1 >= mMaximum) {
                            break;
                        }
                    }
                    mWidthSumInEachLine[lineIndex] -= mChildHorizontalSpacing;
                    lineIndex++;
                    childPositionX = getPaddingLeft();
                    childPositionY += maxLineHeight + mChildVerticalSpacing;
                }
                mItemNumberInEachLine[lineIndex]++;
                mWidthSumInEachLine[lineIndex] += (childw + mChildHorizontalSpacing);
                childPositionX += (childw + mChildHorizontalSpacing);
                measuredChildCount++;
            }

            if (mWidthSumInEachLine.length > 0 && mWidthSumInEachLine[lineIndex] > 0) {
                mWidthSumInEachLine[lineIndex] -= mChildHorizontalSpacing;
            }
            if (heightSpecMode == MeasureSpec.UNSPECIFIED) {
                resultHeight = childPositionY + maxLineHeight + getPaddingBottom();
            } else if (heightSpecMode == MeasureSpec.AT_MOST) {
                resultHeight = childPositionY + maxLineHeight + getPaddingBottom();
                resultHeight = Math.min(resultHeight, heightSpecSize);
            } else {
                resultHeight = heightSpecSize;
            }

        } else {
            resultWidth = getPaddingLeft() + getPaddingRight();
            measuredChildCount = 0;

            for (int i = 0; i < count; i++) {
                if (mMaxMode == NUMBER) {
                    if (measuredChildCount > mMaximum) {
                        break;
                    }
                } else if (mMaxMode == LINES) {
                    if (1 > mMaximum) {
                        break;
                    }
                }
                final View child = getChildAt(i);
                if (child.getVisibility() == GONE) {
                    continue;
                }
                final LayoutParams childLayoutParams = child.getLayoutParams();
                final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                        getPaddingLeft() + getPaddingRight(), childLayoutParams.width);
                final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                        getPaddingTop() + getPaddingBottom(), childLayoutParams.height);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                resultWidth += child.getMeasuredWidth();
                maxLineHeight = Math.max(maxLineHeight, child.getMeasuredHeight());
                measuredChildCount++;
            }
            if (measuredChildCount > 0) {
                resultWidth += mChildHorizontalSpacing * (measuredChildCount - 1);
            }
            resultHeight = maxLineHeight + getPaddingTop() + getPaddingBottom();
            if (mItemNumberInEachLine.length > 0) {
                mItemNumberInEachLine[lineIndex] = count;
            }
            if (mWidthSumInEachLine.length > 0) {
                mWidthSumInEachLine[0] = resultWidth;
            }
        }
        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = right - left;
        switch (mGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                layoutWithGravityLeft(width);
                break;
            case Gravity.RIGHT:
                layoutWithGravityRight(width);
                break;
            case Gravity.CENTER_HORIZONTAL:
                layoutWithGravityCenterHorizontal(width);
                break;
            default:
                layoutWithGravityLeft(width);
                break;
        }
    }


    private void layoutWithGravityCenterHorizontal(int parentWidth) {
        int nextChildIndex = 0;
        int nextChildPositionX;
        int nextChildPositionY = getPaddingTop();
        int lineHeight = 0;

        for (int i = 0; i < mItemNumberInEachLine.length; i++) {
            if (mItemNumberInEachLine[i] == 0) {
                break;
            }

            if (nextChildIndex > measuredChildCount - 1) {
                break;
            }

            nextChildPositionX = (parentWidth - getPaddingLeft() - getPaddingRight() - mWidthSumInEachLine[i]) / 2 + getPaddingLeft();
            for (int j = nextChildIndex; j < nextChildIndex + mItemNumberInEachLine[i]; j++) {
                final View childView = getChildAt(j);
                if (childView.getVisibility() == GONE) {
                    continue;
                }
                final int childw = childView.getMeasuredWidth();
                final int childh = childView.getMeasuredHeight();
                childView.layout(nextChildPositionX, nextChildPositionY, nextChildPositionX + childw, nextChildPositionY + childh);
                lineHeight = Math.max(lineHeight, childh);
                nextChildPositionX += childw + mChildHorizontalSpacing;
            }

            nextChildPositionY += (lineHeight + mChildVerticalSpacing);
            nextChildIndex += mItemNumberInEachLine[i];
            lineHeight = 0;
        }

        int childCount = getChildCount();
        if (measuredChildCount < childCount) {
            for (int i = measuredChildCount; i < childCount; i++) {
                final View childView = getChildAt(i);
                if (childView.getVisibility() == GONE) {
                    continue;
                }
                childView.layout(0, 0, 0, 0);
            }
        }
    }


    private void layoutWithGravityLeft(int parentWidth) {
        int childMaxRight = parentWidth - getPaddingRight();
        int childPositionX = getPaddingLeft();
        int childPositionY = getPaddingTop();
        int lineHeight = 0;
        final int childCount = getChildCount();
        final int childCountToLayout = Math.min(childCount, measuredChildCount);
        for (int i = 0; i < childCountToLayout; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final int childw = child.getMeasuredWidth();
            final int childh = child.getMeasuredHeight();
            if (childPositionX + childw > childMaxRight) {
                childPositionX = getPaddingLeft();
                childPositionY += (lineHeight + mChildVerticalSpacing);
                lineHeight = 0;
            }
            child.layout(childPositionX, childPositionY, childPositionX + childw, childPositionY + childh);
            childPositionX += childw + mChildHorizontalSpacing;
            lineHeight = Math.max(lineHeight, childh);
        }

        if (measuredChildCount < childCount) {
            for (int i = measuredChildCount; i < childCount; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() == GONE) {
                    continue;
                }
                child.layout(0, 0, 0, 0);
            }
        }
    }


    private void layoutWithGravityRight(int parentWidth) {
        int nextChildIndex = 0;
        int nextChildPositionX;
        int nextChildPositionY = getPaddingTop();
        int lineHeight = 0;

        for (int i = 0; i < mItemNumberInEachLine.length; i++) {
            if (mItemNumberInEachLine[i] == 0) {
                break;
            }

            if (nextChildIndex > measuredChildCount - 1) {
                break;
            }

            nextChildPositionX = parentWidth - getPaddingRight() - mWidthSumInEachLine[i];
            for (int j = nextChildIndex; j < nextChildIndex + mItemNumberInEachLine[i]; j++) {
                final View childView = getChildAt(j);
                if (childView.getVisibility() == GONE) {
                    continue;
                }
                final int childw = childView.getMeasuredWidth();
                final int childh = childView.getMeasuredHeight();
                childView.layout(nextChildPositionX, nextChildPositionY, nextChildPositionX + childw, nextChildPositionY + childh);
                lineHeight = Math.max(lineHeight, childh);
                nextChildPositionX += childw + mChildHorizontalSpacing;
            }

            nextChildPositionY += (lineHeight + mChildVerticalSpacing);
            nextChildIndex += mItemNumberInEachLine[i];
            lineHeight = 0;
        }

        int childCount = getChildCount();
        if (measuredChildCount < childCount) {
            for (int i = measuredChildCount; i < childCount; i++) {
                final View childView = getChildAt(i);
                if (childView.getVisibility() == GONE) {
                    continue;
                }
                childView.layout(0, 0, 0, 0);
            }
        }
    }


    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            requestLayout();
        }
    }

    public int getGravity() {
        return mGravity;
    }

    public void setMaxNumber(int maxNumber) {
        mMaximum = maxNumber;
        mMaxMode = NUMBER;
        requestLayout();
    }


    public int getMaxNumber() {
        return mMaxMode == NUMBER ? mMaximum : -1;
    }


    public void setMaxLines(int maxLines) {
        mMaximum = maxLines;
        mMaxMode = LINES;
        requestLayout();
    }


    public int getMaxLines() {
        return mMaxMode == LINES ? mMaximum : -1;
    }


    public void setChildHorizontalSpacing(int spacing) {
        mChildHorizontalSpacing = spacing;
        invalidate();
    }


    public void setChildVerticalSpacing(int spacing) {
        mChildVerticalSpacing = spacing;
        invalidate();
    }
}
